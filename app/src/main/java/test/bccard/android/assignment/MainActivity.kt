package test.bccard.android.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import test.bccard.android.assignment.data.remote.UnsplashApi
import test.bccard.android.assignment.data.remote.UnsplashHttpClient
import test.bccard.android.assignment.data.repository.PhotoRepositoryImpl
import test.bccard.android.assignment.domain.repository.PhotoRepository
import test.bccard.android.assignment.domain.usecase.GetPhotosUseCase
import test.bccard.android.assignment.ui.PhotoListScreen
import test.bccard.android.assignment.ui.theme.BccardTheme
import test.bccard.android.assignment.ui.viewmodel.PhotoListViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val accessKey = ""
        val repository: PhotoRepository = PhotoRepositoryImpl(UnsplashApi(UnsplashHttpClient.create(accessKey)))
        val getPhotos = GetPhotosUseCase(repository)

        setContent {
            BccardTheme {
                val viewModel: PhotoListViewModel = viewModel { PhotoListViewModel(getPhotos) }
                PhotoListScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
