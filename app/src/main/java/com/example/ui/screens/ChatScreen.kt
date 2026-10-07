package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewSidebar
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
import com.example.data.model.ChatMessage
import com.example.data.model.MessageRole
import com.example.data.model.MessageStatus
import com.example.engine.MatrixEngine
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AnaherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: AnaherViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val statusMessage by viewModel.chatStatusMessage.collectAsState()
    val selectedVersion by viewModel.matrixVersion.collectAsState()
    val selectedMode by viewModel.matrixMode.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var inputContent by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnaherLogo(size = 32.dp, showGlowPulse = false)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ANAHER",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = LagoonPrimary
                            )
                            Text(
                                text = "$selectedVersion • Autonomous",
                                style = MaterialTheme.typography.labelSmall,
                                color = LagoonTextSecondaryLight
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.setWorkspaceOpen(true, 0) },
                        modifier = Modifier.testTag("open_workspace_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ViewSidebar,
                            contentDescription = "Open Workspace",
                            tint = LagoonPrimary
                        )
                    }
                    IconButton(
                        onClick = { viewModel.clearChat() },
                        modifier = Modifier.testTag("clear_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear chat",
                            tint = LagoonTextSecondaryLight
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Conversation Column
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AnaherLogo(size = 60.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "What do you want ANAHER to do?",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = LagoonTextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Code modules, math proofs, physics models or full autonomous missions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LagoonTextSecondaryLight
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(messages) { message ->
                        if (message.role == MessageRole.USER) {
                            UserMessageRow(message = message)
                        } else {
                            AnaherMessageRow(
                                message = message,
                                isLiveStreaming = isSending && message == messages.lastOrNull(),
                                statusMessage = statusMessage,
                                onRegenerate = {
                                    val lastUserMsg = messages.findLast { it.role == MessageRole.USER }?.content
                                    if (!lastUserMsg.isNullOrBlank()) {
                                        viewModel.sendMessage(lastUserMsg)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Pinned Composer Bar at bottom
            ComposerBar(
                promptText = inputContent,
                onPromptChange = { inputContent = it },
                onSend = {
                    if (inputContent.isNotBlank()) {
                        val text = inputContent
                        inputContent = ""
                        viewModel.sendMessage(text)
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
}

@Composable
fun UserMessageRow(message: ChatMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = LagoonSurfaceVariantLight,
            border = BorderStroke(1.dp, LagoonBorderLight),
            modifier = Modifier.testTag("user_message_bubble")
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 23.sp),
                color = LagoonTextPrimaryLight,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun AnaherMessageRow(
    message: ChatMessage,
    isLiveStreaming: Boolean,
    statusMessage: String,
    onRegenerate: () -> Unit
) {
    val steps = remember(message.agentStepsJson) {
        MatrixEngine.jsonToSteps(message.agentStepsJson)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        AnaherLogo(size = 32.dp, showGlowPulse = false)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ANAHER",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = LagoonPrimary
                )
                Text(
                    text = message.modelVersion,
                    style = MaterialTheme.typography.labelSmall,
                    color = LagoonTextTertiaryLight
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Body text
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = LagoonTextPrimaryLight
            )

            // Embedded Code Block if present
            if (!message.codeSnippet.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                CodeBlockView(
                    code = message.codeSnippet,
                    language = message.codeLanguage ?: "kotlin"
                )
            }

            // Agent Transparency Step List
            if (steps.isNotEmpty() || isLiveStreaming) {
                Spacer(modifier = Modifier.height(6.dp))
                AgentStepList(
                    steps = steps,
                    isLiveStreaming = isLiveStreaming,
                    statusMessage = statusMessage
                )
            }

            // Confidence & Why Panel
            if (message.status == MessageStatus.COMPLETED) {
                ConfidenceWhyPanel(
                    confidence = message.confidence,
                    whyEvidence = message.whyEvidence,
                    contentToCopy = message.content,
                    onRegenerate = onRegenerate
                )
            }
        }
    }
}
