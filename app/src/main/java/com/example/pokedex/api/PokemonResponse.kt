package com.example.pokedex.api

import com.google.gson.annotations.SerializedName


data class PokemonResponse(
    val name: String,
    val id: Int,
    val sprites: Sprites,
    val versions: Versions,
    val generationV: GenerationV,
    val blackWhite: BlackWhite,
    val animated: Animated
)

data class Sprites (

    val versions: Versions
)

data class Versions (

    @SerializedName("generation-v")
    val generationV: GenerationV
)

data class GenerationV (

    @SerializedName("black-white")
    val blackWhite: BlackWhite
)

data class BlackWhite (

    val animated: Animated
)

data class Animated (

    @SerializedName("front_default")
    val frontDefault: String
)















