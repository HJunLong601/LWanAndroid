package com.hjl.core.ui.mine

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hjl.core.R
import com.hjl.core.net.bean.CoinRankItemBean
import com.hjl.core.viewmodel.CoinRankUiState
import com.hjl.core.viewmodel.CoinRankViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class CoinRankActivity : ComponentActivity() {

    private val viewModel: CoinRankViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = CoinRankColorScheme
            ) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CoinRankScreen(
                    uiState = uiState,
                    onBack = ::finish,
                    onRetry = viewModel::refresh,
                    onLoadMore = viewModel::loadMore
                )
            }
        }
    }
}

private val CoinRankColorScheme = lightColorScheme(
    primary = Color(0xFF0F766E),
    secondary = Color(0xFFF59E0B),
    tertiary = Color(0xFFEF4444),
    background = Color(0xFFF6F8FB),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    onBackground = Color(0xFF0F172A)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoinRankScreen(
    uiState: CoinRankUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = androidx.compose.ui.res.stringResource(id = R.string.points_ranking))
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.items.isEmpty() -> {
                CoinRankLoading(modifier = Modifier.padding(innerPadding))
            }

            uiState.errorMessage != null && uiState.items.isEmpty() -> {
                CoinRankError(
                    modifier = Modifier.padding(innerPadding),
                    message = uiState.errorMessage,
                    onRetry = onRetry
                )
            }

            uiState.items.isEmpty() -> {
                CoinRankEmpty(modifier = Modifier.padding(innerPadding))
            }

            else -> {
                val topItems = remember(uiState.items) { uiState.items.take(3) }
                val restItems = remember(uiState.items) { uiState.items.drop(3) }
                LaunchedEffect(uiState.items.size, uiState.hasMore, uiState.isLoadingMore, uiState.isLoading) {
                    if (uiState.items.isEmpty()) {
                        return@LaunchedEffect
                    }
                    snapshotFlow {
                        listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to listState.layoutInfo.totalItemsCount
                    }
                        .map { (lastVisibleIndex, totalCount) ->
                            val safeLastVisibleIndex = lastVisibleIndex ?: 0
                            safeLastVisibleIndex >= totalCount - 3
                        }
                        .distinctUntilChanged()
                        .filter { it }
                        .collect {
                            if (uiState.hasMore && !uiState.isLoadingMore && !uiState.isLoading) {
                                onLoadMore()
                            }
                        }
                }
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner()
                    }

                    if (topItems.isNotEmpty()) {
                        item {
                            TopRankSection(items = topItems)
                        }
                    }

                    if (restItems.isNotEmpty()) {
                        item {
                            Text(
                                text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_more),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    itemsIndexed(
                        items = restItems,
                        key = { _, item -> "${item.userId}_${item.rank}_${item.username}" }
                    ) { _, item ->
                        RankListItem(item = item)
                    }

                    item {
                        LoadMoreFooter(
                            isLoadingMore = uiState.isLoadingMore,
                            loadMoreError = uiState.loadMoreError,
                            hasMore = uiState.hasMore,
                            onRetry = onLoadMore
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroBanner() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF0F766E), Color(0xFF14B8A6))
                    )
                )
                .padding(20.dp)
        ) {
            Text(
                text = androidx.compose.ui.res.stringResource(id = R.string.points_ranking),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.92f)
            )
        }
    }
}

@Composable
private fun TopRankSection(items: List<CoinRankItemBean>) {
    val champion = items.getOrNull(0)
    val runnerUp = items.getOrNull(1)
    val secondRunnerUp = items.getOrNull(2)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        champion?.let {
            TopRankCard(
                item = it,
                title = "NO.1",
                modifier = Modifier.fillMaxWidth(),
                colors = listOf(Color(0xFFF59E0B), Color(0xFFF97316))
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            runnerUp?.let {
                TopRankCard(
                    item = it,
                    title = "NO.2",
                    modifier = Modifier.weight(1f),
                    colors = listOf(Color(0xFF94A3B8), Color(0xFF64748B))
                )
            }
            secondRunnerUp?.let {
                TopRankCard(
                    item = it,
                    title = "NO.3",
                    modifier = Modifier.weight(1f),
                    colors = listOf(Color(0xFFFB7185), Color(0xFFF43F5E))
                )
            }
        }
    }
}

@Composable
private fun TopRankCard(
    item: CoinRankItemBean,
    title: String,
    modifier: Modifier,
    colors: List<Color>
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.linearGradient(colors = colors))
                .padding(18.dp)
        ) {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            RankAvatarSeed(name = item.displayName)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = item.displayName,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(
                    id = R.string.points_rank_score,
                    item.coinCount
                ),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = androidx.compose.ui.res.stringResource(
                    id = R.string.points_rank_level,
                    item.level
                ),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun RankAvatarSeed(name: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1).uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RankListItem(item: CoinRankItemBean) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.rank.ifBlank { "-" },
                modifier = Modifier.width(40.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            RankAvatarSeed(name = item.displayName)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = androidx.compose.ui.res.stringResource(
                        id = R.string.points_rank_level,
                        item.level
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
            }
            Text(
                text = androidx.compose.ui.res.stringResource(
                    id = R.string.points_rank_score,
                    item.coinCount
                ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LoadMoreFooter(
    isLoadingMore: Boolean,
    loadMoreError: String?,
    hasMore: Boolean,
    onRetry: () -> Unit
) {
    when {
        isLoadingMore -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_loading),
                    color = Color(0xFF64748B)
                )
            }
        }

        loadMoreError != null -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = loadMoreError,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFB91C1C)
                )
                TextButton(onClick = onRetry) {
                    Text(text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_retry))
                }
            }
        }

        !hasMore -> {
            Text(
                text = androidx.compose.ui.res.stringResource(id = R.string.its_already_the_end),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun CoinRankLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_loading))
        }
    }
}

@Composable
private fun CoinRankError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message.ifBlank {
                    androidx.compose.ui.res.stringResource(id = R.string.points_rank_load_failed)
                },
                textAlign = TextAlign.Center,
                color = Color(0xFFB91C1C)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Text(text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_retry))
            }
        }
    }
}

@Composable
private fun CoinRankEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(id = R.string.points_rank_empty),
            color = Color(0xFF64748B)
        )
    }
}
