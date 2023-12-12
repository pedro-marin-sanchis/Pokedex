package com.pmdm.pokedex.model

import androidx.compose.ui.graphics.Color

data class Pokemon(
    val id: String, // Number in the pokedex
    val name: String,

    var color: Color?,

    val height: Float,
    val weight: Float,

    val types: List<PokemonType>,
    val stats: List<PokemonStat>,

    val imageURL: String, // URL to sprite.
) {

    fun getColor(): Color {
        if (color == null) {
            color = getAverageTypeColor()
        }
        return color as Color
    }

    private fun getAverageTypeColor(): Color {
        var averageColor = Color.Black
        var totalRed = 0f
        var totalGreen = 0f
        var totalBlue = 0f

        // Calculate the total sum of red, green, and blue components
        for (type in types) {
            totalRed += type.color.red
            totalGreen += type.color.green
            totalBlue += type.color.blue
        }

        // Calculate the average of red, green, and blue components
        val averageRed = totalRed / types.size
        val averageGreen = totalGreen / types.size
        val averageBlue = totalBlue / types.size

        averageColor = Color(averageRed, averageGreen, averageBlue)
        return averageColor
    }
}

val defaultPokemon = Pokemon(
    id = "0",
    name = "MissingNo.",
    color = Color.Gray,
    height = 0.0f,
    weight = 0.0f,
    types = listOf(PokemonType("Glitch", Color.Magenta)),
    stats = listOf(
        PokemonStat("HP", 0,0, getPokemonColorAssignment("HP")),
        PokemonStat("Attack", 1, 1, getPokemonColorAssignment("ATTACK")),
        PokemonStat("Defense", 1, 1, getPokemonColorAssignment("DEFENSE")),
    ),
    imageURL = "https://images4.wikia.nocookie.net/__cb20111010225147/unanything/images/thumb/c/c1/MISSINGNO.png/639px-MISSINGNO.png"
)