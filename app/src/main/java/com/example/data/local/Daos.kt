package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC LIMIT 100")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Update
    suspend fun updateMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun clearSession(sessionId: String)
}

@Dao
interface MissionDao {
    @Query("SELECT * FROM missions ORDER BY createdAt DESC")
    fun getAllMissions(): Flow<List<Mission>>

    @Query("SELECT * FROM missions WHERE id = :id LIMIT 1")
    fun getMissionById(id: Long): Flow<Mission?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: Mission): Long

    @Update
    suspend fun updateMission(mission: Mission)

    @Query("DELETE FROM missions WHERE id = :id")
    suspend fun deleteMission(id: Long)

    // Tasks
    @Query("SELECT * FROM mission_tasks WHERE missionId = :missionId ORDER BY orderIndex ASC")
    fun getTasksForMission(missionId: Long): Flow<List<MissionTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: MissionTask): Long

    @Update
    suspend fun updateTask(task: MissionTask)

    // Logs
    @Query("SELECT * FROM mission_logs WHERE missionId = :missionId ORDER BY timestamp ASC")
    fun getLogsForMission(missionId: Long): Flow<List<MissionLogEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MissionLogEntry): Long

    // Artifacts
    @Query("SELECT * FROM mission_artifacts WHERE missionId = :missionId ORDER BY createdAt ASC")
    fun getArtifactsForMission(missionId: Long): Flow<List<MissionArtifact>>

    @Query("SELECT * FROM mission_artifacts ORDER BY createdAt DESC")
    fun getAllArtifacts(): Flow<List<MissionArtifact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtifact(artifact: MissionArtifact): Long
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ProjectItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectItem): Long

    @Delete
    suspend fun deleteProject(project: ProjectItem)
}

@Dao
interface KnowledgeDao {
    @Query("SELECT * FROM knowledge_items ORDER BY updatedAt DESC")
    fun getAllKnowledge(): Flow<List<KnowledgeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKnowledge(item: KnowledgeItem): Long

    @Update
    suspend fun updateKnowledge(item: KnowledgeItem)

    @Delete
    suspend fun deleteKnowledge(item: KnowledgeItem)
}

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memory_items ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<MemoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(item: MemoryItem): Long

    @Update
    suspend fun updateMemory(item: MemoryItem)

    @Delete
    suspend fun deleteMemory(item: MemoryItem)
}
