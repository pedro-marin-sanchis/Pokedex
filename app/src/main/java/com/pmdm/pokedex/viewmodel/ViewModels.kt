package com.pmdm.pokedex.viewmodel

import com.pmdm.pokedex.model.dataretriever.PokeApiV2PokemonDataRetriever
import com.pmdm.pokedex.model.dataretriever.PokemonDataRetriever

val dataRetriever: PokemonDataRetriever = PokeApiV2PokemonDataRetriever()
val pokedexTopAppBarViewModel = PokedexTopAppBarViewModel()
val pokemonPLPViewModel = PokemonPLPViewModel()
val pokemonPDPViewModel = PokemonPDPViewModel()