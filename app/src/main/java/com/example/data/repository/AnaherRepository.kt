package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import com.example.engine.MatrixEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnaherRepository(
    private val database: AppDatabase
) {
    private val chatDao = database.chatDao()
    private val missionDao = database.missionDao()
    private val projectDao = database.projectDao()
    private val knowledgeDao = database.knowledgeDao()
    private val memoryDao = database.memoryDao()

    init {
        // Seed initial data asynchronously if empty
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfEmpty()
        }
    }

    // Chat
    fun getMessages(sessionId: String = "default_session"): Flow<List<ChatMessage>> =
        chatDao.getMessagesForSession(sessionId)

    suspend fun sendUserMessage(
        content: String,
        sessionId: String = "default_session",
        version: String = "Matrix Auto",
        mode: String = "Think deeply",
        onStatusChange: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        // 1. Save user message
        val userMsg = ChatMessage(
            sessionId = sessionId,
            role = MessageRole.USER,
            content = content,
            status = MessageStatus.COMPLETED
        )
        chatDao.insertMessage(userMsg)

        // 2. Insert placeholder assistant message with SENDING status
        val botMsgId = chatDao.insertMessage(
            ChatMessage(
                sessionId = sessionId,
                role = MessageRole.ANAHER,
                content = "Initializing autonomous pipeline...",
                status = MessageStatus.STREAMING,
                modelVersion = version
            )
        )

        // 3. Execute MATRIX Pipeline
        val result = MatrixEngine.executeTask(
            userPrompt = content,
            version = version,
            mode = mode,
            onProgressUpdate = { status, step ->
                onStatusChange(status)
            }
        )

        // 4. Update bot message with final result
        val updatedBotMsg = ChatMessage(
            id = botMsgId,
            sessionId = sessionId,
            role = MessageRole.ANAHER,
            content = result.answer,
            status = MessageStatus.COMPLETED,
            agentName = "ANAHER",
            modelVersion = version,
            confidence = result.confidence,
            whyEvidence = result.whyEvidence,
            codeSnippet = result.codeSnippet,
            codeLanguage = result.codeLanguage,
            tokenCount = result.tokenCount,
            latencyMs = result.latencyMs,
            agentStepsJson = MatrixEngine.stepsToJson(result.steps)
        )
        chatDao.updateMessage(updatedBotMsg)

        // If artifacts were generated, link them
        result.artifacts.forEach { artifact ->
            missionDao.insertArtifact(artifact)
        }
    }

    suspend fun clearChat(sessionId: String = "default_session") = withContext(Dispatchers.IO) {
        chatDao.clearSession(sessionId)
    }

    // Missions
    fun getAllMissions(): Flow<List<Mission>> = missionDao.getAllMissions()
    fun getMissionById(id: Long): Flow<Mission?> = missionDao.getMissionById(id)
    fun getTasksForMission(missionId: Long): Flow<List<MissionTask>> = missionDao.getTasksForMission(missionId)
    fun getLogsForMission(missionId: Long): Flow<List<MissionLogEntry>> = missionDao.getLogsForMission(missionId)
    fun getArtifactsForMission(missionId: Long): Flow<List<MissionArtifact>> = missionDao.getArtifactsForMission(missionId)
    fun getAllArtifacts(): Flow<List<MissionArtifact>> = missionDao.getAllArtifacts()

    suspend fun createAndRunMission(
        title: String,
        description: String,
        version: String = "Matrix 4.1 Forge"
    ): Long = withContext(Dispatchers.IO) {
        val missionId = missionDao.insertMission(
            Mission(
                title = title,
                description = description,
                stage = MissionStage.UNDERSTANDING,
                progressPercent = 10,
                modelVersion = version,
                statusMessage = "A1 Intake analyzing mission objectives..."
            )
        )

        // Add initial tasks
        val task1 = missionDao.insertTask(
            MissionTask(
                missionId = missionId,
                title = "Specification Analysis & Risk Boundary",
                assignedAgent = "A1 Intake / A2 Safety",
                status = TaskStatus.RUNNING,
                orderIndex = 1
            )
        )
        missionDao.insertTask(
            MissionTask(
                missionId = missionId,
                title = "Architectural Decomposition DAG",
                assignedAgent = "A4 Planner",
                status = TaskStatus.PENDING,
                orderIndex = 2
            )
        )
        missionDao.insertTask(
            MissionTask(
                missionId = missionId,
                title = "Code Synthesis & Test Generation",
                assignedAgent = "S3 Coder / S4 Tester",
                status = TaskStatus.PENDING,
                orderIndex = 3
            )
        )
        missionDao.insertTask(
            MissionTask(
                missionId = missionId,
                title = "Formal Verification & Consensus Judge",
                assignedAgent = "Q1 Verifier / Q4 Final QA",
                status = TaskStatus.PENDING,
                orderIndex = 4
            )
        )

        missionDao.insertLog(
            MissionLogEntry(
                missionId = missionId,
                level = LogLevel.INFO,
                agentName = "A3 Conductor",
                message = "Mission #$missionId registered with autonomous graph. Executing under $version."
            )
        )

        missionId
    }

    suspend fun advanceMission(missionId: Long, stage: MissionStage, progress: Int, message: String) =
        withContext(Dispatchers.IO) {
            val mission = missionDao.getMissionById(missionId).firstOrNull() ?: return@withContext
            missionDao.updateMission(
                mission.copy(
                    stage = stage,
                    progressPercent = progress,
                    statusMessage = message,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

    suspend fun deleteMission(id: Long) = withContext(Dispatchers.IO) {
        missionDao.deleteMission(id)
    }

    // Projects
    fun getAllProjects(): Flow<List<ProjectItem>> = projectDao.getAllProjects()
    suspend fun addProject(title: String, description: String, instructions: String, category: String) =
        withContext(Dispatchers.IO) {
            projectDao.insertProject(
                ProjectItem(
                    title = title,
                    description = description,
                    instructions = instructions,
                    category = category
                )
            )
        }
    suspend fun deleteProject(project: ProjectItem) = withContext(Dispatchers.IO) {
        projectDao.deleteProject(project)
    }

    // Knowledge Vault
    fun getAllKnowledge(): Flow<List<KnowledgeItem>> = knowledgeDao.getAllKnowledge()
    suspend fun addKnowledge(title: String, source: String, type: String, snippet: String) =
        withContext(Dispatchers.IO) {
            knowledgeDao.insertKnowledge(
                KnowledgeItem(
                    title = title,
                    source = source,
                    type = type,
                    snippet = snippet
                )
            )
        }
    suspend fun updateKnowledge(item: KnowledgeItem) = withContext(Dispatchers.IO) {
        knowledgeDao.updateKnowledge(item)
    }
    suspend fun deleteKnowledge(item: KnowledgeItem) = withContext(Dispatchers.IO) {
        knowledgeDao.deleteKnowledge(item)
    }

    // Memories
    fun getAllMemories(): Flow<List<MemoryItem>> = memoryDao.getAllMemories()
    suspend fun addMemory(key: String, value: String, category: String) = withContext(Dispatchers.IO) {
        memoryDao.insertMemory(
            MemoryItem(
                key = key,
                value = value,
                category = category
            )
        )
    }
    suspend fun deleteMemory(item: MemoryItem) = withContext(Dispatchers.IO) {
        memoryDao.deleteMemory(item)
    }

    private suspend fun seedInitialDataIfEmpty() {
        val existingProjects = projectDao.getAllProjects().firstOrNull()
        if (existingProjects.isNullOrEmpty()) {
            projectDao.insertProject(
                ProjectItem(
                    title = "Autonomous Cloud Orchestrator",
                    description = "Event-driven microservice dispatch system with zero-trust safety checks.",
                    instructions = "Ensure all endpoints are authenticated with OAuth2 and rate-limited.",
                    category = "Backend System",
                    filesCount = 8
                )
            )
            projectDao.insertProject(
                ProjectItem(
                    title = "Lagoon Mobile Client",
                    description = "Jetpack Compose multi-screen client with live WebSocket telemetry.",
                    instructions = "Target 60fps animations with WCAG AA compliance.",
                    category = "Mobile App",
                    filesCount = 14
                )
            )
        }

        val existingMissions = missionDao.getAllMissions().firstOrNull()
        if (existingMissions.isNullOrEmpty()) {
            val mid = missionDao.insertMission(
                Mission(
                    title = "Build Distributed Cache Engine",
                    description = "Construct high-throughput in-memory cache with Raft consensus and TTL eviction.",
                    stage = MissionStage.DELIVERING,
                    progressPercent = 88,
                    modelVersion = "Matrix 4.1 Forge",
                    tokensUsed = 14200,
                    costEst = 0.028,
                    statusMessage = "Q4 Final QA verified 12/12 unit tests and benchmarked at 1.4M ops/sec."
                )
            )
            missionDao.insertTask(
                MissionTask(
                    missionId = mid,
                    title = "Parse concurrency requirements & lock-free primitives",
                    assignedAgent = "A1 Intake / S2 Architect",
                    status = TaskStatus.COMPLETED,
                    orderIndex = 1,
                    durationMs = 380,
                    outputSummary = "Architecture approved. Selected Go sync primitives."
                )
            )
            missionDao.insertTask(
                MissionTask(
                    missionId = mid,
                    title = "Raft Consensus and Heartbeat Dispatcher",
                    assignedAgent = "S3 Coder",
                    status = TaskStatus.COMPLETED,
                    orderIndex = 2,
                    durationMs = 820,
                    outputSummary = "Implemented leader election and log replication."
                )
            )
            missionDao.insertTask(
                MissionTask(
                    missionId = mid,
                    title = "Fuzz Testing & Network Partition Verification",
                    assignedAgent = "S4 Tester / Q1 Verifier",
                    status = TaskStatus.COMPLETED,
                    orderIndex = 3,
                    durationMs = 640,
                    outputSummary = "0 deadlocks detected across 10,000 randomized network partitions."
                )
            )
            missionDao.insertTask(
                MissionTask(
                    missionId = mid,
                    title = "Benchmark against Redis protocol",
                    assignedAgent = "Q4 Final QA",
                    status = TaskStatus.RUNNING,
                    orderIndex = 4,
                    durationMs = 210,
                    outputSummary = "Testing throughput under 100 concurrent worker threads."
                )
            )

            missionDao.insertLog(
                MissionLogEntry(
                    missionId = mid,
                    level = LogLevel.INFO,
                    agentName = "A3 Conductor",
                    message = "Allocated Matrix 4.1 Forge pipeline. Budget set to $0.05 max."
                )
            )
            missionDao.insertLog(
                MissionLogEntry(
                    missionId = mid,
                    level = LogLevel.INFO,
                    agentName = "S3 Coder",
                    message = "Generated cache_raft.go and cache_eviction_lru.go"
                )
            )
            missionDao.insertLog(
                MissionLogEntry(
                    missionId = mid,
                    level = LogLevel.INFO,
                    agentName = "Q1 Verifier",
                    message = "Verified memory bounds: leak detection tests returned 0 byte delta."
                )
            )

            missionDao.insertArtifact(
                MissionArtifact(
                    missionId = mid,
                    name = "cache_engine_core.go",
                    type = ArtifactType.CODE,
                    content = "// In-Memory Raft Cache Engine\npackage cache\n\ntype Node struct {\n    id string\n    leader bool\n}\n\nfunc NewCluster(nodes []string) *Cluster {\n    return &Cluster{Nodes: nodes}\n}",
                    verified = true
                )
            )
            missionDao.insertArtifact(
                MissionArtifact(
                    missionId = mid,
                    name = "Benchmark_Report_V1.pdf",
                    type = ArtifactType.DOCUMENT,
                    content = "Summary: Throughput 1,420,000 ops/sec. Latency p99 < 1.2ms. Zero packet loss.",
                    verified = true
                )
            )
        }

        val existingKnowledge = knowledgeDao.getAllKnowledge().firstOrNull()
        if (existingKnowledge.isNullOrEmpty()) {
            knowledgeDao.insertKnowledge(
                KnowledgeItem(
                    title = "Lagoon White Design System Guidelines",
                    source = "anaher://design/spec-v2.md",
                    type = "Design Spec",
                    snippet = "Tokens: Primary #00838F, Canvas #F7FAFC, Cyan Glow #00E5FF, 24dp composer radius.",
                    sizeKb = 28
                )
            )
            knowledgeDao.insertKnowledge(
                KnowledgeItem(
                    title = "Matrix 4.0 Multi-Agent Orchestration Protocol",
                    source = "anaher://core/agents.proto",
                    type = "Architecture",
                    snippet = "Typed JSON blackboard: {task_id, goal, subtask, inputs, constraints, confidence, evidence}.",
                    sizeKb = 64
                )
            )
            knowledgeDao.insertKnowledge(
                KnowledgeItem(
                    title = "Kotlin Jetpack Compose Production Best Practices",
                    source = "internal://android/standards.md",
                    type = "Engineering",
                    snippet = "StateFlow, MVVM, 48dp minimum touch targets, dynamic theming support.",
                    sizeKb = 42
                )
            )
        }

        val existingMemories = memoryDao.getAllMemories().firstOrNull()
        if (existingMemories.isNullOrEmpty()) {
            memoryDao.insertMemory(
                MemoryItem(
                    key = "preferred_architecture",
                    value = "Clean Architecture / MVVM with Jetpack Compose",
                    category = "Engineering"
                )
            )
            memoryDao.insertMemory(
                MemoryItem(
                    key = "primary_accent",
                    value = "Teal #00838F on Lagoon White canvas",
                    category = "Design"
                )
            )
            memoryDao.insertMemory(
                MemoryItem(
                    key = "test_confidence_target",
                    value = "Minimum 95% confidence before delivery",
                    category = "Quality"
                )
            )
        }
    }
}
