package com.pmdm.pokedex.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokedexTopAppBarViewModel(): ViewModel() {
    private val _textColor = MutableLiveData<Color>(Color.White)
    var textColor: LiveData<Color> = _textColor

    private val _pokemonLabel = MutableLiveData<String>("")
    var pokemonLabel: LiveData<String> = _pokemonLabel

    private val _topBarColor = MutableLiveData<Color>(Color.White)
    var topBarColor: LiveData<Color> = _topBarColor

    fun setPokemonLabel(label: String) {
        _pokemonLabel.value = "# $label"
    }

    fun clearPokemonLabel() {
        _pokemonLabel.value = ""
    }

    fun setTopBarColor(color: Color) {
        _topBarColor.value = color
    }

    fun setTopBarTextColor(color: Color) {
        _textColor.value = color
    }
}