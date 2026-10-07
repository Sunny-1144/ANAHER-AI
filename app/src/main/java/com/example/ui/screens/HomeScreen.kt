package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Mission
import com.example.data.model.MissionStage
import com.example.ui.components.AnaherLogo
import com.example.ui.components.ComposerBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

data class SuggestionChipItem(
    val title: String,
    val prompt: String,
    val icon: ImageVector
)

@Composable
fun HomeScreen(
    viewModel: AnaherViewModel,
    onNavigateToChat: () -> Unit,
    onNavigateToMissions: () -> Unit,
    onSelectMission: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var promptInput by remember { mutableStateOf("") }
    val selectedVersion by viewModel.matrixVersion.collectAsState()
    val selectedMode by viewModel.matrixMode.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val missions by viewModel.missions.collectAsState()
    val reducedMotion by viewModel.reducedMotion.collectAsState()

    val suggestions = listOf(
        SuggestionChipItem("Build an app", "Build an autonomous event orchestration module in Kotlin with Clean Architecture and StateFlow.", Icons.Default.Apps),
        SuggestionChipItem("Solve a problem", "Derive the mathematical proof for distributed consensus under 33% Byzantine failure.", Icons.Default.Calculate),
        SuggestionChipItem("Write a prompt", "Construct a production system prompt for a multi-agent code reviewer.", Icons.Default.Edit),
        SuggestionChipItem("Research a topic", "Compare Raft vs Paxos algorithms for high-throughput in-memory state machines.", Icons.Default.Search),
        SuggestionChipItem("Plan a project", "Generate a sprint task breakdown DAG for launching a multi-tenant cloud service.", Icons.Default.AccountTree),
        SuggestionChipItem("Analyze a file", "Review system architecture constraints and detect bottleneck vulnerabilities.", Icons.Default.Description)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Brand & Greeting
            item {
                Spacer(modifier = Modifier.height(18.dp))
                AnaherLogo(
                    size = 72.dp,
                    showGlowPulse = true,
                    reducedMotion = reducedMotion
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Good evening, Bhargav.",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = LagoonTextPrimaryLight
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "What do you want ANAHER to do?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LagoonTextSecondaryLight
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Suggestion Chips (Horizontal Scroll)
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "SUGGESTIONS",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = LagoonTextSecondaryLight,
                        modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(suggestions) { item ->
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = LagoonSurfaceSubtleLight,
                                border = BorderStroke(1.dp, LagoonBorderLight),
                                onClick = {
                                    promptInput = item.prompt
                                },
                                modifier = Modifier.testTag("suggestion_${item.title.lowercase().replace(" ", "_")}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = LagoonPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = LagoonTealDark,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Active Missions / Quick Status
            if (missions.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACTIVE MISSIONS",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Text(
                            text = "View all",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = LagoonPrimary,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .clickable { onNavigateToMissions() }
                                .padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(missions.take(2)) { mission ->
                    MissionQuickCard(
                        mission = mission,
                        onClick = { onSelectMission(mission.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // Pinned Composer Bar at bottom
        ComposerBar(
            promptText = promptInput,
            onPromptChange = { promptInput = it },
            onSend = {
                if (promptInput.isNotBlank()) {
                    val text = promptInput
                    promptInput = ""
                    viewModel.sendMessage(text)
                    onNavigateToChat()
                }
            },
            onOpenVoice = { viewModel.setVoiceModeOpen(true) },
            selectedVersion = selectedVersion,
            onSelectVersion = { viewModel.setMatrixVersion(it) },
            selectedMode = selectedMode,
            onSelectMode = { viewModel.setMatrixMode(it) },
            isRunning = isSending
        )
    }
}

@Composable
fun MissionQuickCard(
    mission: Mission,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, LagoonBorderLight),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mission_quick_card_${mission.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        when (mission.stage) {
                            MissionStage.COMPLETED -> LagoonOnline
                            MissionStage.FAILED -> LagoonError
                            MissionStage.PAUSED -> LagoonWarning
                            else -> LagoonCyanGlow
                        }
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = LagoonTextPrimaryLight,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${mission.stage.name} • ${mission.progressPercent}% • ${mission.modelVersion}",
                    style = MaterialTheme.typography.labelSmall,
                    color = LagoonTextSecondaryLight
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open mission",
                tint = LagoonTextSecondaryLight,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
