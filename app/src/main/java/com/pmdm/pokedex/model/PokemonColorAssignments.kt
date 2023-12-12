package com.pmdm.pokedex.model

import androidx.compose.ui.graphics.Color

enum class PokemonColorAssignments(val color: Color) {
    // STATS
    HP(Color(0xFFFF3737)),
    ATTACK(Color(0xFFE91E63)),
    DEFENSE(Color(0xFF3F51B5)),
    `SPECIAL-ATTACK`(Color(0xFFCAB600)),
    `SPECIAL-DEFENSE`(Color(0xFF4CAF50)),
    SPEED(Color(0xFF00BCD4)),

    // TYPE
    BUG(Color(0xFFA2B118)),
    DARK(Color(0xFF3F3021)),
    DRAGON(Color(0xFF705CD9)),
    ELECTRIC(Color(0xFFF7B613)),
    FAIRY(Color(0xFFDE8DE0)),
    FIGHTING(Color(0xFF712F1C)),
    FIRE(Color(0xFFE33909)),
    FLYING(Color(0xFF5C72D4)),
    GHOST(Color(0xFF5B5CA6)),
    GRASS(Color(0xFF73C138)),
    GROUND(Color(0xFFD4B870)),
    ICE(Color(0xFF6ECFF4)),
    NORMAL(Color(0xFF928E89)),
    POISON(Color(0xFF7A327C)),
    PSYCHIC(Color(0xFFE13D73)),
    ROCK(Color(0xFF9F8540)),
    STEEL(Color(0xFF8C8B9B)),
    WATER(Color(0xFF0D65C0));
}

fun getPokemonColorAssignment(string: String): Color {
    for (i in PokemonColorAssignments.values()) {
        if (string.uppercase().equals(i.name)) {
            return i.color
        }
    }
    return Color.Gray
}