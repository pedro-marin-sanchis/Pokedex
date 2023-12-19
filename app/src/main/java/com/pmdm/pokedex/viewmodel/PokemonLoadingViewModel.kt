package com.pmdm.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokemonLoadingViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>((true))
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}