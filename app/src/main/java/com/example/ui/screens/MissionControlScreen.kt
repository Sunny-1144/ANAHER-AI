package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.AgentMapCanvas
import com.example.ui.components.CodeBlockView
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionControlScreen(
    viewModel: AnaherViewModel,
    missionId: Long,
    onBack: () -> Unit,
    onOpenResult: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val missions by viewModel.missions.collectAsState()
    val mission = missions.find { it.id == missionId } ?: missions.firstOrNull()
    val tasks by if (mission != null) viewModel.getTasksForMission(mission.id).collectAsState(initial = emptyList()) else remember { mutableStateOf(emptyList()) }
    val logs by if (mission != null) viewModel.getLogsForMission(mission.id).collectAsState(initial = emptyList()) else remember { mutableStateOf(emptyList()) }
    val artifacts by if (mission != null) viewModel.getArtifactsForMission(mission.id).collectAsState(initial = emptyList()) else remember { mutableStateOf(emptyList()) }
    val reducedMotion by viewModel.reducedMotion.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Timeline, 1: Agent Map, 2: Logs, 3: Artifacts, 4: Resources
    val tabTitles = listOf("Timeline", "Agent Map", "Logs", "Artifacts", "Resources")

    val stages = listOf(
        MissionStage.UNDERSTANDING,
        MissionStage.PLANNING,
        MissionStage.BUILDING,
        MissionStage.TESTING,
        MissionStage.VERIFYING,
        MissionStage.DELIVERING
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MISSION CONTROL",
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
                actions = {
                    IconButton(onClick = { if (mission != null) onOpenResult(mission.id) }) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "View result",
                            tint = LagoonPrimary
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
                Text(text = "Mission not found", style = MaterialTheme.typography.bodyLarge)
            }
            return@Scaffold
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Mission Header Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, LagoonBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mission.title,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = LagoonTextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = mission.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = LagoonTextSecondaryLight,
                                maxLines = 2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stage Stepper (Horizontal Scroll)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        stages.forEachIndexed { index, st ->
                            val isCompleted = stages.indexOf(mission.stage) > index || mission.stage == MissionStage.COMPLETED
                            val isCurrent = mission.stage == st

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isCompleted -> LagoonOnline
                                                isCurrent -> LagoonCyanGlow
                                                else -> LagoonSurfaceSubtleLight
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${index + 1}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (isCurrent) AbyssCanvasDark else LagoonTextSecondaryLight
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = st.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrent) LagoonPrimary else LagoonTextSecondaryLight
                                    )
                                )
                                if (index < stages.size - 1) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(16.dp)
                                            .height(2.dp)
                                            .background(if (isCompleted) LagoonOnline else LagoonBorderLight)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Bar & Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${mission.progressPercent}% • ${mission.statusMessage}",
                            style = MaterialTheme.typography.labelSmall,
                            color = LagoonTextSecondaryLight,
                            modifier = Modifier.weight(1f)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = LagoonSurfaceSubtleLight,
                                border = BorderStroke(1.dp, LagoonBorderLight),
                                onClick = { /* Pause */ }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Pause, contentDescription = "Pause", modifier = Modifier.size(14.dp), tint = LagoonTealDark)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Pause", style = MaterialTheme.typography.labelSmall, color = LagoonTealDark)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { mission.progressPercent / 100f },
                        color = LagoonPrimary,
                        trackColor = LagoonSurfaceSubtleLight,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tabs Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = LagoonSurfaceSubtleLight,
                contentColor = LagoonPrimary,
                edgePadding = 0.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LagoonBorderLight, RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        modifier = Modifier.testTag("mission_tab_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content
            when (selectedTab) {
                0 -> { // Timeline
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(tasks) { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LagoonBorderLight, RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (task.status) {
                                                TaskStatus.COMPLETED -> LagoonOnline
                                                TaskStatus.RUNNING -> LagoonCyanGlow
                                                TaskStatus.FAILED -> LagoonError
                                                else -> LagoonBorderLight
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = LagoonTextPrimaryLight
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Agent: ${task.assignedAgent} • ${task.durationMs}ms",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LagoonTextSecondaryLight
                                    )
                                    if (task.outputSummary.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = task.outputSummary,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = LagoonTealDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> { // Agent Map
                    Column(modifier = Modifier.fillMaxSize()) {
                        AgentMapCanvas(
                            activeAgentId = "S3",
                            reducedMotion = reducedMotion
                        )
                    }
                }
                2 -> { // Logs
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(logs) { log ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LagoonBorderLight, RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "[${log.level.name}]",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (log.level == LogLevel.ERROR) LagoonErrorTextLight else LagoonPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${log.agentName}: ${log.message}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LagoonTextPrimaryLight
                                )
                            }
                        }
                    }
                }
                3 -> { // Artifacts
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(artifacts) { art ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, LagoonBorderLight),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = art.name,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = LagoonTextPrimaryLight
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    if (art.type == ArtifactType.CODE) {
                                        CodeBlockView(code = art.content, language = "go")
                                    } else {
                                        Text(
                                            text = art.content,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = LagoonTextSecondaryLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                4 -> { // Resources
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, LagoonBorderLight),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Resource Telemetry & Cost",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = LagoonTextPrimaryLight
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "• Model Engine: ${mission.modelVersion}\n" +
                                                "• Tokens Consumed: ${mission.tokensUsed} tokens\n" +
                                                "• Estimated Run Cost: $${mission.costEst}\n" +
                                                "• Sandbox CPU Allocation: 2 vCPU (Sandbox limits enforced)\n" +
                                                "• Permissions Granted: Local storage sandbox, no external network\n" +
                                                "• Circuit Breaker Budget Cap: $0.10 max",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = LagoonTextSecondaryLight,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
