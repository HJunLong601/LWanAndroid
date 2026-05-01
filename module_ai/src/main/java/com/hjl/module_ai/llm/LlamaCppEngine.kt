package com.hjl.module_ai.llm

import android.content.Context
import com.arm.aichat.AiChat
import com.arm.aichat.InferenceEngine
import com.hjl.module_ai.BuildConfig
import kotlinx.coroutines.flow.Flow

/**
 * author: long
 * description 基于 llama.cpp 的本地大模型推理实现
 * Date: 2026/5/1
 */
class LlamaCppEngine(context: Context) : LocalLlmEngine {

    private val appContext = context.applicationContext
    private val inferenceEngine: InferenceEngine by lazy {
        check(BuildConfig.LLAMA_CPP_NATIVE_ENABLED) {
            "llama.cpp native 未启用，请使用 -PenableLlamaCppNative=true 构建，并确认 Android SDK 已安装 CMake/NDK"
        }
        AiChat.getInferenceEngine(appContext)
    }

    override suspend fun loadModel(modelPath: String) {
        inferenceEngine.loadModel(modelPath)
    }

    override suspend fun setSystemPrompt(systemPrompt: String) {
        inferenceEngine.setSystemPrompt(systemPrompt)
    }

    override fun generate(prompt: String, config: LlmGenerationConfig): Flow<String> {
        return inferenceEngine.sendUserPrompt(prompt, config.predictLength)
    }

    override fun stop() {
        inferenceEngine.cancelGeneration()
    }

    override fun release() {
        inferenceEngine.destroy()
    }
}
