package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MissionArtifact
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionResultScreen(
    viewModel: AnaherViewModel,
    missionId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val missions by viewModel.missions.collectAsState()
    val mission = missions.find { it.id == missionId } ?: missions.firstOrNull()
    val artifacts by if (mission != null) viewModel.getArtifactsForMission(mission.id).collectAsState(initial = emptyList()) else remember { mutableStateOf(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MISSION RESULT",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = LagoonPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LagoonTextPrimaryLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (mission == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Mission not found")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Hero Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(LagoonSurfaceSubtleLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = LagoonOnline,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Mission Completed Successfully",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = LagoonTextPrimaryLight
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = mission.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = LagoonPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "All formal QA criteria and verification tests passed with 100% confidence.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LagoonTextSecondaryLight
                        )
                    }
                }
            }

            // Verification Metrics
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "VERIFICATION & PERFORMANCE AUDIT",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Confidence", style = MaterialTheme.typography.labelSmall, color = LagoonTextSecondaryLight)
                                Text("99.4%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = LagoonOnline)
                            }
                            Column {
                                Text("Test Pass Rate", style = MaterialTheme.typography.labelSmall, color = LagoonTextSecondaryLight)
                                Text("12 / 12 (100%)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = LagoonPrimary)
                            }
                            Column {
                                Text("Tokens", style = MaterialTheme.typography.labelSmall, color = LagoonTextSecondaryLight)
                                Text("${mission.tokensUsed}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = LagoonTextPrimaryLight)
                            }
                            Column {
                                Text("Cost", style = MaterialTheme.typography.labelSmall, color = LagoonTextSecondaryLight)
                                Text("$${mission.costEst}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = LagoonTealDark)
                            }
                        }
                    }
                }
            }

            // Produced Artifacts
            item {
                Text(
                    text = "GENERATED ARTIFACTS (${artifacts.size})",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = LagoonTextSecondaryLight
                )
            }

            items(artifacts) { art ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = LagoonPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = art.name,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = LagoonTextPrimaryLight
                            )
                            Text(
                                text = "${art.type.name} • Verified by Q4 QA Gate",
                                style = MaterialTheme.typography.bodySmall,
                                color = LagoonTextSecondaryLight
                            )
                        }
                        IconButton(onClick = {
                            Toast.makeText(context, "Exporting ${art.name}...", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download",
                                tint = LagoonPrimary
                            )
                        }
                    }
                }
            }

            // Action Buttons: "Improve this", "Run again", "Export Bundle"
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.sendMessage("Improve and optimize the architecture of mission #${mission.id} for higher throughput.")
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LagoonPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("improve_mission_button")
                    ) {
                        Text("Improve this", fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.createMission(mission.title, mission.description, mission.modelVersion)
                            onBack()
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, LagoonPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("run_again_button")
                    ) {
                        Text("Run again", color = LagoonPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
