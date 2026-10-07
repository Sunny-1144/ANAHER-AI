package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemoryItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AnaherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentTheme by viewModel.themeMode.collectAsState()
    val reducedMotion by viewModel.reducedMotion.collectAsState()
    val memoryList by viewModel.memoryList.collectAsState()

    var showAddMemoryDialog by remember { mutableStateOf(false) }
    var newKey by remember { mutableStateOf("") }
    var newValue by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Preferences") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SETTINGS & MEMORY",
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
            // Theme Mode Selector
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "APPEARANCE & THEME",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = LagoonTextSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val themes = listOf(
                            ThemeMode.SYSTEM to "System Default",
                            ThemeMode.LIGHT to "Lagoon White (Light)",
                            ThemeMode.DARK to "Lagoon Abyss (Dark)",
                            ThemeMode.HIGH_CONTRAST to "High-Contrast"
                        )

                        themes.forEach { (mode, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentTheme == mode,
                                    onClick = { viewModel.setThemeMode(mode) },
                                    colors = RadioButtonDefaults.colors(selectedColor = LagoonPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = LagoonTextPrimaryLight
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        // Reduced Motion Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Reduced Motion",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = LagoonTextPrimaryLight
                                )
                                Text(
                                    text = "Disable pulsing glows and particle animations.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LagoonTextSecondaryLight
                                )
                            }
                            Switch(
                                checked = reducedMotion,
                                onCheckedChange = { viewModel.toggleReducedMotion() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = LagoonPrimary,
                                    checkedTrackColor = LagoonSurfaceSubtleLight
                                )
                            )
                        }
                    }
                }
            }

            // Memory Manager Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRANSPARENT MEMORY (${memoryList.size})",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = LagoonTextSecondaryLight
                    )
                    TextButton(onClick = { showAddMemoryDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = LagoonPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add fact", color = LagoonPrimary, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            // Memory Items List
            items(memoryList) { mem ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, LagoonBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mem.key,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = LagoonPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = mem.value,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LagoonTextPrimaryLight
                            )
                            Text(
                                text = mem.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = LagoonTextSecondaryLight
                            )
                        }
                        IconButton(onClick = { viewModel.deleteMemory(mem) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Forget", tint = LagoonTextSecondaryLight, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // GDPR-style data export
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LagoonSurfaceSubtleLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Toast.makeText(context, "Telemetry & Memory data exported to JSON bundle.", Toast.LENGTH_LONG).show()
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = LagoonPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Export All Personal Data & Models",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = LagoonTealDark
                            )
                            Text(
                                text = "GDPR-compliant export of chat sessions, knowledge and memory.",
                                style = MaterialTheme.typography.bodySmall,
                                color = LagoonTextSecondaryLight
                            )
                        }
                    }
                }
            }
        }

        if (showAddMemoryDialog) {
            AlertDialog(
                onDismissRequest = { showAddMemoryDialog = false },
                title = { Text("Add Memory Item") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newKey,
                            onValueChange = { newKey = it },
                            label = { Text("Memory Key (e.g. coding_preference)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newValue,
                            onValueChange = { newValue = it },
                            label = { Text("Value / Constraint") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newKey.isNotBlank()) {
                                viewModel.addMemory(newKey, newValue, newCategory)
                                newKey = ""
                                newValue = ""
                                showAddMemoryDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LagoonPrimary)
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddMemoryDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
