package com.pmdm.pokedex.model.repository.dataretriever

import com.pmdm.pokedex.model.Pokemon
import com.pmdm.pokedex.model.PokemonStat
import com.pmdm.pokedex.model.PokemonType
import com.pmdm.pokedex.model.getPokemonColorAssignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


class OfflinePokeApiV2PokemonDataRetriever: PokemonDataRetriever {

    val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getAllPokemon(): List<Pokemon> {
        TODO("Not yet implemented")
    }

    override suspend fun getPokemon(limit: Int, offset: Int): List<Pokemon> {
        TODO("Not yet implemented")
    }

    override suspend fun getPokemon(id: Int): Pokemon {
        return parsePokemon(getJSONResource("res/raw/pokemon$id.json"))
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
            imageURL = "null"
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

    override suspend fun getPokemonList(): Map<Int, String> {
        var pokemonMap = mutableMapOf<Int, String>()
        pokemonMap.putAll(getPokemonListCyclesDistribution())
        return pokemonMap
    }

    private suspend fun getPokemonListCyclesDistribution(): Map<Int, String> {
        val pokemonMap = mutableMapOf<Int, String>()
        val pokemonListJSONArray = getJSONResource("res/raw/pokemonlist.json").get("results") as JSONArray

        val jobs = ArrayList<Job>()
        val jobNumber = Math.max(1, 3000/5)

        for (i in 0 until jobNumber) {
            jobs.add(
                scope.async {
                    val subList = mutableMapOf<Int, String>()
                    for (j in i until pokemonListJSONArray.length() step jobNumber) {
                        val pokemonIdentifier = pokemonListJSONArray[j] as JSONObject
                        val url = pokemonIdentifier.get("url").toString()
                        val id = Regex("""/(\d+)/""").find(url)?.groupValues?.get(1)!!.toInt()
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

    private suspend fun getJSONResource(filename: String) : JSONObject {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filename)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val content = StringBuilder()
        var line: String?
        while (withContext(Dispatchers.IO) {
                reader.readLine()
            }.also { line = it } != null) {
            content.append(line)
        }
        return JSONObject(content.toString())
    }
}