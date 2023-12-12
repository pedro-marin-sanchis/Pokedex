package com.pmdm.pokedex.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pmdm.pokedex.model.Pokemon
import com.pmdm.pokedex.model.PokemonStat
import com.pmdm.pokedex.model.PokemonType
import com.pmdm.pokedex.viewModel.pokedexTopAppBarViewModel
import com.pmdm.pokedex.viewModel.pokemonPDPViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PokemonPDP() {
    val isLoading by pokemonPDPViewModel.isLoading.observeAsState()

    LoadWaiter(isLoading = isLoading!!) {
        PokemonPDPDisplay()
    }
}

@Composable
private fun PokemonPDPDisplay() {
    val pokemon by pokemonPDPViewModel.pokemon.observeAsState()

    if (navHostController!!.currentBackStackEntry!!.destination.route == "PokemonPDP") {
        pokedexTopAppBarViewModel.setTopBarColor(pokemon!!.getColor())
        pokedexTopAppBarViewModel.setPokemonLabel(pokemon!!.id)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PokemonImageCard(pokemon)
        Spacer(modifier = Modifier.size(15.dp))
        Text(text = pokemon!!.name, color = Color.White, fontSize = 30.sp)
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(pokemon!!.types.size) {
                PokemonTypeCard(pokemon!!.types[it])
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pokemon!!.weight.toString() + " KG",
                    fontSize = 20.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Weight", fontSize = 15.sp, color = Color.Gray,
                )
            }
            Spacer(modifier = Modifier.size(80.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pokemon!!.height.toString() + " M",
                    fontSize = 20.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Height", fontSize = 15.sp, color = Color.Gray,
                )
            }
        }
        Spacer(modifier = Modifier.size(15.dp))
        PokemonBaseStats(pokemon)
    }
}

@Composable
private fun PokemonImageCard(pokemon: Pokemon?) {
    val animatedFractionValue = remember { Animatable(-500f) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            delay(100)
            animatedFractionValue.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = FastOutSlowInEasing
                )
            )
        }

        onDispose {
            job.cancel()
        }
    }

    Card(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .offset(y = with(LocalDensity.current) { animatedFractionValue.value.toDp() }),
        colors = CardDefaults.cardColors(containerColor = pokemon!!.getColor()),
        shape = RoundedCornerShape(0.dp, 0.dp, 32.dp, 32.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box() {
                Box() {
                    AsyncImage(
                        model = pokemon.imageURL,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .blur(5.dp)
                            .graphicsLayer {
                                translationY = 10f
                                translationX = 10f
                                scaleX = 1.1f
                                scaleY = 1.1f
                                alpha = 0.10f
                            },
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Box() {
                    AsyncImage(
                        model = pokemon.imageURL,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        contentScale = ContentScale.Fit,
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonBaseStats(pokemon: Pokemon?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Base Stats", fontSize = 25.sp, color = Color.White)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
        ) {
            items(pokemon!!.stats.size) {
                PokemonStatCard(pokemon!!.stats[it])
            }
        }
    }
}

@Composable
private fun PokemonStatCard(stat: PokemonStat) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Text(
            text = stat.name, color = Color.Gray,
        )
        Spacer(modifier = Modifier.size(10.dp))
        PokemonStatIndicator(stat)
    }
}

@Composable
private fun PokemonStatIndicator(stat: PokemonStat) {
    val statsAnimationPlayed by pokemonPDPViewModel.statsAnimationPlayed.observeAsState()
    val animatedFractionValue = remember { Animatable(-1000f) }
    val animatedTextOpacity = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            if (statsAnimationPlayed!!) {
                animatedFractionValue.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 0,
                        easing = FastOutSlowInEasing
                    )
                )
            } else {
                delay(500L)
                animatedFractionValue.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 2500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }

        onDispose {
            job.cancel()
        }
    }

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            if (statsAnimationPlayed!!) {
                animatedTextOpacity.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 0,
                        easing = FastOutSlowInEasing
                    )
                )
            } else {
                delay(1500)
                animatedTextOpacity.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 2500,
                        easing = FastOutSlowInEasing
                    )
                )
                pokemonPDPViewModel.setStatsAnimationPlayed(true)
            }
        }

        onDispose {
            job.cancel()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(50)
        ) {
            // y = sqrt(x)
            var widthFloat = Math.sqrt((stat.value.toDouble() / stat.maxValue.toDouble())).toFloat()
            if (widthFloat.isNaN()) {
                widthFloat = 0f
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(widthFloat)
                    .offset(x = with(LocalDensity.current) { animatedFractionValue.value.toDp() })
                    .clip(shape = RoundedCornerShape(50))
                    .border(1.dp, stat.color, shape = RoundedCornerShape(50))
                    .background(color = stat.color)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stat.value}/${stat.maxValue}",
                        color = Color.White,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .graphicsLayer {
                                alpha = animatedTextOpacity.value
                            },
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonTypeCard(type: PokemonType) {
    Card(
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .width(180.dp)
            .padding(15.dp),
        colors = CardDefaults.cardColors(containerColor = type.color)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = type.name, color = Color.White,
            )
        }
    }
}