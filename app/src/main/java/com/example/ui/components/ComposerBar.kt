package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ComposerBar(
    promptText: String,
    onPromptChange: (String) -> Unit,
    onSend: () -> Unit,
    onOpenVoice: () -> Unit,
    selectedVersion: String,
    onSelectVersion: (String) -> Unit,
    selectedMode: String,
    onSelectMode: (String) -> Unit,
    isRunning: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showAttachMenu by remember { mutableStateOf(false) }
    var showVersionMenu by remember { mutableStateOf(false) }

    val versions = listOf(
        "Matrix Auto" to "Conductor dynamically selects optimal agents",
        "Matrix 4.0 Core" to "General reasoning, synthesis & prompt engine",
        "Matrix 4.1 Forge" to "Full-stack code synthesis, sandbox & tests",
        "Matrix 4.2 Logic" to "Formal mathematics & step-by-step proofs",
        "Matrix 4.3 Cosmos" to "Physics, chemistry & scientific modeling",
        "Matrix 4.4 Genesis" to "Multi-model consensus & autonomous agents"
    )

    val modes = listOf("Answer quickly", "Think deeply", "Run as Mission")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Model & Mode Pills Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Version Pill
            Box {
                Surface(
                    onClick = { showVersionMenu = true },
                    shape = RoundedCornerShape(100.dp),
                    color = LagoonSurfaceSubtleLight,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.testTag("matrix_version_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(LagoonCyanGlow)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedVersion,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = LagoonTealDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select version",
                            tint = LagoonTealDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showVersionMenu,
                    onDismissRequest = { showVersionMenu = false }
                ) {
                    Text(
                        text = "SELECT MATRIX ENGINE VERSION",
                        style = MaterialTheme.typography.labelSmall,
                        color = LagoonTextSecondaryLight,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                    HorizontalDivider()
                    versions.forEach { (ver, desc) ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = ver,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (ver == selectedVersion) FontWeight.Bold else FontWeight.Medium,
                                            color = if (ver == selectedVersion) LagoonPrimary else LagoonTextPrimaryLight
                                        )
                                    )
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LagoonTextSecondaryLight,
                                        fontSize = 11.sp
                                    )
                                }
                            },
                            onClick = {
                                onSelectVersion(ver)
                                showVersionMenu = false
                            }
                        )
                    }
                }
            }

            // Mode Selector (Answer quickly / Think deeply / Run as Mission)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(LagoonSurfaceSubtleLight)
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                modes.forEach { mode ->
                    val isSelected = mode == selectedMode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isSelected) LagoonPrimary else Color.Transparent)
                            .clickable { onSelectMode(mode) }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = when (mode) {
                                "Answer quickly" -> "Quick"
                                "Think deeply" -> "Deep"
                                else -> "Mission"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else LagoonTextSecondaryLight
                            ),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Main Composer Card (Rounded-24, 1px border, shadow, faint cyan glow)
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, LagoonBorderLight),
            shadowElevation = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = LagoonCyanGlow.copy(alpha = 0.2f),
                    spotColor = LagoonCyanGlow.copy(alpha = 0.2f)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Attach (+) button with menu
                Box {
                    IconButton(
                        onClick = { showAttachMenu = true },
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("attach_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Attach file",
                            tint = LagoonTextSecondaryLight,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showAttachMenu,
                        onDismissRequest = { showAttachMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Upload File") },
                            leadingIcon = { Icon(Icons.Default.AttachFile, contentDescription = null) },
                            onClick = { showAttachMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Take Photo") },
                            leadingIcon = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
                            onClick = { showAttachMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Google Drive") },
                            leadingIcon = { Icon(Icons.Default.CloudQueue, contentDescription = null) },
                            onClick = { showAttachMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Paste Web Link") },
                            leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                            onClick = { showAttachMenu = false }
                        )
                    }
                }

                // Auto-growing text input
                TextField(
                    value = promptText,
                    onValueChange = onPromptChange,
                    placeholder = {
                        Text(
                            text = "What do you want ANAHER to do?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LagoonTextSecondaryLight
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    maxLines = 5,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("composer_text_input")
                )

                // Voice Mode Button
                IconButton(
                    onClick = onOpenVoice,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("voice_mode_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = "Voice mode",
                        tint = LagoonTextSecondaryLight,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Send / Stop Button
                IconButton(
                    onClick = onSend,
                    enabled = promptText.isNotBlank() || isRunning,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (isRunning) LagoonWarning else LagoonPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(bottom = 2.dp, end = 2.dp)
                        .size(38.dp)
                        .testTag("send_button")
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Stop else Icons.AutoMirrored.Filled.Send,
                        contentDescription = if (isRunning) "Stop" else "Send",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
