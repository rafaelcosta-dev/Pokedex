package com.example.pokedex.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
class ApiClient {

    companion object {

        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        val pokeApiServiceByName: PokeApiServiceByName by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(PokeApiServiceByName::class.java)
        }

        val pokeApiServiceById: PokeApiServiceByID by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(PokeApiServiceByID::class.java)
        }
    }


}*/

class RetroFitHelper {

    companion object {

        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}
