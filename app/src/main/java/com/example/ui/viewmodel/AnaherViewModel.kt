package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.AnaherRepository
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH, ONBOARDING, MAIN
}

enum class MainTab {
    HOME, CHAT, MISSIONS, PROJECTS, MORE
}

enum class SubScreen {
    NONE, MISSION_DETAIL, MISSION_RESULT, KNOWLEDGE_VAULT, LEARNING_MODE, SYSTEM_ACCESS, MODEL_ROUTER, ANALYTICS, SETTINGS, MEMORY_MANAGER
}

class AnaherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnaherRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AnaherRepository(db)
    }

    private val _appScreen = MutableStateFlow(AppScreen.MAIN)
    val appScreen: StateFlow<AppScreen> = _appScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _subScreen = MutableStateFlow(SubScreen.NONE)
    val subScreen: StateFlow<SubScreen> = _subScreen.asStateFlow()

    private val _selectedMissionId = MutableStateFlow<Long?>(null)
    val selectedMissionId: StateFlow<Long?> = _selectedMissionId.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _reducedMotion = MutableStateFlow(false)
    val reducedMotion: StateFlow<Boolean> = _reducedMotion.asStateFlow()

    // Matrix Engine Picker
    private val _matrixVersion = MutableStateFlow("Matrix Auto")
    val matrixVersion: StateFlow<String> = _matrixVersion.asStateFlow()

    private val _matrixMode = MutableStateFlow("Think deeply")
    val matrixMode: StateFlow<String> = _matrixMode.asStateFlow()

    // Panels & Sheets
    private val _isVoiceModeOpen = MutableStateFlow(false)
    val isVoiceModeOpen: StateFlow<Boolean> = _isVoiceModeOpen.asStateFlow()

    private val _isWorkspaceOpen = MutableStateFlow(false)
    val isWorkspaceOpen: StateFlow<Boolean> = _isWorkspaceOpen.asStateFlow()

    private val _workspaceTab = MutableStateFlow(0) // 0: Artifacts, 1: Timeline, 2: Agent Map
    val workspaceTab: StateFlow<Int> = _workspaceTab.asStateFlow()

    private val _activeArtifact = MutableStateFlow<MissionArtifact?>(null)
    val activeArtifact: StateFlow<MissionArtifact?> = _activeArtifact.asStateFlow()

    // Live execution state
    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private val _chatStatusMessage = MutableStateFlow("")
    val chatStatusMessage: StateFlow<String> = _chatStatusMessage.asStateFlow()

    // Repository flows
    val chatMessages: StateFlow<List<ChatMessage>> = repository.getMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val missions: StateFlow<List<Mission>> = repository.getAllMissions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projects: StateFlow<List<ProjectItem>> = repository.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val knowledgeList: StateFlow<List<KnowledgeItem>> = repository.getAllKnowledge()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memoryList: StateFlow<List<MemoryItem>> = repository.getAllMemories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allArtifacts: StateFlow<List<MissionArtifact>> = repository.getAllArtifacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Mission details
    fun getTasksForMission(missionId: Long): Flow<List<MissionTask>> = repository.getTasksForMission(missionId)
    fun getLogsForMission(missionId: Long): Flow<List<MissionLogEntry>> = repository.getLogsForMission(missionId)
    fun getArtifactsForMission(missionId: Long): Flow<List<MissionArtifact>> = repository.getArtifactsForMission(missionId)

    // Navigation actions
    fun navigateToTab(tab: MainTab) {
        _currentTab.value = tab
        _subScreen.value = SubScreen.NONE
    }

    fun openSubScreen(sub: SubScreen) {
        _subScreen.value = sub
    }

    fun closeSubScreen() {
        _subScreen.value = SubScreen.NONE
    }

    fun finishSplash() {
        _appScreen.value = AppScreen.MAIN
    }

    fun finishOnboarding() {
        _appScreen.value = AppScreen.MAIN
    }

    fun selectMission(missionId: Long) {
        _selectedMissionId.value = missionId
        _subScreen.value = SubScreen.MISSION_DETAIL
    }

    fun openMissionResult(missionId: Long) {
        _selectedMissionId.value = missionId
        _subScreen.value = SubScreen.MISSION_RESULT
    }

    // Theme toggles
    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun toggleReducedMotion() {
        _reducedMotion.value = !_reducedMotion.value
    }

    // Engine settings
    fun setMatrixVersion(version: String) {
        _matrixVersion.value = version
    }

    fun setMatrixMode(mode: String) {
        _matrixMode.value = mode
    }

    // Sheet toggles
    fun setVoiceModeOpen(open: Boolean) {
        _isVoiceModeOpen.value = open
    }

    fun setWorkspaceOpen(open: Boolean, initialTab: Int = 0) {
        _isWorkspaceOpen.value = open
        _workspaceTab.value = initialTab
    }

    fun setWorkspaceTab(tabIndex: Int) {
        _workspaceTab.value = tabIndex
    }

    fun openArtifact(artifact: MissionArtifact) {
        _activeArtifact.value = artifact
        _workspaceTab.value = 0
        _isWorkspaceOpen.value = true
    }

    // Chat operations
    fun sendMessage(content: String) {
        if (content.isBlank() || _isSending.value) return
        _isSending.value = true
        _chatStatusMessage.value = "Planning… Building… Testing… Verifying…"
        viewModelScope.launch {
            try {
                repository.sendUserMessage(
                    content = content,
                    version = _matrixVersion.value,
                    mode = _matrixMode.value,
                    onStatusChange = { status ->
                        _chatStatusMessage.value = status
                    }
                )
            } finally {
                _isSending.value = false
                _chatStatusMessage.value = ""
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // Mission operations
    fun createMission(title: String, description: String, version: String = _matrixVersion.value) {
        viewModelScope.launch {
            val id = repository.createAndRunMission(title, description, version)
            _selectedMissionId.value = id
            _subScreen.value = SubScreen.MISSION_DETAIL
        }
    }

    fun deleteMission(id: Long) {
        viewModelScope.launch {
            repository.deleteMission(id)
            if (_selectedMissionId.value == id) {
                _subScreen.value = SubScreen.NONE
            }
        }
    }

    // Project operations
    fun addProject(title: String, description: String, instructions: String, category: String) {
        viewModelScope.launch {
            repository.addProject(title, description, instructions, category)
        }
    }

    fun deleteProject(project: ProjectItem) {
        viewModelScope.launch {
            repository.deleteProject(project)
        }
    }

    // Knowledge Vault operations
    fun addKnowledge(title: String, source: String, type: String, snippet: String) {
        viewModelScope.launch {
            repository.addKnowledge(title, source, type, snippet)
        }
    }

    fun toggleKnowledgeItem(item: KnowledgeItem) {
        viewModelScope.launch {
            repository.updateKnowledge(item.copy(isEnabled = !item.isEnabled))
        }
    }

    fun deleteKnowledge(item: KnowledgeItem) {
        viewModelScope.launch {
            repository.deleteKnowledge(item)
        }
    }

    // Memory operations
    fun addMemory(key: String, value: String, category: String) {
        viewModelScope.launch {
            repository.addMemory(key, value, category)
        }
    }

    fun deleteMemory(item: MemoryItem) {
        viewModelScope.launch {
            repository.deleteMemory(item)
        }
    }
}
