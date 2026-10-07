package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun ModelRouterScreen(
    viewModel: AnaherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentVersion by viewModel.matrixVersion.collectAsState()
    var localOnlyMode by remember { mutableStateOf(false) }
    var maxBudgetCap by remember { mutableStateOf(0.10f) }
    var routingStrategy by remember { mutableStateOf("Single (Cost-Optimized)") }

    val strategies = listOf(
        "Single (Cost-Optimized)",
        "Race (Lowest Latency)",
        "Ensemble (3-Model Vote)",
        "Debate (Cross-Check Proof)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MODEL ROUTER & BROKER",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Active Version Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ACTIVE MATRIX ENGINE",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentVersion,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = LagoonPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Autonomous multi-agent graph running with official Gemini server-side adapter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LagoonTextSecondaryLight
                        )
                    }
                }
            }

            // Routing Mode Selection
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ROUTING DISPATCH STRATEGY",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        strategies.forEach { strat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = routingStrategy == strat,
                                    onClick = { routingStrategy = strat },
                                    colors = RadioButtonDefaults.colors(selectedColor = LagoonPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = strat,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LagoonTextPrimaryLight
                                )
                            }
                        }
                    }
                }
            }

            // Privacy & Budget Controls
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "SECURITY & BUDGET CONSTRAINTS",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Local-Only Privacy Mode",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = LagoonTextPrimaryLight
                                )
                                Text(
                                    text = "Force offline local synthesis; no external network packets transmitted.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LagoonTextSecondaryLight
                                )
                            }
                            Switch(
                                checked = localOnlyMode,
                                onCheckedChange = { localOnlyMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = LagoonPrimary,
                                    checkedTrackColor = LagoonSurfaceSubtleLight
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Per-Mission Token Budget Cap: \$${"%.2f".format(maxBudgetCap)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = LagoonTextPrimaryLight
                        )
                        Slider(
                            value = maxBudgetCap,
                            onValueChange = { maxBudgetCap = it },
                            valueRange = 0.01f..1.00f,
                            colors = SliderDefaults.colors(
                                thumbColor = LagoonPrimary,
                                activeTrackColor = LagoonPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}
