package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskStatus
import com.example.engine.AgentExecutionStep
import com.example.ui.theme.*

@Composable
fun AgentStepList(
    steps: List<AgentExecutionStep>,
    isLiveStreaming: Boolean = false,
    statusMessage: String = "",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Calm status line & expand chevron
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .padding(vertical = 4.dp, horizontal = 6.dp)
                .testTag("show_work_toggle"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(LagoonAgentPurple)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isLiveStreaming && statusMessage.isNotBlank()) statusMessage else if (steps.isNotEmpty()) "Autonomous pipeline: ${steps.size} steps completed" else "Show work",
                style = MaterialTheme.typography.labelMedium,
                color = LagoonTextSecondaryLight,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Hide work" else "Show work",
                tint = LagoonTextSecondaryLight,
                modifier = Modifier.size(18.dp)
            )
        }

        // Slim cyan shimmer bar when live streaming
        if (isLiveStreaming) {
            val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
            val shimmerX by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "shimmer_x"
            )
            val shimmerBrush = Brush.horizontalGradient(
                colors = listOf(
                    LagoonCyanGlow.copy(alpha = 0.2f),
                    LagoonCyanGlow,
                    LagoonPrimaryLight,
                    LagoonCyanGlow.copy(alpha = 0.2f)
                ),
                startX = shimmerX - 300f,
                endX = shimmerX
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(shimmerBrush)
            )
        }

        // Expanded step list
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .border(1.dp, LagoonBorderLight, RoundedCornerShape(12.dp))
                    .background(LagoonSurfaceVariantLight.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                steps.forEach { step ->
                    AgentStepRow(step = step)
                }
            }
        }
    }
}

@Composable
fun AgentStepRow(step: AgentExecutionStep) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(LagoonAgentPurple)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${step.agentCode} • ${step.agentName}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = LagoonTextPrimaryLight
                )
                Text(
                    text = "${step.durationMs}ms • ${step.tokenCount} tok",
                    style = MaterialTheme.typography.labelSmall,
                    color = LagoonTextTertiaryLight
                )
            }
            if (step.details.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = step.details,
                    style = MaterialTheme.typography.bodySmall,
                    color = LagoonTextSecondaryLight
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = if (step.status == TaskStatus.COMPLETED) Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
            contentDescription = step.status.name,
            tint = if (step.status == TaskStatus.COMPLETED) LagoonOnline else LagoonWarning,
            modifier = Modifier.size(16.dp)
        )
    }
}
