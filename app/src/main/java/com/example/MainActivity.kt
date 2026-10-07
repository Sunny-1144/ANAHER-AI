package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private val viewModel: AnaherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val appScreen by viewModel.appScreen.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val subScreen by viewModel.subScreen.collectAsState()
            val selectedMissionId by viewModel.selectedMissionId.collectAsState()
            val isVoiceOpen by viewModel.isVoiceModeOpen.collectAsState()
            val isWorkspaceOpen by viewModel.isWorkspaceOpen.collectAsState()
            val reducedMotion by viewModel.reducedMotion.collectAsState()

            AnaherTheme(themeMode = themeMode) {
                when (appScreen) {
                    AppScreen.SPLASH -> {
                        SplashScreen(
                            onContinue = { viewModel.finishSplash() },
                            reducedMotion = reducedMotion
                        )
                    }
                    AppScreen.ONBOARDING -> {
                        OnboardingScreen(
                            onFinish = { viewModel.finishOnboarding() }
                        )
                    }
                    AppScreen.MAIN -> {
                        when (subScreen) {
                            SubScreen.MISSION_DETAIL -> {
                                BackHandler { viewModel.closeSubScreen() }
                                MissionControlScreen(
                                    viewModel = viewModel,
                                    missionId = selectedMissionId ?: 0L,
                                    onBack = { viewModel.closeSubScreen() },
                                    onOpenResult = { viewModel.openMissionResult(it) }
                                )
                            }
                            SubScreen.MISSION_RESULT -> {
                                BackHandler { viewModel.closeSubScreen() }
                                MissionResultScreen(
                                    viewModel = viewModel,
                                    missionId = selectedMissionId ?: 0L,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.KNOWLEDGE_VAULT -> {
                                BackHandler { viewModel.closeSubScreen() }
                                KnowledgeVaultScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.LEARNING_MODE -> {
                                BackHandler { viewModel.closeSubScreen() }
                                LearningModeScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.SYSTEM_ACCESS -> {
                                BackHandler { viewModel.closeSubScreen() }
                                SystemAccessScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.MODEL_ROUTER -> {
                                BackHandler { viewModel.closeSubScreen() }
                                ModelRouterScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.ANALYTICS -> {
                                BackHandler { viewModel.closeSubScreen() }
                                AnalyticsScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.SETTINGS, SubScreen.MEMORY_MANAGER -> {
                                BackHandler { viewModel.closeSubScreen() }
                                SettingsScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.closeSubScreen() }
                                )
                            }
                            SubScreen.NONE -> {
                                MainScaffold(
                                    viewModel = viewModel,
                                    currentTab = currentTab
                                )
                            }
                        }

                        // Voice Mode Sheet Overlay
                        VoiceModeSheet(
                            isOpen = isVoiceOpen,
                            onDismiss = { viewModel.setVoiceModeOpen(false) },
                            onSendTranscript = { transcript ->
                                viewModel.sendMessage(transcript)
                                viewModel.navigateToTab(MainTab.CHAT)
                            },
                            reducedMotion = reducedMotion
                        )

                        // Workspace Panel / Sheet Overlay
                        WorkspaceSheet(
                            viewModel = viewModel,
                            isOpen = isWorkspaceOpen,
                            onDismiss = { viewModel.setWorkspaceOpen(false) }
                        )
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val tab: MainTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainScaffold(
    viewModel: AnaherViewModel,
    currentTab: MainTab
) {
    val navItems = listOf(
        BottomNavItem(MainTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(MainTab.CHAT, "Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
        BottomNavItem(MainTab.MISSIONS, "Missions", Icons.Filled.TaskAlt, Icons.Outlined.TaskAlt),
        BottomNavItem(MainTab.PROJECTS, "Projects", Icons.Filled.Folder, Icons.Outlined.Folder),
        BottomNavItem(MainTab.MORE, "More", Icons.Filled.Apps, Icons.Outlined.Apps)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = LagoonPrimary,
                tonalElevation = 6.dp
            ) {
                navItems.forEach { item ->
                    val isSelected = currentTab == item.tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.navigateToTab(item.tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LagoonPrimary,
                            selectedTextColor = LagoonPrimary,
                            indicatorColor = LagoonSurfaceSubtleLight,
                            unselectedIconColor = LagoonTextSecondaryLight,
                            unselectedTextColor = LagoonTextSecondaryLight
                        ),
                        modifier = Modifier.testTag("nav_tab_${item.label.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToChat = { viewModel.navigateToTab(MainTab.CHAT) },
                    onNavigateToMissions = { viewModel.navigateToTab(MainTab.MISSIONS) },
                    onSelectMission = { viewModel.selectMission(it) }
                )
                MainTab.CHAT -> ChatScreen(
                    viewModel = viewModel
                )
                MainTab.MISSIONS -> MissionsScreen(
                    viewModel = viewModel,
                    onSelectMission = { viewModel.selectMission(it) }
                )
                MainTab.PROJECTS -> ProjectsScreen(
                    viewModel = viewModel
                )
                MainTab.MORE -> MoreScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
