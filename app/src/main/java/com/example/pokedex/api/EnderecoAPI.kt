package com.example.pokedex.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface PokeApiServiceByName {
    /*fun getPokemon(
        @Path("name") name: String)
    : Call<PokemonResponse>*/

    @GET("pokemon/{name}")
    suspend fun recuperarPokemonByName(
        @Path("name") name: String
    )
    : Response<PokemonResponse>

}

interface PokeApiServiceByID {

    @GET("pokemon/{id}")
    suspend fun recuperarPokemonByID(
        @Path("id") id: Int
    )
    : Response<PokemonResponse>

}



/*
interface PokeApiServiceByID {
    @GET("pokemon/{id}")
    fun getPokemonID(
        @Path("id") id: Int)
    : Call<PokemonResponse>
}*/
