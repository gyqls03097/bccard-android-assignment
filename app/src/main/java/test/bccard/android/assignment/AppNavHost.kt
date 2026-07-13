package test.bccard.android.assignment

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
import test.bccard.android.assignment.domain.model.Photo
import test.bccard.android.assignment.domain.model.PhotoUser
import test.bccard.android.assignment.ui.screen.AccessKeyScreen
import test.bccard.android.assignment.ui.screen.FavoriteListScreen
import test.bccard.android.assignment.ui.screen.PhotoDetailScreen
import test.bccard.android.assignment.ui.screen.PhotoListScreen
import test.bccard.android.assignment.ui.viewmodel.AccessKeyViewModel
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
                PhotoListViewModel(di.getPhotos)
            }
            PhotoListScreen(
                viewModel = viewModel,
                onPhotoClick = { photo -> navController.navigate(Screen.PhotoDetail.create(photo)) },
                onLikedListClick = { navController.navigate(Screen.FavoriteList) },
            )
        }

        composable<Screen.FavoriteList> {
            FavoriteListScreen()
        }

        composable<Screen.PhotoDetail> { backStackEntry ->
            val route: Screen.PhotoDetail = backStackEntry.toRoute()
            val viewModel: PhotoDetailViewModel = viewModel {
                PhotoDetailViewModel(route.photoId, di.getPhotoDetail)
            }
            PhotoDetailScreen(
                photo = route.toPhoto(),
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
