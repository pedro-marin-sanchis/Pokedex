package com.pmdm.pokedex.model.dataRetriever

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


class OfflinePokeApiV2PokemonDataRetriever: PokeApiV2PokemonDataRetriever() {
    @Override
    private suspend fun getResponseAsJSONObject(url: String): JSONObject {
        var responseJSONObject = JSONObject()
        var fileName: String = ""

        if (url == "https://pokeapi.co/api/v2/pokemon?limit=${_limit}&offset=${_offset}") {
            fileName == "pokemon-list.json"
        }

        return scope.async {
            val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = StringBuilder()
            var line: String?
            while (withContext(Dispatchers.IO) {
                    reader.readLine()
                }.also { line = it } != null) {
                content.append(line)
            }
            responseJSONObject = JSONObject(content.toString())
            responseJSONObject
        }.await()
    }
}