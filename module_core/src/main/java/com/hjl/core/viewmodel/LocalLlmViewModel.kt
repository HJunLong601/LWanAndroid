package com.hjl.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjl.module_ai.llm.LlamaCppEngine
import com.hjl.module_ai.llm.LlmGenerationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocalLlmUiState(
    val modelPath: String = DEFAULT_MODEL_PATH,
    val prompt: String = "",
    val answer: String = "",
    val statusText: String = "未加载模型",
    val isLoadingModel: Boolean = false,
    val isModelReady: Boolean = false,
    val isGenerating: Boolean = false,
    val errorMessage: String? = null
)

private const val DEFAULT_MODEL_PATH =
    "/sdcard/Android/data/com.hjl.lwanandroid/files/qwen2.5-1.5b-instruct-q4_k_m.gguf"

@HiltViewModel
class LocalLlmViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val engine = LlamaCppEngine(context)
    private val _uiState = MutableStateFlow(LocalLlmUiState())
    val uiState: StateFlow<LocalLlmUiState> = _uiState.asStateFlow()

    private var generateJob: Job? = null

    fun updateModelPath(modelPath: String) {
        _uiState.update { state ->
            state.copy(modelPath = modelPath, errorMessage = null)
        }
    }

    fun updatePrompt(prompt: String) {
        _uiState.update { state ->
            state.copy(prompt = prompt, errorMessage = null)
        }
    }

    fun loadModel() {
        val modelPath = _uiState.value.modelPath.trim()
        if (modelPath.isEmpty()) {
            _uiState.update { state -> state.copy(errorMessage = "请输入 GGUF 模型文件路径") }
            return
        }
        if (_uiState.value.isLoadingModel || _uiState.value.isGenerating) {
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoadingModel = true,
                    isModelReady = false,
                    statusText = "正在加载模型",
                    errorMessage = null
                )
            }

            runCatching {
                engine.loadModel(modelPath)
                engine.setSystemPrompt("你是一个运行在安卓本地设备上的中文助手，回答要简洁、准确。")
            }.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoadingModel = false,
                        isModelReady = true,
                        statusText = "模型已就绪"
                    )
                }
            }.onFailure { throwable ->
                _uiState.update { state ->
                    state.copy(
                        isLoadingModel = false,
                        isModelReady = false,
                        statusText = "模型加载失败",
                        errorMessage = throwable.message ?: "模型加载失败，请检查路径和设备内存"
                    )
                }
            }
        }
    }

    fun generate() {
        val prompt = _uiState.value.prompt.trim()
        if (prompt.isEmpty()) {
            _uiState.update { state -> state.copy(errorMessage = "请输入问题") }
            return
        }
        if (!_uiState.value.isModelReady || _uiState.value.isGenerating) {
            return
        }

        generateJob = viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    answer = "",
                    isGenerating = true,
                    statusText = "正在生成",
                    errorMessage = null
                )
            }

            runCatching {
                engine.generate(prompt, LlmGenerationConfig(predictLength = 512)).collect { token ->
                    _uiState.update { state ->
                        state.copy(answer = state.answer + token)
                    }
                }
            }.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isGenerating = false,
                        statusText = "模型已就绪"
                    )
                }
            }.onFailure { throwable ->
                _uiState.update { state ->
                    state.copy(
                        isGenerating = false,
                        statusText = "生成失败",
                        errorMessage = throwable.message ?: "生成失败"
                    )
                }
            }
        }
    }

    fun stopGenerate() {
        engine.stop()
        generateJob?.cancel()
        generateJob = null
        _uiState.update { state ->
            state.copy(isGenerating = false, statusText = "已停止")
        }
    }

    override fun onCleared() {
        generateJob?.cancel()
        runCatching { engine.release() }
        super.onCleared()
    }
}
