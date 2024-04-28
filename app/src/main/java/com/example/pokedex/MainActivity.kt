package com.example.pokedex

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pokedex.api.PokeApiServiceByID
import com.example.pokedex.api.PokeApiServiceByName

import com.example.pokedex.api.PokemonResponse
import com.example.pokedex.api.RetroFitHelper
import com.example.pokedex.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class MainActivity() : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val retrofit by lazy {
        RetroFitHelper.retrofit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //CHAMANDO PRIMEIRO POKEMON QUANDO ABRE O APP

        CoroutineScope(Dispatchers.Main).launch {
            currentPokemon = 1

            recuperarPokemonByID(currentPokemon)
        }

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //CHAMANDO POKEMON NO BUTTON DO AP
        binding.btnSearch.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                var namePoke = binding.editName.text.toString()

                searchPokemon(namePoke)

            }
        }

        //CHAMANDO O PROXIMO POKEMON NO BUTTON NEXT
        binding.btnNext.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                nextPoke()

            }

        }

        //CHAMANDO O POKEMON ANTERIOR NO BUTTON PREVIOUS
        binding.btnPrevious.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                PreviousPoke()
            }
        }

    }


    private suspend fun recuperarPokemonByName(pokemonName: String? = null) {


        //PROGRESS LOADING
        withContext(Dispatchers.Main) {

            binding.nomePokemon.text = "Loading ..."
            binding.numberPokemon.text = ""

        }

        CoroutineScope(Dispatchers.IO).launch {

            var retorno: Response<PokemonResponse>? = null
            val pokemonDigitado = binding.editName.text.toString().lowercase()

            try {

                val enderecoAPI = retrofit.create(PokeApiServiceByName::class.java)
                retorno = enderecoAPI.recuperarPokemonByName(pokemonName.toString())

            } catch (e: Exception) {

                e.printStackTrace()
                Log.i("info_pokemon", pokemonDigitado)

            }

            if (retorno != null) {

                if (retorno.isSuccessful) {

                    val PokemonResponse = retorno.body()
                    val namePokemon = PokemonResponse?.name
                    val idPokemon = PokemonResponse?.id
                    val pokemonImg =
                        retorno.body()?.sprites?.versions?.generationV?.blackWhite?.animated?.frontDefault

                    withContext(Dispatchers.Main) {

                        binding.nomePokemon.text = namePokemon?.capitalize()
                        binding.numberPokemon.text = idPokemon?.toString()

                        val imgPokemon = binding.imgPokemon
                        Glide.with(applicationContext)
                            .asGif()
                            .load(pokemonImg)
                            .into(imgPokemon)
                    }

                } else {

                    withContext(Dispatchers.Main) {

                        binding.nomePokemon.text = "Not Found :["

                    }


                }

            }


        }

    }


    private suspend fun recuperarPokemonByID(pokemonId: Int? = null) {

        withContext(Dispatchers.Main) {

            binding.nomePokemon.text = "Loading ..."
            binding.numberPokemon.text = ""

        }

        var retorno: Response<PokemonResponse>? = null

        val pokemonDigitado = binding.editName.text.toString()


        try {

            val enderecoAPI = retrofit.create(PokeApiServiceByID::class.java)
            if (pokemonId != null) {
                retorno = enderecoAPI.recuperarPokemonByID(pokemonId.toInt())
            }

        } catch (e: Exception) {

            e.printStackTrace()
            Log.i("info_pokemon", pokemonDigitado)

        }

        if (retorno != null) {

            if (retorno.isSuccessful) {

                val PokemonResponse = retorno.body()
                val namePokemon = PokemonResponse?.name
                val idPokemon = PokemonResponse?.id
                val pokemonImg =
                    retorno.body()?.sprites?.versions?.generationV?.blackWhite?.animated?.frontDefault

                withContext(Dispatchers.Main) {

                    binding.nomePokemon.text = namePokemon?.capitalize()
                    binding.numberPokemon.text = idPokemon?.toString()
                    val imgPokemon = binding.imgPokemon
                    Glide.with(applicationContext)
                        .asGif()
                        .load(pokemonImg)
                        .into(imgPokemon)
                }

            } else {

                withContext(Dispatchers.Main) {

                    binding.nomePokemon.text = "Not Found :["

                }
            }

        }


    }


    var currentPokemon:Int? = 1

    private suspend fun nextPoke() {

        if (currentPokemon != null){

            if (currentPokemon == 649) {
                currentPokemon = 1
                recuperarPokemonByID(currentPokemon)
            } else {
                currentPokemon?.let { currentPokemon = it + 1 }
                val nextId= currentPokemon
                recuperarPokemonByID(nextId)

            }
        }


    }

    private suspend fun PreviousPoke() {

        if (currentPokemon != null){

            if (currentPokemon == 1) {
                currentPokemon = 649
                recuperarPokemonByID(currentPokemon)
            } else {
                currentPokemon?.let { currentPokemon = it - 1 }
                val nextId= currentPokemon
                recuperarPokemonByID(nextId)

            }
        }



    }


    //DEFININDO SE ESTÁ SENDO CHAMADO PELO ID OU PELO NOME DO POKEMON
    suspend fun searchPokemon(input: String) {


        val isNumeric = input.all {
            it.isDigit()
        }

        if (isNumeric) {

            currentPokemon = input.toIntOrNull()

            if (currentPokemon != null && currentPokemon in 1..649) {

                recuperarPokemonByID(currentPokemon)

                withContext(Dispatchers.Main){
                    binding.imgPokemon.isVisible = true
                    binding.editName.setText("")

                }

            } else {

                withContext(Dispatchers.Main) {

                    binding.nomePokemon.text = "From 1 to 649"
                    binding.numberPokemon.text = ""
                    binding.imgPokemon.isVisible = false

                }

            }

        } else {

            recuperarPokemonByName(input)
            binding.editName.setText("")
            binding.imgPokemon.isVisible = true

        }


    }
}





