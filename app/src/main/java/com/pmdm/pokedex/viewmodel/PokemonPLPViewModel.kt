package com.pmdm.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonPLPViewModel(): ViewModel() {
    private val _pokemonList = MutableLiveData<Map<Int, String>>(
        mapOf<Int, String>()
    ) // TODO: Default Pokemon?
    val pokemonList: LiveData<Map<Int, String>> = _pokemonList

    private val _isLoading = MutableLiveData<Boolean>((true))
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val pokemonList = withContext(Dispatchers.IO) {
                dataRetriever!!.getPokemonList()
            }
            _pokemonList.postValue(pokemonList)
            _isLoading.value = false
        }
    }

    fun setIsLoading(boolean: Boolean) {
        _isLoading.value = boolean;
    }

}