package com.hjl.module_ai.llm

import kotlinx.coroutines.flow.Flow

/**
 * author: long
 * description 本地大模型统一推理接口
 * Date: 2026/5/1
 */
interface LocalLlmEngine {

    suspend fun loadModel(modelPath: String)

    suspend fun setSystemPrompt(systemPrompt: String)

    fun generate(prompt: String, config: LlmGenerationConfig = LlmGenerationConfig()): Flow<String>

    fun stop()

    fun release()
}
