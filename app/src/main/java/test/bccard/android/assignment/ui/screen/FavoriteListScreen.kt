package test.bccard.android.assignment.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import test.bccard.android.assignment.R
import test.bccard.android.assignment.core.domain.model.Photo
import test.bccard.android.assignment.core.domain.model.PhotoUser
import test.bccard.android.assignment.favorite.domain.model.FavoritePhoto
import test.bccard.android.assignment.ui.composables.ProfileImage
import test.bccard.android.assignment.ui.dialog.ProgressDialog
import test.bccard.android.assignment.ui.viewmodel.FavoriteListViewModel


@Composable
fun FavoriteListScreen(
    viewModel: FavoriteListViewModel,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isToggling) {
        ProgressDialog()
    }

    FavoriteListScreenContent(
        favorites = uiState.favorites,
        modifier = modifier,
        dbImage = viewModel::dbImage,
        onPhotoClick = onPhotoClick,
        onToggleLike = viewModel::toggleFavorite,
        onBackClick = onBackClick,
    )
}

@Composable
private fun FavoriteListScreenContent(
    favorites: List<FavoritePhoto>,
    modifier: Modifier = Modifier,
    dbImage: suspend (photo: Photo) -> ByteArray? = { null },
    onPhotoClick: (photo: Photo) -> Unit = { },
    onToggleLike: (photo: Photo) -> Unit = { },
    onBackClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_back),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Text(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 8.dp, top = 20.dp, bottom = 20.dp),
                text = "Favorites",
                fontSize = 20.sp
            )
        }
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        )

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "empty favorites",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1.0f)
                    .background(Color(0xfff5f5f5)),
                contentPadding = PaddingValues(12.dp),
            ) {
                items(items = favorites, key = { it.photo.id }) { favorite ->
                    FavoriteListItem(
                        favorite = favorite,
                        dbImage = dbImage,
                        onClick = { onPhotoClick(favorite.photo) },
                        onToggleLike = { onToggleLike(favorite.photo) },
                        modifier = Modifier.padding(vertical = 6.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteListItem(
    favorite: FavoritePhoto,
    dbImage: suspend (Photo) -> ByteArray?,
    onClick: () -> Unit,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val photo = favorite.photo
    var dbImageArray by remember(photo.id) { mutableStateOf<ByteArray?>(null) }
    LaunchedEffect(photo.id) {
        dbImageArray = dbImage(photo)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.8f),
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model = dbImageArray,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                    )
                },
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(all = 8.dp)
                    .background(
                        color = Color(0x77000000),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(all = 8.dp)
            ) {
                ProfileImage(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    profileImageUrl = photo.user?.profileImageUrl,
                    size = 20.dp
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    text = photo.user?.name ?: photo.id,
                    fontSize = 14.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onToggleLike,
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_heart),
                    modifier = Modifier.align(Alignment.Center),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteListScreenPreview() {
    val sample = List(2) { i ->
        FavoritePhoto(
            photo = Photo(
                id = "id_$i",
                url = "https://example.com/$i.jpg",
                user = PhotoUser(name = "작가 $i"),
                width = 3,
                height = 2,
            ),
            createdAt = i.toLong(),
        )
    }
    FavoriteListScreenContent(favorites = sample)
}

@Preview(showBackground = true)
@Composable
private fun FavoriteListScreenEmptyPreview() {
    FavoriteListScreenContent(favorites = emptyList())
}
