package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArtifactType
import com.example.data.model.MissionArtifact
import com.example.data.model.MissionTask
import com.example.data.model.TaskStatus
import com.example.ui.components.AgentMapCanvas
import com.example.ui.components.CodeBlockView
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceSheet(
    viewModel: AnaherViewModel,
    isOpen: Boolean,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val currentTab by viewModel.workspaceTab.collectAsState()
    val activeArtifact by viewModel.activeArtifact.collectAsState()
    val allArtifacts by viewModel.allArtifacts.collectAsState()
    val missions by viewModel.missions.collectAsState()
    val reducedMotion by viewModel.reducedMotion.collectAsState()

    val tabTitles = listOf("Artifacts", "Live Timeline", "Agent Map")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle(color = LagoonBorderLight) },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(LagoonCyanGlow)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WORKSPACE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = LagoonPrimary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_workspace_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Workspace",
                        tint = LagoonTextSecondaryLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Tabs
            TabRow(
                selectedTabIndex = currentTab,
                containerColor = LagoonSurfaceSubtleLight,
                contentColor = LagoonPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LagoonBorderLight, RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { viewModel.setWorkspaceTab(index) },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (currentTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        modifier = Modifier.testTag("workspace_tab_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Content
            when (currentTab) {
                0 -> ArtifactsTabContent(
                    activeArtifact = activeArtifact,
                    allArtifacts = allArtifacts,
                    onSelectArtifact = { viewModel.openArtifact(it) }
                )
                1 -> LiveTimelineTabContent(
                    missions = missions
                )
                2 -> AgentMapTabContent(
                    reducedMotion = reducedMotion
                )
            }
        }
    }
}

@Composable
fun ArtifactsTabContent(
    activeArtifact: MissionArtifact?,
    allArtifacts: List<MissionArtifact>,
    onSelectArtifact: (MissionArtifact) -> Unit
) {
    val artifactToDisplay = activeArtifact ?: allArtifacts.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (artifactToDisplay != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = artifactToDisplay.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = LagoonTextPrimaryLight
                            )
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = LagoonSurfaceSubtleLight
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = LagoonOnline,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "VERIFIED",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LagoonTealDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (artifactToDisplay.type == ArtifactType.CODE) {
                            CodeBlockView(
                                code = artifactToDisplay.content,
                                language = if (artifactToDisplay.name.endsWith(".go")) "go" else "kotlin"
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = LagoonSurfaceVariantLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = artifactToDisplay.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LagoonTextPrimaryLight,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "ALL ARTIFACTS (${allArtifacts.size})",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                color = LagoonTextSecondaryLight,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
            )
        }

        items(allArtifacts) { item ->
            Surface(
                onClick = { onSelectArtifact(item) },
                shape = RoundedCornerShape(12.dp),
                color = if (item == artifactToDisplay) LagoonSurfaceSubtleLight else MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, LagoonBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (item.type == ArtifactType.CODE) Icons.Default.Code else Icons.Default.Description,
                        contentDescription = null,
                        tint = LagoonPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = LagoonTextPrimaryLight
                        )
                        Text(
                            text = "${item.type.name} • 100% Verified by Q4 QA",
                            style = MaterialTheme.typography.bodySmall,
                            color = LagoonTextSecondaryLight
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = LagoonTextSecondaryLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LiveTimelineTabContent(
    missions: List<com.example.data.model.Mission>
) {
    val activeMission = missions.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (activeMission != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = activeMission.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = LagoonTextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Stage: ${activeMission.stage.name} (${activeMission.progressPercent}%)",
                            style = MaterialTheme.typography.labelMedium,
                            color = LagoonPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { activeMission.progressPercent / 100f },
                            color = LagoonPrimary,
                            trackColor = LagoonSurfaceSubtleLight,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "EXECUTION PIPELINE MILESTONES",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                color = LagoonTextSecondaryLight,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        val dummyMilestones = listOf(
            Triple("A1 Intake / A2 Safety", "Requirements decomposed with zero risk flags", TaskStatus.COMPLETED),
            Triple("A4 Planner DAG", "Formulated 4 parallel task dependencies", TaskStatus.COMPLETED),
            Triple("S3 Coder / S4 Tester", "Synthesized engine and verified 12/12 unit tests", TaskStatus.COMPLETED),
            Triple("Q1 Verifier / Q4 Final QA", "Benchmarking throughput and generating artifacts", TaskStatus.RUNNING)
        )

        items(dummyMilestones) { (agent, desc, status) ->
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
                        .background(if (status == TaskStatus.COMPLETED) LagoonOnline else LagoonCyanGlow)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = agent,
                        style = MaterialTheme.typography.labelLarge,
                        color = LagoonTextPrimaryLight
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = LagoonTextSecondaryLight
                    )
                }
            }
        }
    }
}

@Composable
fun AgentMapTabContent(
    reducedMotion: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "LIVE MULTI-AGENT EXECUTION GRAPH",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
            color = LagoonTextSecondaryLight,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        AgentMapCanvas(
            activeAgentId = "S3",
            reducedMotion = reducedMotion
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, LagoonBorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "MATRIX Protocol Invariants",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = LagoonTextPrimaryLight
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• BlackBoard typed JSON state with task_id, constraints, confidence\n" +
                            "• Zero-trust safety sandbox: all tools validated before invocation\n" +
                            "• Consensus judge: 3-model cross-evaluation on mathematical & scientific proofs\n" +
                            "• Self-improvement loop: failures automatically converted into regression tests",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LagoonTextSecondaryLight,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
