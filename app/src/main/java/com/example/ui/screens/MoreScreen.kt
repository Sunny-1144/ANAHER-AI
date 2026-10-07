package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel
import com.example.ui.viewmodel.SubScreen

data class MoreNavigationItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val targetSubScreen: SubScreen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    viewModel: AnaherViewModel,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        MoreNavigationItem("Knowledge Vault", "Manage indexed documents and domain sources", Icons.AutoMirrored.Filled.MenuBook, SubScreen.KNOWLEDGE_VAULT),
        MoreNavigationItem("Learning Mode", "Step-by-step STEM breakdown & practice verification", Icons.Default.School, SubScreen.LEARNING_MODE),
        MoreNavigationItem("System Access & Device Center", "Hardware sandboxing, permissions & zero-risk controls", Icons.Default.Security, SubScreen.SYSTEM_ACCESS),
        MoreNavigationItem("Model Router & Broker", "MATRIX engines, fallback chain & budget caps", Icons.AutoMirrored.Filled.AltRoute, SubScreen.MODEL_ROUTER),
        MoreNavigationItem("Analytics & Telemetry", "Agent performance, tokens, and cost breakdown", Icons.Default.BarChart, SubScreen.ANALYTICS),
        MoreNavigationItem("Settings & Memory", "Themes, reduced motion & transparent memory manager", Icons.Default.Settings, SubScreen.SETTINGS)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "STUDIO UTILITIES",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = LagoonPrimary
                    )
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "ADVANCED CONTROLS & MODULES",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = LagoonTextSecondaryLight
                )
            }

            items(items) { item ->
                Surface(
                    onClick = { viewModel.openSubScreen(item.targetSubScreen) },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth().testTag("more_item_${item.title.lowercase().replace(" ", "_")}")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LagoonSurfaceSubtleLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = LagoonPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = LagoonTextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = LagoonTextSecondaryLight
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = LagoonTextSecondaryLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
