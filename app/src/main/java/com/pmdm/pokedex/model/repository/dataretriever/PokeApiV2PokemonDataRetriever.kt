package com.pmdm.pokedex.model.repository.dataretriever

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.pmdm.pokedex.model.Pokemon
import com.pmdm.pokedex.model.PokemonStat
import com.pmdm.pokedex.model.PokemonType
import com.pmdm.pokedex.model.getPokemonColorAssignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import kotlin.system.measureTimeMillis


open class PokeApiV2PokemonDataRetriever(val context: Context): PokemonDataRetriever {

    val scope = CoroutineScope(Dispatchers.IO)
    private val _pokeApiHost = "pokeapi.co"
    val _limit: Int = 5000
    val _offset: Int = 0

    init {
        if (!checkInternetConnection(context)) {
            throw Exception()
        }
    }

    private fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }
        return false
    }

    override suspend fun getPokemonList(): Map<Int, String> {
        return scope.async {
            runBlocking {
                val pokemonMap = mutableMapOf<Int, String>()
                val loadTime = measureTimeMillis {
                    pokemonMap.putAll(getPokemonListCyclesDistribution(_limit, _offset))
                }
                println("POKEMON: ${pokemonMap}")
                println("POKEMON LIST LOAD TIME: ${loadTime.toFloat()/1000}s")
                pokemonMap
            }
        }.await()
    }

    private suspend fun getPokemonListCyclesDistribution(limit: Int, offset: Int): Map<Int, String> {
        val pokemonMap = mutableMapOf<Int, String>()
        val apiURL = "https://$_pokeApiHost/api/v2/pokemon?limit=$limit&offset=$offset"
        val pokemonListJSONArray = getResponseAsJSONObject(apiURL).get("results") as JSONArray

        val jobs = ArrayList<Job>()
        val jobNumber = Math.max(1, _limit/5)

        for (i in 0 until jobNumber) {
            jobs.add(
                scope.async {
                    val subList = mutableMapOf<Int, String>()
                    for (j in i until pokemonListJSONArray.length() step jobNumber) {
                        val pokemonIdentifier = pokemonListJSONArray[j] as JSONObject
                        val url = pokemonIdentifier.get("url").toString()
                        val id = Regex("""/(\d+)/""").find(url)?.groupValues?.get(1)!!.toInt()
                        //println(Regex("""/(\d+)/""").find("https://pokeapi.co/api/v2/pokemon/10001/")?.groupValues?.get(1)!!.toInt())
                        if ( id < 1018 ) { // To skip evolutions ( Starting at 1018 )
                            subList[id] = pokemonIdentifier.get("name").toString().replaceFirstChar { "$it".uppercase() }
                        }
                    }
                    synchronized(pokemonMap) {
                        pokemonMap.putAll(subList)
                    }
                }
            )
        }

        jobs.joinAll()
        return pokemonMap
    }

    override suspend fun getAllPokemon(): List<Pokemon> = getPokemon(_limit, _offset)

    override suspend fun getPokemon(limit: Int, offset: Int): List<Pokemon> {
        return scope.async {
            runBlocking {
                val pokemonList = mutableListOf<Pokemon>()
                val loadTime = measureTimeMillis {
                    pokemonList.addAll(getPokemonCyclesDistribution(limit, offset))
                }
                println("POKEMON LOAD TIME: ${loadTime.toFloat()/1000}s")
                pokemonList
            }
        }.await()
    }

    override suspend fun getPokemon(id: Int): Pokemon = getPokemon(1, id-1).first()

    private suspend fun getPokemonCyclesDistribution(limit: Int, offset: Int): List<Pokemon> {
        val pokemonList = mutableListOf<Pokemon>()
        val apiURL = "https://$_pokeApiHost/api/v2/pokemon?limit=$limit&offset=$offset"
        val pokemonListJSONArray = getResponseAsJSONObject(apiURL).get("results") as JSONArray

        val jobs = ArrayList<Job>()
        val jobNumber = Math.max(1, _limit/5)

        for (i in 0 until jobNumber) {
            jobs.add(
                scope.async {
                    val subList = mutableListOf<Pokemon>()
                    for (j in i until pokemonListJSONArray.length() step jobNumber) {
                        val pokemonIdentifier = pokemonListJSONArray[j] as JSONObject
                        val url = pokemonIdentifier.get("url").toString()
                        val id = Regex("""/(\d+)/""").find(url)?.groupValues?.get(1)!!.toInt()
                        if ( id < 1018 ) { // To skip evolutions ( Starting at 1018 )
                            subList.add(parsePokemon(getResponseAsJSONObject(pokemonIdentifier.get("url").toString())))
                        }
                    }
                    synchronized(pokemonList) {
                        pokemonList.addAll(subList)
                    }
                }
            )
        }

        jobs.joinAll()
        return pokemonList
    }

    open suspend fun getResponseAsJSONObject(url: String): JSONObject {
        var responseJSONObject = JSONObject()
        return withContext(Dispatchers.IO) {
            responseJSONObject = JSONObject(URL(url).readText())
//            println("JSON RESPONSE: $responseJSONObject")
            return@withContext responseJSONObject
        }
    }

    private suspend fun parsePokemon(pokemonJSONObject: JSONObject): Pokemon {
        return Pokemon(
            id = pokemonJSONObject.get("id").toString(),
            name = pokemonJSONObject.get("name").toString().replaceFirstChar { "$it".uppercase() },
            color = null,
            height = pokemonJSONObject.get("height").toString().toFloat()/10,
            weight = pokemonJSONObject.get("weight").toString().toFloat()/10,
            types = getPokemonTypeList(pokemonJSONObject),
            stats = getPokemonStatList(pokemonJSONObject),
            imageURL = getPokemonImageURL(pokemonJSONObject)
        )
    }

    private fun getPokemonTypeList(pokemonJSONObject: JSONObject): List<PokemonType> {
        val typeList = mutableListOf<PokemonType>()
        val types = pokemonJSONObject.get("types") as JSONArray
        for (i in 0 until types.length()) {
            val type = types[i] as JSONObject
            val name = (type.get("type") as JSONObject).get("name").toString().replaceFirstChar { "$it".uppercase() }
            typeList.add(PokemonType(name, getPokemonColorAssignment(name)))
        }
        return typeList
    }

    private fun getPokemonStatList(pokemonJSONObject: JSONObject): List<PokemonStat> {
        val statList = mutableListOf<PokemonStat>()
        val stats = pokemonJSONObject.get("stats") as JSONArray
        for (i in 0 until stats.length()) {
            val stat = stats[i] as JSONObject
            val name = (stat.get("stat") as JSONObject).get("name").toString().uppercase()
            val value = (stat.get("base_stat")).toString().toInt()
            statList.add(PokemonStat(name, value, 255, getPokemonColorAssignment(name))) // TODO: IS MAXVALUE REALLY ALWAYS 255?
        }
        return statList
    }

    private suspend fun getPokemonImageURL(pokemonJSONObject: JSONObject): String {
        try {
            val fromURL = ((pokemonJSONObject.get("forms") as JSONArray).get(0) as JSONObject).get("url").toString() // First pokemon form URL.
            val formJSONObject = getResponseAsJSONObject(fromURL)
            val frontDefaultSpriteURL = (formJSONObject.get("sprites") as JSONObject).get("front_default").toString()
            return frontDefaultSpriteURL
        } catch(e: Exception) {
            e.printStackTrace()
        }

        return "https://images4.wikia.nocookie.net/__cb20111010225147/unanything/images/thumb/c/c1/MISSINGNO.png/639px-MISSINGNO.png" // MISSING NO <-- LOL
    }

//    private suspend fun getPokemonSequential(limit: Int, offset: Int): List<Pokemon> {
//        val pokemonList = mutableListOf<Pokemon>()
//        val apiURL = "https://$_pokeApiHost/api/v2/pokemon?limit=$limit&offset=$offset"
//        val pokemonListJSONArray = getResponseAsJSONObject(apiURL).get("results") as JSONArray
//
//        for (i in 0 until  Math.min(pokemonListJSONArray.length(), limit)) {
//            val pokemonIdentifier = pokemonListJSONArray[i] as JSONObject
//            pokemonList.add(parsePokemon(getResponseAsJSONObject(pokemonIdentifier.get("url").toString())))
//        }
//
//        return pokemonList
//    }
}