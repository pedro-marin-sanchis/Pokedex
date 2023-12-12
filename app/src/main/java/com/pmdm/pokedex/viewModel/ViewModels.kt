package com.pmdm.pokedex.viewModel

import com.pmdm.pokedex.model.dataRetriever.OfflinePokeApiV2PokemonDataRetriever
import com.pmdm.pokedex.model.dataRetriever.PokemonDataRetriever

val dataRetriever: PokemonDataRetriever = OfflinePokeApiV2PokemonDataRetriever()
val pokedexTopAppBarViewModel = PokedexTopAppBarViewModel()
val pokemonPLPViewModel = PokemonPLPViewModel()
val pokemonPDPViewModel = PokemonPDPViewModel()