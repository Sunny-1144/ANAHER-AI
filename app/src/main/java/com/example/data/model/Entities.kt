package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageRole {
    USER, ANAHER, AGENT, SYSTEM
}

enum class MessageStatus {
    SENDING, STREAMING, COMPLETED, FAILED
}

enum class MissionStage {
    UNDERSTANDING, PLANNING, BUILDING, TESTING, VERIFYING, DELIVERING, COMPLETED, PAUSED, FAILED
}

enum class TaskStatus {
    PENDING, RUNNING, COMPLETED, FAILED
}

enum class LogLevel {
    INFO, WARN, ERROR
}

enum class ArtifactType {
    CODE, DOCUMENT, PREVIEW, FILE
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String = "default_session",
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.COMPLETED,
    val agentName: String = "ANAHER",
    val modelVersion: String = "Matrix 4.0 Core",
    val confidence: Int = 98,
    val whyEvidence: String = "Verified by autonomous pipeline with unit validation.",
    val codeSnippet: String? = null,
    val codeLanguage: String? = null,
    val tokenCount: Int = 0,
    val latencyMs: Long = 0,
    val isWorkExpanded: Boolean = false,
    val agentStepsJson: String = "" // List of agent steps executed
)

@Entity(tableName = "missions")
data class Mission(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val stage: MissionStage = MissionStage.UNDERSTANDING,
    val progressPercent: Int = 0,
    val modelVersion: String = "Matrix 4.1 Forge",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val tokensUsed: Int = 0,
    val costEst: Double = 0.004,
    val statusMessage: String = "Initializing autonomous pipeline..."
)

@Entity(tableName = "mission_tasks")
data class MissionTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val missionId: Long,
    val title: String,
    val assignedAgent: String,
    val status: TaskStatus = TaskStatus.PENDING,
    val orderIndex: Int = 0,
    val durationMs: Long = 0,
    val outputSummary: String = ""
)

@Entity(tableName = "mission_logs")
data class MissionLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val missionId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val level: LogLevel = LogLevel.INFO,
    val agentName: String,
    val message: String
)

@Entity(tableName = "mission_artifacts")
data class MissionArtifact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val missionId: Long,
    val name: String,
    val type: ArtifactType,
    val content: String,
    val verified: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "projects")
data class ProjectItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val instructions: String,
    val category: String = "Application",
    val filesCount: Int = 1,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "knowledge_items")
data class KnowledgeItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val source: String,
    val type: String = "Document",
    val snippet: String,
    val isIndexed: Boolean = true,
    val isEnabled: Boolean = true,
    val sizeKb: Int = 12,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "memory_items")
data class MemoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: String,
    val value: String,
    val category: String = "Preferences",
    val isEditable: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class AgentStep(
    val name: String,
    val role: String,
    val status: TaskStatus,
    val durationMs: Long,
    val tokenCount: Int,
    val output: String
)
