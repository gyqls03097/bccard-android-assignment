package test.bccard.android.assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import test.bccard.android.assignment.domain.model.PhotoDetail
import test.bccard.android.assignment.domain.model.PhotoExif
import test.bccard.android.assignment.domain.model.PhotoLocation
import test.bccard.android.assignment.domain.usecase.GetPhotoDetailUseCase

data class PhotoDetailUiState(
    val photoDetail: PhotoDetail? = null,
    val isLiked: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class PhotoDetailViewModel(
    private val photoId: String,
    private val getPhotoDetail: GetPhotoDetailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoDetailUiState())
    val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getPhotoDetail(photoId)
                .onSuccess { photoDetail ->
                    _uiState.update {
                        it.copy(
                            photoDetail = photoDetail,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "사진을 불러오지 못했습니다.",
                        )
                    }
                }
        }
    }

    fun toggleLike() {
        // todo db 추가 후 구현
    }

    fun isPhotoExifDraw(exif: PhotoExif?): Boolean {
        if (exif?.make != null) return true
        if (exif?.model != null) return true
        if (exif?.name != null) return true
        if (exif?.exposureTime != null) return true
        if (exif?.aperture != null) return true
        if (exif?.focalLength != null) return true
        if (exif?.iso != null) return true
        return false
    }

    fun isPhotoLocationDraw(location: PhotoLocation?): Boolean {
        if (location?.name != null) return true
        if (location?.city != null) return true
        if (location?.country != null) return true
        val lat = location?.latitude
        if (lat != null && lat > 1) return true
        val long = location?.longitude
        return long != null && long > 1
    }
}
