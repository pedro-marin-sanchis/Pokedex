package com.pmdm.pokedex.model.repository

import android.content.Context
import com.pmdm.pokedex.model.repository.dataretriever.OfflinePokeApiV2PokemonDataRetriever
import com.pmdm.pokedex.model.repository.dataretriever.PokeApiV2PokemonDataRetriever
import com.pmdm.pokedex.model.repository.dataretriever.PokemonDataRetriever

class PokemonRepository(val context: Context) {
        var dataRetriever: PokemonDataRetriever? = null;
        init {
            try {
                dataRetriever = PokeApiV2PokemonDataRetriever(context = context)
            } catch (e: Exception) {
                dataRetriever = OfflinePokeApiV2PokemonDataRetriever()
            }
        }
}