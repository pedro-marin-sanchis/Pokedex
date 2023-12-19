package com.pmdm.pokedex.model.repository.dataretriever

import com.pmdm.pokedex.model.Pokemon

interface PokemonDataRetriever {
    suspend fun getAllPokemon(): List<Pokemon>
    suspend fun getPokemon(limit: Int, offset: Int): List<Pokemon>
    suspend fun getPokemon(id: Int): Pokemon
    suspend fun getPokemonList(): Map<Int, String> // Name, ID
}