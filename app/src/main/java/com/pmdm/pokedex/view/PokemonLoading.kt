package com.pmdm.pokedex.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
private fun PokemonLoading() {
    val value by rememberInfiniteTransition(label = "PLPLoadingElement").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            )
        ), label = "PLPLoading"
    )
    val colors = listOf(
        Color(0x1E000000),
        Color(0xFFFFFFFF),
        Color(0xFFFFFFFF),
        Color(0xFFFFFFFF)
    )
    val gradientBrush by remember {
        mutableStateOf(
            Brush.horizontalGradient(
                colors = colors,
                startX = -100f,
                endX = 300.0f,
                tileMode = TileMode.Repeated
            )
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(modifier = Modifier
                        .padding(40.dp)
                        .drawBehind {
                            drawCircle(
                                color = Color.Transparent,
                                radius = 50.dp.toPx()
                            )
                            rotate(value) {
                                drawCircle(
                                    gradientBrush,
                                    style = Stroke(width = 12.dp.value),
                                    radius = 50.dp.toPx()
                                )
                            }
                        }
                        .size(50.dp)
                        .clickable() {}
                    )
                }
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "LOADING", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun LoadWaiter(isLoading: Boolean, content: @Composable() (BoxScope.() -> Unit)) {
    if (!isLoading) {
        AnimateReveal(delay = 1000, waitFor = 500, content = content)
    }
    AnimateVisibility(isLoading, delay = 200, waitFor = 0) {
        PokemonLoading()
    }
}

@Composable
fun AnimateReveal(delay: Int, waitFor:Long, content: @Composable() (BoxScope.() -> Unit)) {
    val animatedFractionValue = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            delay(waitFor)
            animatedFractionValue.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = delay,
                    easing = FastOutSlowInEasing
                )
            )
        }

        onDispose {
            job.cancel()
        }
    }

    Box(
        modifier = Modifier.alpha(animatedFractionValue.value),
        content = content
    )
}

@Composable
fun AnimateVisibility(
    isVisible: Boolean,
    delay: Int,
    waitFor: Long,
    content: @Composable() (BoxScope.() -> Unit)
) {
    val animatedFractionValue = remember { Animatable(if (isVisible) 1f else 0f) }

    LaunchedEffect(isVisible) {
        delay(waitFor)
        animatedFractionValue.animateTo(
            targetValue = if (isVisible) 1f else 0f,
            animationSpec = tween(
                durationMillis = delay,
                easing = LinearEasing
            )
        )
    }

    Box(
        modifier = Modifier.alpha(animatedFractionValue.value),
        content = content
    )
}