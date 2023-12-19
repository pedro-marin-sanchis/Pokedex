package com.pmdm.pokedex.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pmdm.pokedex.R
import com.pmdm.pokedex.view.ui.theme.setStatusBarColor
import com.pmdm.pokedex.viewmodel.pokedexTopAppBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexTopAppBar() {
    val pokemonLabel by pokedexTopAppBarViewModel.pokemonLabel.observeAsState()
    val topBarColor by pokedexTopAppBarViewModel.topBarColor.observeAsState()
    val topBarTextColor by pokedexTopAppBarViewModel.textColor.observeAsState()
    setStatusBarColor(topBarColor!!)

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navHostController!!.popBackStack()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back",
                        tint = topBarTextColor!!)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = stringResource(id = R.string.app_name), color = topBarTextColor!!)
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f))
                Text(text = pokemonLabel!!, color = topBarTextColor!!)
                Spacer(modifier = Modifier.width(30.dp))
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = topBarColor!!
        )
    )
}