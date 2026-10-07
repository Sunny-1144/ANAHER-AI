package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningModeScreen(
    viewModel: AnaherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTopic by remember { mutableStateOf("Distributed Consensus (Raft)") }
    val topics = listOf(
        "Distributed Consensus (Raft)",
        "Calculus: Integration by Parts",
        "Quantum Computing: Superposition",
        "Clean Architecture in Mobile"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "LEARNING MODE",
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
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LagoonSurfaceSubtleLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Pedagogical Decomposition Engine",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = LagoonTealDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Matrix 4.2 Logic breaks down tough STEM concepts into: 1) Core Concept, 2) Step-by-Step Method, 3) Common Pitfalls, 4) Practice Verification.",
                            style = MaterialTheme.typography.bodySmall,
                            color = LagoonTextSecondaryLight
                        )
                    }
                }
            }

            // 1. Concept Section
            item {
                LearningSectionCard(
                    title = "1. CORE CONCEPT",
                    icon = Icons.Default.Lightbulb,
                    content = "Raft is a consensus algorithm designed for state machine replication across a cluster. It ensures that distributed nodes agree on a series of inputs and execute them in the identical sequence, maintaining safety under network partitions."
                )
            }

            // 2. Step-by-Step Method
            item {
                LearningSectionCard(
                    title = "2. STEP-BY-STEP METHOD",
                    icon = Icons.Default.Timeline,
                    content = "1. Leader Election: A candidate initiates a vote with randomized election timeouts.\n" +
                            "2. Log Replication: The elected leader receives client writes and broadcasts AppendEntries.\n" +
                            "3. Safety Invariant: Once an entry is committed by a majority of nodes, it is permanent."
                )
            }

            // 3. Common Pitfalls & Mistakes
            item {
                LearningSectionCard(
                    title = "3. COMMON PITFALLS & MISTAKES",
                    icon = Icons.Default.WarningAmber,
                    content = "• Split Brain: Failing to require a strict majority (N/2 + 1) during election.\n" +
                            "• Unbounded Timers: Identical election timeouts causing repeated tie votes.\n" +
                            "• Overwriting Committed Logs: Leaders must never overwrite entries already committed."
                )
            }

            // 4. Interactive Practice Question
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "4. PRACTICE VERIFICATION",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "In a 5-node Raft cluster, what is the minimum number of functional nodes required to maintain live consensus and accept writes?",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = LagoonTextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        var selectedOption by remember { mutableStateOf<Int?>(null) }
                        val options = listOf("2 nodes", "3 nodes (Majority)", "4 nodes", "5 nodes")

                        options.forEachIndexed { idx, opt ->
                            Surface(
                                onClick = { selectedOption = idx },
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedOption == idx) (if (idx == 1) LagoonSurfaceSubtleLight else LagoonSurfaceVariantLight) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, if (selectedOption == idx) LagoonPrimary else LagoonBorderLight),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = opt,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (selectedOption == idx && idx == 1) LagoonTealDark else LagoonTextPrimaryLight
                                    )
                                    if (selectedOption == idx) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(
                                            imageVector = if (idx == 1) Icons.Default.CheckCircle else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (idx == 1) LagoonOnline else LagoonErrorTextLight,
                                            modifier = Modifier.size(18.dp)
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
}

@Composable
fun LearningSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, LagoonBorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = LagoonPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.Bold),
                    color = LagoonPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = LagoonTextPrimaryLight,
                lineHeight = 22.sp
            )
        }
    }
}
