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
import test.bccard.android.assignment.favorite.domain.usecase.FavoriteToggleUseCase

data class FavoriteListUiState(
    val favorites: List<FavoritePhoto> = emptyList(),
    val error: String = "",
    val isToggling: Boolean = false,
)

class FavoriteListViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val favoriteToggleUseCase: FavoriteToggleUseCase
) : ViewModel() {

    private val errorMessage = MutableStateFlow("")

    private val isToggling = MutableStateFlow(false)

    val uiState: StateFlow<FavoriteListUiState> = combine(
        flow = favoriteRepository.findAll(),
        flow2 = errorMessage,
        flow3 = isToggling,
    ) { favorites, error, toggling ->
        FavoriteListUiState(favorites, error, toggling)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoriteListUiState(),
    )

    fun toggleFavorite(photo: Photo) {
        if (isToggling.value) return
        isToggling.value = true
        viewModelScope.launch {
            favoriteToggleUseCase(photo)
            isToggling.value = false
        }
    }

    suspend fun dbImage(photo: Photo): ByteArray? {
        return favoriteRepository.getImage(photo.id)
    }
}
