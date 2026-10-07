package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnaherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ANALYTICS & OBSERVABILITY",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = LagoonPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard("Tokens Processed", "482,910", "+14% this week", LagoonPrimary, Modifier.weight(1f))
                    MetricCard("Cumulative Cost", "\$0.86", "Within budget", LagoonOnline, Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard("Avg Latency", "1.12s", "p95 benchmark", LagoonSecondary, Modifier.weight(1f))
                    MetricCard("Mission Success", "99.2%", "0 policy blocks", LagoonTealDark, Modifier.weight(1f))
                }
            }

            // Agent Performance Breakdown
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AGENT TOKEN & TIME ALLOCATION",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        AgentPerformanceBar("S3 Coder (Matrix 4.1)", 0.45f, "45% (217k tok)")
                        Spacer(modifier = Modifier.height(10.dp))
                        AgentPerformanceBar("A4 Planner / Conductor", 0.22f, "22% (106k tok)")
                        Spacer(modifier = Modifier.height(10.dp))
                        AgentPerformanceBar("Q1 Verifier / Critic", 0.18f, "18% (87k tok)")
                        Spacer(modifier = Modifier.height(10.dp))
                        AgentPerformanceBar("A1 Intake / A2 Safety", 0.15f, "15% (72k tok)")
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, LagoonBorderLight),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = LagoonTextSecondaryLight)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = accentColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = LagoonTextSecondaryLight, fontSize = 11.sp)
        }
    }
}

@Composable
fun AgentPerformanceBar(
    agentName: String,
    fraction: Float,
    label: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = agentName, style = MaterialTheme.typography.labelMedium, color = LagoonTextPrimaryLight)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = LagoonTextSecondaryLight)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { fraction },
            color = LagoonPrimary,
            trackColor = LagoonSurfaceSubtleLight,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}
