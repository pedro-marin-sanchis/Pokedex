package com.pmdm.pokedex.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdm.pokedex.model.Pokemon
import com.pmdm.pokedex.model.defaultPokemon
import kotlinx.coroutines.launch

class PokemonPDPViewModel: ViewModel() {
    private val _pokemon = MutableLiveData<Pokemon>(
        defaultPokemon
    )
    val pokemon: LiveData<Pokemon> = _pokemon

    private val _statsAnimationPlayed = MutableLiveData<Boolean>((false))
    val statsAnimationPlayed: LiveData<Boolean> = _statsAnimationPlayed

    private val _isLoading = MutableLiveData<Boolean>((true))
    val isLoading: LiveData<Boolean> = _isLoading

    fun setPokemon(pokemon: Pokemon) {
        _pokemon.value = pokemon
        _statsAnimationPlayed.value = false
    }

    fun setPokemon(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            _pokemon.value = dataRetriever.getPokemon(id)
            _statsAnimationPlayed.value = false
            _isLoading.value = false
        }
    }

    fun setStatsAnimationPlayed(hasPlayed: Boolean) {
        _statsAnimationPlayed.value = hasPlayed
    }
}