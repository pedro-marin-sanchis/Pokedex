package com.pmdm.pokedex.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pmdm.pokedex.viewmodel.pokedexTopAppBarViewModel
import com.pmdm.pokedex.viewmodel.pokemonPDPViewModel
import com.pmdm.pokedex.viewmodel.pokemonPLPViewModel

@Composable
fun PokemonPLP() {
    val pokemonList by pokemonPLPViewModel.pokemonList.observeAsState()
    val isLoading by pokemonPLPViewModel.isLoading.observeAsState()
    var currentPage by rememberSaveable {
        mutableIntStateOf(1)
    }
    val itemsPerPage = 50

    if (navHostController!!.currentBackStackEntry!!.destination.route == "PokemonPLP") {
        pokedexTopAppBarViewModel.setTopBarColor(Color(0xFFD36846))
        pokedexTopAppBarViewModel.clearPokemonLabel()
    }

    LoadWaiter(isLoading = isLoading!!) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
            ) {
                item {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp))
                }
                items(pokemonList!!.size) {
                    if (it < currentPage*itemsPerPage && it > (currentPage-1)*itemsPerPage) {
                        val pokemon = pokemonList!!.toSortedMap()[pokemonList!!.toSortedMap().keys.elementAt(it-1)]
                        PokemonCard(id = it, name = pokemon.toString(), navController = navHostController!!)
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.width(50.dp))
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color(
                    0xFFD16745),
                    modifier = Modifier.clickable(
                        onClick = {
                            if (currentPage > 1) {
                                currentPage--
                            }
                        }
                    ))

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f))
                Text(text = ("${currentPage*itemsPerPage-itemsPerPage+1} - ${currentPage*itemsPerPage-1}"), color = Color.White)
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f))

                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color(
                    0xFFD16745),
                    modifier = Modifier.clickable(
                        onClick = {
                            if (currentPage != Integer.MAX_VALUE) {
                                if (currentPage*itemsPerPage < pokemonList!!.size) {
                                    currentPage++
                                }
                            }
                        }
                    ))
                Spacer(modifier = Modifier.width(50.dp))
            }
        }
    }
}

@Composable
fun PokemonCard(id: Int, name: String, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6F666F)),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp)
            .clickable {
                pokemonPDPViewModel.setPokemon(id)
                navController.navigate("PokemonPDP")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = name, color = Color.White)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            Text(text = "# $id", color = Color.White)
        }
    }
}
