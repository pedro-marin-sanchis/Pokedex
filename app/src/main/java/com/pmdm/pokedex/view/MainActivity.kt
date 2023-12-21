package com.pmdm.pokedex.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmdm.pokedex.model.repository.PokemonRepository
import com.pmdm.pokedex.view.ui.theme.PokedexTheme
import com.pmdm.pokedex.viewmodel.dataRetriever
import dagger.hilt.android.HiltAndroidApp

@SuppressLint("StaticFieldLeak") // TODO: Care about this memory leak.
var navHostController: NavHostController? = null

@HiltAndroidApp
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            dataRetriever = PokemonRepository(LocalContext.current).dataRetriever
            navHostController = rememberNavController()

            PokedexTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        PokedexTopAppBar()
                    }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(it.calculateTopPadding()))
                        NavHost(navController = navHostController!!, startDestination = "PokemonPLP") {
                            composable("PokemonPLP") {
                                PokemonPLP()
                            }
                            composable("PokemonPDP") {
                                PokemonPDP()
                            }
                        }
                    }
                }
            }
        }
    }
}