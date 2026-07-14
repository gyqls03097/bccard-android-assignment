package test.bccard.android.assignment

import android.util.Base64
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.core.domain.model.PhotoUser
import test.bccard.android.assignment.ui.screen.AccessKeyScreen
import test.bccard.android.assignment.ui.screen.FavoriteListScreen
import test.bccard.android.assignment.ui.screen.PhotoDetailScreen
import test.bccard.android.assignment.ui.screen.PhotoExpandScreen
import test.bccard.android.assignment.ui.screen.PhotoListScreen
import test.bccard.android.assignment.ui.viewmodel.AccessKeyViewModel
import test.bccard.android.assignment.ui.viewmodel.FavoriteListViewModel
import test.bccard.android.assignment.ui.viewmodel.PhotoDetailViewModel
import test.bccard.android.assignment.ui.viewmodel.PhotoListViewModel

@Serializable
sealed interface Screen {

    @Serializable
    data object AccessKey : Screen

    @Serializable
    data object PhotoList : Screen

    @Serializable
    data object FavoriteList : Screen

    @Serializable
    data class PhotoExpand(val base64Url: String) : Screen {

        fun getUrl(): String {
            val arr = Base64.decode(base64Url, Base64.URL_SAFE or Base64.NO_WRAP)
            return arr.toString(Charsets.UTF_8)
        }

        companion object {
            fun create(url: String): PhotoExpand {
                val arr = url.toByteArray(Charsets.UTF_8)
                return PhotoExpand(Base64.encodeToString(arr, Base64.URL_SAFE or Base64.NO_WRAP))
            }
        }
    }

    @Serializable
    data class PhotoDetail(
        val photoId: String,
        val width: Int = 0,
        val height: Int = 0,
        val urlDetail: String? = null,
        val userId: String = "",
        val userName: String? = null,
        val userUsername: String? = null,
        val userProfileImageUrl: String? = null,
    ) : Screen {

        fun toPhoto(): Photo = Photo(
            id = photoId,
            user = PhotoUser(
                id = userId,
                username = userUsername,
                name = userName,
                profileImageUrl = userProfileImageUrl,
            ),
            width = width,
            height = height,
            urlDetail = urlDetail,
        )

        companion object {
            fun create(photo: Photo): PhotoDetail {
                return PhotoDetail(
                    photoId = photo.id,
                    width = photo.width,
                    height = photo.height,
                    urlDetail = photo.urlDetail,
                    userId = photo.user?.id ?: "",
                    userName = photo.user?.name,
                    userUsername = photo.user?.username,
                    userProfileImageUrl = photo.user?.profileImageUrl,
                )
            }
        }
    }
}

@Composable
fun AppNavHost(
    di: AppDI,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Any = if (di.pref.accessKey.isEmpty()) Screen.AccessKey else Screen.PhotoList,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<Screen.AccessKey> {
            val viewModel: AccessKeyViewModel = viewModel {
                AccessKeyViewModel(saveAccessKey = { accessKey -> di.pref.accessKey = accessKey })
            }
            AccessKeyScreen(
                viewModel = viewModel,
                onSaved = {
                    navController.navigate(Screen.PhotoList) {
                        popUpTo<Screen.AccessKey> { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.PhotoList> {
            val viewModel: PhotoListViewModel = viewModel {
                PhotoListViewModel(
                    getPhotos = di.getPhotos,
                    toggleUseCase = di.favoriteToggleUseCase,
                    favoriteRepository = di.favoriteRepository,
                )
            }
            PhotoListScreen(
                viewModel = viewModel,
                onPhotoClick = { photo -> navController.navigate(Screen.PhotoDetail.create(photo)) },
                onLikedListClick = { navController.navigate(Screen.FavoriteList) },
            )
        }

        composable<Screen.FavoriteList> {
            val viewModel: FavoriteListViewModel = viewModel {
                FavoriteListViewModel(
                    favoriteRepository = di.favoriteRepository,
                    favoriteToggleUseCase = di.favoriteToggleUseCase
                )
            }
            FavoriteListScreen(
                viewModel = viewModel,
                onPhotoClick = { photo -> navController.navigate(Screen.PhotoDetail.create(photo)) },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<Screen.PhotoDetail> { backStackEntry ->
            val route: Screen.PhotoDetail = backStackEntry.toRoute()
            val viewModel: PhotoDetailViewModel = viewModel {
                PhotoDetailViewModel(
                    photo = route.toPhoto(),
                    getPhotoDetail = di.getPhotoDetail,
                    downloadPhoto = di.downloadPhoto,
                    toggleUseCase = di.favoriteToggleUseCase,
                    favoriteRepository = di.favoriteRepository,
                )
            }
            PhotoDetailScreen(
                photo = route.toPhoto(),
                viewModel = viewModel,
                onPhotoClick = { url -> navController.navigate(Screen.PhotoExpand.create(url)) },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<Screen.PhotoExpand> { backStackEntry ->
            val route: Screen.PhotoExpand = backStackEntry.toRoute()
            PhotoExpandScreen(
                url = route.getUrl(),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
