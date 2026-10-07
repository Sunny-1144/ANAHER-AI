package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnaherLogo
import com.example.ui.theme.*
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceModeSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSendTranscript: (String) -> Unit,
    reducedMotion: Boolean = false
) {
    if (!isOpen) return

    var isListening by remember { mutableStateOf(true) }
    var transcript by remember { mutableStateOf("Listening to your voice intent...") }

    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val wavePhase by if (!reducedMotion) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 6.28f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "wave_phase"
        )
    } else {
        rememberUpdatedState(0f)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AbyssCanvasDark,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = AbyssBorderDark)
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnaherLogo(size = 32.dp, showGlowPulse = false)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "VOICE MODE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = LagoonCyanGlow
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close voice mode",
                        tint = AbyssTextSecondaryDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Animated Cyan Waveform Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AbyssSurfaceDark)
                    .border(1.dp, AbyssBorderDark, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2f
                    val barCount = 36
                    val barSpacing = width / barCount

                    for (i in 0 until barCount) {
                        val x = i * barSpacing + barSpacing / 2f
                        val factor = if (isListening) {
                            (sin(i * 0.4f + wavePhase).toFloat() * 0.45f + 0.55f)
                        } else {
                            0.1f
                        }
                        val barHeight = height * 0.7f * factor
                        val topY = centerY - barHeight / 2f
                        val bottomY = centerY + barHeight / 2f

                        drawLine(
                            color = if (isListening) LagoonCyanGlow else AbyssTextSecondaryDark,
                            start = Offset(x, topY),
                            end = Offset(x, bottomY),
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Live Transcription
            Text(
                text = transcript,
                style = MaterialTheme.typography.bodyLarge,
                color = AbyssTextPrimaryDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Microphone Toggle Button
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(if (isListening) LagoonCyanGlow else AbyssSurfaceVariantDark)
                        .clickable {
                            isListening = !isListening
                            transcript = if (isListening) "Listening to your voice intent..." else "Microphone muted."
                        }
                        .testTag("toggle_mic_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Toggle mic",
                        tint = if (isListening) AbyssCanvasDark else AbyssTextPrimaryDark,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated speech send action
            Button(
                onClick = {
                    onSendTranscript("Build an autonomous cloud event consumer with dead-letter queue verification.")
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AbyssPrimaryContainerDark,
                    contentColor = AbyssOnPrimaryContainerDark
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("send_voice_intent_button")
            ) {
                Text(
                    text = "Submit Voice Intent",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}
