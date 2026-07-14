package test.bccard.android.assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.favorite.domain.model.FavoritePhoto
import test.bccard.android.assignment.favorite.domain.repository.FavoriteRepository

data class FavoriteListUiState(
    val favorites: List<FavoritePhoto> = emptyList(),
    val error: String = "",
)

class FavoriteListViewModel(
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val errorMessage = MutableStateFlow("")

    val uiState: StateFlow<FavoriteListUiState> = combine(
        flow = favoriteRepository.findAll(),
        flow2 = errorMessage,
    ) { favorites, error ->
        FavoriteListUiState(favorites, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoriteListUiState(),
    )

    fun toggleLike(photo: Photo) {
        viewModelScope.launch { favoriteRepository.toggleFavorite(photo) }
    }

    suspend fun dbImage(photo: Photo): ByteArray? {
        return favoriteRepository.getImage(photo.id)
    }
}
