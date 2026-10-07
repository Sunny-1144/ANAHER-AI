package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun AnaherLogo(
    size: Dp = 56.dp,
    showGlowPulse: Boolean = true,
    reducedMotion: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_glow")
    val glowAlpha by if (showGlowPulse && !reducedMotion) {
        infiniteTransition.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.85f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glow_alpha"
        )
    } else {
        rememberUpdatedState(0.5f)
    }

    val tileShape = RoundedCornerShape(size * 0.28f)
    val tileGradient = Brush.linearGradient(
        colors = listOf(
            LogoTileGradientStart,
            LogoTileGradientMid,
            LogoTileGradientEnd
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .shadow(
                elevation = if (reducedMotion) 4.dp else (8 * glowAlpha).dp,
                shape = tileShape,
                ambientColor = LagoonCyanGlow,
                spotColor = LagoonCyanGlow
            )
            .border(
                width = 1.dp,
                color = LagoonCyanGlow.copy(alpha = glowAlpha),
                shape = tileShape
            )
            .background(brush = tileGradient, shape = tileShape)
            .clip(tileShape)
            .padding(size * 0.12f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.anaher_logo),
            contentDescription = "ANAHER Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun AnaherBrandHeader(
    modifier: Modifier = Modifier,
    logoSize: Dp = 64.dp,
    showTagline: Boolean = true,
    reducedMotion: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnaherLogo(
            size = logoSize,
            showGlowPulse = true,
            reducedMotion = reducedMotion
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "ANAHER",
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            letterSpacing = 2.sp,
            color = LagoonPrimary
        )
        if (showTagline) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Describe it. ANAHER builds it.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = LagoonTextSecondaryLight
            )
        }
    }
}
