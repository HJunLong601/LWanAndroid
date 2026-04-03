package com.hjl.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjl.jetpacklib.mvvm.exception.ExceptionHandler
import com.hjl.core.net.bean.CoinRankItemBean
import com.hjl.core.repository.CoinRankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoinRankUiState(
    val items: List<CoinRankItemBean> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val loadMoreError: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)

@HiltViewModel
class CoinRankViewModel @Inject constructor(
    private val repository: CoinRankRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinRankUiState())
    val uiState: StateFlow<CoinRankUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore) {
            return
        }
        loadPage(page = 1, isRefresh = true)
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasMore) {
            return
        }
        loadPage(page = state.currentPage + 1, isRefresh = false)
    }

    private fun loadPage(page: Int, isRefresh: Boolean) {
        viewModelScope.launch {
            _uiState.update { state ->
                if (isRefresh) {
                    state.copy(
                        isLoading = true,
                        isLoadingMore = false,
                        errorMessage = null,
                        loadMoreError = null,
                        hasMore = true,
                        currentPage = 0
                    )
                } else {
                    state.copy(
                        isLoadingMore = true,
                        loadMoreError = null
                    )
                }
            }

            runCatching {
                repository.getCoinRank(page)
            }.onSuccess { pageBean ->
                _uiState.update { state ->
                    val mergedItems = if (isRefresh) {
                        pageBean.datas
                    } else {
                        state.items + pageBean.datas
                    }.distinctBy { item ->
                        "${item.userId}_${item.rank}_${item.username}"
                    }

                    state.copy(
                        items = mergedItems,
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = null,
                        loadMoreError = null,
                        currentPage = pageBean.curPage.coerceAtLeast(page),
                        hasMore = !pageBean.over &&
                            pageBean.curPage < pageBean.pageCount &&
                            pageBean.datas.isNotEmpty()
                    )
                }
            }.onFailure { throwable ->
                val message = ExceptionHandler.handle(throwable).errorMessage.ifBlank {
                    "加载失败，请稍后重试"
                }
                _uiState.update { state ->
                    if (isRefresh) {
                        state.copy(
                            isLoading = false,
                            errorMessage = message,
                            loadMoreError = null
                        )
                    } else {
                        state.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            loadMoreError = message
                        )
                    }
                }
            }
        }
    }
}
