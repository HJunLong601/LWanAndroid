package com.hjl.module_ai.llm

/**
 * author: long
 * description 本地大模型运行状态
 * Date: 2026/5/1
 */
sealed class LlmEngineState {
    object Idle : LlmEngineState()
    object Loading : LlmEngineState()
    object Ready : LlmEngineState()
    object Generating : LlmEngineState()
    data class Error(val message: String) : LlmEngineState()
}
