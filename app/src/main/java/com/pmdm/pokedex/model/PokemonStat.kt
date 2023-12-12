package com.pmdm.pokedex.model

import androidx.compose.ui.graphics.Color

data class PokemonStat(
    val name: String,
    val value: Int,
    val maxValue: Int,
    val color: Color
) {
    val maxLimit: Int = 255 // Setting a constant maximum limit of 255 for the stat

    init {
        require(value >= 0) { "Value must be non-negative." }
        require(maxValue >= 0) { "MaxValue must be non-negative." }
        require(value <= maxValue) { "Value cannot exceed MaxValue (255)." }
    }

}