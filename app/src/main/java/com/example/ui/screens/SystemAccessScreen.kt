package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

data class DevicePermissionStatus(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    var isEnabled: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemAccessScreen(
    viewModel: AnaherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val permissions = remember {
        mutableStateListOf(
            DevicePermissionStatus("fs", "Local Filesystem Sandbox", "Allow Matrix engine to store generated code and logs in app sandbox.", Icons.Default.FolderOpen, true),
            DevicePermissionStatus("net", "Secure Network Telemetry", "Connect to official Gemini API endpoint for live model streaming.", Icons.Default.Language, true),
            DevicePermissionStatus("mic", "Microphone / Voice Mode", "Audio waveform streaming for voice intent recognition.", Icons.Default.Mic, true),
            DevicePermissionStatus("clip", "Clipboard Access", "Copy code snippets and verified deliverables in one tap.", Icons.Default.ContentPaste, true),
            DevicePermissionStatus("shell", "Containerized Code Runner", "Execute synthesized Kotlin and Go modules inside isolated runtime.", Icons.Default.Terminal, true)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SYSTEM ACCESS & DEVICE CENTER",
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
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LagoonSurfaceSubtleLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = LagoonPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Zero-Trust Agent Sandbox",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = LagoonTealDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A2 Safety and Policy agent checks every tool invocation before execution. You can grant or revoke autonomous permissions below.",
                            style = MaterialTheme.typography.bodySmall,
                            color = LagoonTextSecondaryLight
                        )
                    }
                }
            }

            items(permissions) { perm ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LagoonSurfaceSubtleLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = perm.icon,
                                contentDescription = null,
                                tint = LagoonPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = perm.name,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = LagoonTextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = perm.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = LagoonTextSecondaryLight
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = perm.isEnabled,
                            onCheckedChange = { checked ->
                                val idx = permissions.indexOf(perm)
                                if (idx != -1) {
                                    permissions[idx] = perm.copy(isEnabled = checked)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = LagoonPrimary,
                                checkedTrackColor = LagoonSurfaceSubtleLight
                            )
                        )
                    }
                }
            }
        }
    }
}
