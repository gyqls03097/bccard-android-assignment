package test.bccard.android.assignment.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
import test.bccard.android.assignment.ui.viewmodel.PhotoListViewModel


@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit = {},
    onLikedListClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PhotoListScreenContent(
        photos = uiState.photos,
        modifier = modifier,
        isLoading = uiState.isLoading,
        error = uiState.error,
        isLikeId = { id -> uiState.likedIds.contains(id) },
        onPhotoClick = onPhotoClick,
        onToggleLike = viewModel::toggleLike,
        onLikedListClick = onLikedListClick,
        onLoadMore = viewModel::loadNextPage,
        onRetry = viewModel::retry,
    )
}

@Composable
private fun PhotoListScreenContent(
    photos: List<Photo>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    isLikeId: (String) -> Boolean = { false },
    onPhotoClick: (Photo) -> Unit = {},
    onToggleLike: (Photo) -> Unit = {},
    onLikedListClick: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    val listState = rememberLazyListState()

    // 스크롤이 마지막 아이템 근처에 있는가
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            0 < total && total - 2 <= lastVisible
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore }
            .collect { if (it) onLoadMore() }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp),
                text = "Unsplash Images",
                fontSize = 20.sp
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),
                onClick = onLikedListClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_heart),
                    modifier = Modifier.align(Alignment.Center),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .align(Alignment.BottomCenter)
            )
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .background(Color(0xfff5f5f5)),
            contentPadding = PaddingValues(12.dp),
        ) {
            items(items = photos, key = { it.id }) { photo ->
                PhotoListItem(
                    photo = photo,
                    isLiked = isLikeId(photo.id),
                    imageUrl = photo.url.orEmpty(),
                    onClick = { onPhotoClick(photo) },
                    onToggleLike = { onToggleLike(photo) },
                    modifier = Modifier.padding(vertical = 6.dp),
                )
            }

            when {
                isLoading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                        )
                        Button(
                            modifier = Modifier.padding(top = 12.dp),
                            onClick = onRetry,
                        ) {
                            Text(text = "다시 시도")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoListItem(
    photo: Photo,
    imageUrl: String,
    isLiked: Boolean,
    onClick: () -> Unit,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                model = imageUrl.takeIf { it.isNotBlank() },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                },
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 12.dp),
                text = photo.user?.name ?: photo.id,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onToggleLike,
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_heart),
                    modifier = Modifier.align(Alignment.Center),
                    contentDescription = null,
                    tint = if (isLiked) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoListScreenPreview() {
    val sample = List(2) { i ->
        Photo(
            id = "id_$i",
            url = "https://example.com/$i.jpg",
            user = PhotoUser(name = "작가 $i"),
            width = 3,
            height = 2,
        )
    }
    PhotoListScreenContent(
        photos = sample,
        isLikeId = { false },
        isLoading = true,
    )
}
