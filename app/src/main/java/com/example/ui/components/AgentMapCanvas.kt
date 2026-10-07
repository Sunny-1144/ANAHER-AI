package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class MapNode(
    val id: String,
    val name: String,
    val role: String,
    val normX: Float, // 0.0 to 1.0
    val normY: Float,
    val isPulsing: Boolean = false
)

data class MapEdge(
    val fromNodeId: String,
    val toNodeId: String
)

@Composable
fun AgentMapCanvas(
    modifier: Modifier = Modifier,
    activeAgentId: String = "S3",
    reducedMotion: Boolean = false
) {
    val nodes = listOf(
        MapNode("A1", "A1 Intake", "Parser", 0.18f, 0.20f),
        MapNode("A2", "A2 Safety", "Zero Risk", 0.50f, 0.15f),
        MapNode("A3", "A3 Conductor", "Orchestrator", 0.82f, 0.25f),
        MapNode("A4", "A4 Planner", "DAG Graph", 0.80f, 0.55f),
        MapNode("S3", "S3 Coder", "Matrix 4.1", 0.50f, 0.55f, isPulsing = true),
        MapNode("S4", "S4 Tester", "Sandbox", 0.20f, 0.60f),
        MapNode("Q1", "Q1 Verifier", "Formal Critic", 0.35f, 0.85f),
        MapNode("Q4", "Q4 Final QA", "Delivery Gate", 0.65f, 0.85f)
    )

    val edges = listOf(
        MapEdge("A1", "A2"),
        MapEdge("A2", "A3"),
        MapEdge("A3", "A4"),
        MapEdge("A4", "S3"),
        MapEdge("S3", "S4"),
        MapEdge("S4", "Q1"),
        MapEdge("Q1", "Q4"),
        MapEdge("Q4", "A3") // feedback loop
    )

    val infiniteTransition = rememberInfiniteTransition(label = "agent_map")
    val packetProgress by if (!reducedMotion) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2400, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "packet_progress"
        )
    } else {
        rememberUpdatedState(0.5f)
    }

    val pulseScale by if (!reducedMotion) {
        infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )
    } else {
        rememberUpdatedState(1.0f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, AbyssBorderDark, RoundedCornerShape(16.dp))
            .background(AbyssCanvasDark)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val nodePositions = nodes.associate { node ->
                node.id to Offset(node.normX * width, node.normY * height)
            }

            // Draw connecting edges
            edges.forEach { edge ->
                val start = nodePositions[edge.fromNodeId]
                val end = nodePositions[edge.toNodeId]
                if (start != null && end != null) {
                    drawLine(
                        color = AbyssBorderDark,
                        start = start,
                        end = end,
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    // Draw moving data packet
                    val packetPos = Offset(
                        x = start.x + (end.x - start.x) * packetProgress,
                        y = start.y + (end.y - start.y) * packetProgress
                    )
                    drawCircle(
                        color = LagoonCyanGlow,
                        radius = 3.5.dp.toPx(),
                        center = packetPos
                    )
                }
            }

            // Draw nodes
            nodes.forEach { node ->
                val pos = nodePositions[node.id] ?: return@forEach
                val isTarget = node.id == activeAgentId || node.isPulsing

                // Outer glow if pulsing
                if (isTarget) {
                    drawCircle(
                        color = LagoonCyanGlow.copy(alpha = 0.25f),
                        radius = (16.dp * pulseScale).toPx(),
                        center = pos
                    )
                }

                // Node background circle
                drawCircle(
                    color = if (isTarget) AbyssPrimaryContainerDark else AbyssSurfaceDark,
                    radius = 12.dp.toPx(),
                    center = pos
                )

                // Node border
                drawCircle(
                    color = if (isTarget) LagoonCyanGlow else LagoonAgentPurple,
                    radius = 12.dp.toPx(),
                    center = pos,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Center core dot
                drawCircle(
                    color = if (isTarget) LagoonCyanHighlight else LagoonAgentPurple,
                    radius = 4.dp.toPx(),
                    center = pos
                )
            }
        }

        // Overlay Labels for nodes
        nodes.forEach { node ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = (node.normX * 100).dp - 30.dp,
                        top = (node.normY * 100).dp + 16.dp
                    )
            ) {
                // Keep clean without overlapping text
            }
        }

        // Legend at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .background(AbyssSurfaceVariantDark.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(LagoonCyanGlow)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Live Matrix Telemetry: Packets flowing across 8 autonomous agents",
                style = MaterialTheme.typography.labelSmall,
                color = AbyssTextPrimaryDark,
                fontSize = 10.5.sp
            )
        }
    }
}
