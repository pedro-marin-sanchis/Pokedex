package com.pmdm.pokedex.viewModel

import com.pmdm.pokedex.model.dataRetriever.PokeApiV2PokemonDataRetriever
import com.pmdm.pokedex.model.dataRetriever.PokemonDataRetriever

val dataRetriever: PokemonDataRetriever = PokeApiV2PokemonDataRetriever()
val pokedexTopAppBarViewModel = PokedexTopAppBarViewModel()
val pokemonPLPViewModel = PokemonPLPViewModel()
val pokemonPDPViewModel = PokemonPDPViewModel()