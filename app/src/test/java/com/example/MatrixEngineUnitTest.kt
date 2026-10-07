package com.example

import com.example.data.model.TaskStatus
import com.example.engine.AgentExecutionStep
import com.example.engine.MatrixEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MatrixEngineUnitTest {

    @Test
    fun testJsonSerializationOfAgentSteps() {
        val steps = listOf(
            AgentExecutionStep(
                agentCode = "A1",
                agentName = "Intake Agent",
                status = TaskStatus.COMPLETED,
                durationMs = 120,
                tokenCount = 50,
                details = "Parsed intent successfully"
            ),
            AgentExecutionStep(
                agentCode = "S3",
                agentName = "Coder",
                status = TaskStatus.COMPLETED,
                durationMs = 350,
                tokenCount = 200,
                details = "Synthesized module"
            )
        )

        val json = MatrixEngine.stepsToJson(steps)
        assertTrue(json.contains("A1"))
        assertTrue(json.contains("Intake Agent"))
        assertTrue(json.contains("S3"))

        val deserialized = MatrixEngine.jsonToSteps(json)
        assertEquals(2, deserialized.size)
        assertEquals("A1", deserialized[0].agentCode)
        assertEquals("S3", deserialized[1].agentCode)
    }

    @Test
    fun testExecuteTaskGeneratesStructuredResult() = runBlocking {
        val result = MatrixEngine.executeTask(
            userPrompt = "Build an Android background mission runner in Kotlin",
            version = "Matrix 4.1 Forge",
            mode = "Think deeply",
            onProgressUpdate = { _, _ -> }
        )

        assertNotNull(result)
        assertTrue(result.answer.isNotBlank())
        assertTrue(result.confidence >= 95)
        assertTrue(result.steps.isNotEmpty())
    }
}
