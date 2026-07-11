package test.bccard.android.assignment.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import test.bccard.android.assignment.AppDI
import test.bccard.android.assignment.ui.AccessKeyScreen
import test.bccard.android.assignment.ui.PhotoListScreen
import test.bccard.android.assignment.ui.viewmodel.AccessKeyViewModel
import test.bccard.android.assignment.ui.viewmodel.PhotoListViewModel

@Serializable
sealed interface Screen {

    @Serializable
    data object AccessKey : Screen

    @Serializable
    data object PhotoList : Screen
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
                onPhotoClick = {},  // todo
                onLikedListClick = {},  // todo
            )
        }
    }
}
