package com.hjl.core.ui.mine

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hjl.core.R
import com.hjl.core.viewmodel.LocalLlmUiState
import com.hjl.core.viewmodel.LocalLlmViewModel
import com.hjl.jetpacklib.mvvm.view.BaseComposeActivity
import com.hjl.jetpacklib.mvvm.view.BaseComposeTitleBar
import dagger.hilt.android.AndroidEntryPoint
import com.hjl.commonlib.R as CommonRes

@AndroidEntryPoint
class LocalLlmActivity : BaseComposeActivity() {

    private val viewModel: LocalLlmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = LocalLlmColorScheme) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LocalLlmScreen(
                    uiState = uiState,
                    onBack = ::finish,
                    onModelPathChange = viewModel::updateModelPath,
                    onPromptChange = viewModel::updatePrompt,
                    onLoadModel = viewModel::loadModel,
                    onGenerate = viewModel::generate,
                    onStop = viewModel::stopGenerate
                )
            }
        }
    }

    override fun isUseFullScreenMode(): Boolean {
        return true
    }

    override fun isUseBlackFontWithStatusBar(): Boolean {
        return false
    }
}

private val LocalLlmColorScheme = lightColorScheme(
    primary = Color(0xFF00796B),
    secondary = Color(0xFF455A64),
    background = Color.White,
    surface = Color.White,
    onSurface = Color(0xFF172026),
    onBackground = Color(0xFF172026)
)

@Composable
private fun LocalLlmScreen(
    uiState: LocalLlmUiState,
    onBack: () -> Unit,
    onModelPathChange: (String) -> Unit,
    onPromptChange: (String) -> Unit,
    onLoadModel: () -> Unit,
    onGenerate: () -> Unit,
    onStop: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            BaseComposeTitleBar(
                title = stringResource(id = R.string.local_llm_title),
                onBack = onBack,
                backgroundColor = colorResource(id = CommonRes.color.common_base_theme_color),
                contentColor = Color.White,
                immersive = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StatusText(uiState = uiState)
            OutlinedTextField(
                value = uiState.modelPath,
                onValueChange = onModelPathChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.local_llm_model_path)) },
                singleLine = true,
                enabled = !uiState.isLoadingModel && !uiState.isGenerating
            )
            Button(
                onClick = onLoadModel,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoadingModel && !uiState.isGenerating
            ) {
                Text(text = stringResource(id = R.string.local_llm_load_model))
            }
            OutlinedTextField(
                value = uiState.prompt,
                onValueChange = onPromptChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                label = { Text(text = stringResource(id = R.string.local_llm_prompt)) },
                enabled = !uiState.isGenerating
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onGenerate,
                    modifier = Modifier.weight(1f),
                    enabled = uiState.isModelReady && !uiState.isGenerating
                ) {
                    Text(text = stringResource(id = R.string.local_llm_generate))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onStop,
                    modifier = Modifier.weight(1f),
                    enabled = uiState.isGenerating,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF455A64))
                ) {
                    Text(text = stringResource(id = R.string.local_llm_stop))
                }
            }
            AnswerPanel(answer = uiState.answer)
        }
    }
}

@Composable
private fun StatusText(uiState: LocalLlmUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = uiState.statusText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB91C1C)
            )
        }
    }
}

@Composable
private fun AnswerPanel(answer: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        color = Color(0xFFF8FAFC),
        tonalElevation = 1.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(14.dp))
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = answer.ifBlank { stringResource(id = R.string.local_llm_empty_answer) },
                style = MaterialTheme.typography.bodyMedium,
                color = if (answer.isBlank()) Color(0xFF64748B) else Color(0xFF172026)
            )
        }
    }
}
