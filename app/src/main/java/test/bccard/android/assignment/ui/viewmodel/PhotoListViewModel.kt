package test.bccard.android.assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.domain.usecase.GetPhotosUseCase
import test.bccard.android.assignment.favorite.domain.repository.FavoriteRepository
import test.bccard.android.assignment.favorite.domain.usecase.FavoriteToggleUseCase

data class PhotoListUiState(
    val photos: List<Photo> = emptyList(),
    val likedIds: Set<String> = emptySet(),
    val isToggling: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false,
)

class PhotoListViewModel(
    private val getPhotos: GetPhotosUseCase,
    private val toggleUseCase: FavoriteToggleUseCase,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoListUiState())
    val uiState: StateFlow<PhotoListUiState> = _uiState.asStateFlow()

    private var nextPage = 1

    private var isPageLoading = false

    init {
        loadNextPage()
        viewModelScope.launch {
            favoriteRepository.findIdAll().collect { ids ->
                _uiState.update { it.copy(likedIds = ids) }
            }
        }
    }

    fun loadNextPage() {
        if (isPageLoading) return
        val state = _uiState.value
        if (state.endReached || state.error != null) return
        fetch()
    }

    fun retry() {
        if (isPageLoading) return
        fetch()
    }

    private fun fetch() {
        isPageLoading = true
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getPhotos(nextPage)
                .onSuccess { page ->
                    _uiState.update { state ->
                        val existItems: Set<String> = state.photos.map { it.id }.toSet()
                        val newItems = page.filter { it.id.isNotBlank() && !existItems.contains(it.id) }
                        state.copy(
                            photos = state.photos + newItems,
                            isLoading = false,
                            error = null,
                            endReached = page.isEmpty(),
                        )
                    }
                    if (page.isNotEmpty()) nextPage++
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "사진을 불러오지 못했습니다.",
                        )
                    }
                }
            isPageLoading = false
        }
    }

    fun toggleFavorite(photo: Photo) {
        if (_uiState.value.isToggling) return
        _uiState.update { it.copy(isToggling = true) }
        viewModelScope.launch {
            toggleUseCase(photo)
            _uiState.update { it.copy(isToggling = false) }
        }
    }
}
