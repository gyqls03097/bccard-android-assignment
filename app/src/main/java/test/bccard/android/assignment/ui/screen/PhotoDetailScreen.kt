package test.bccard.android.assignment.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import test.bccard.android.assignment.R
import test.bccard.android.assignment.comm.HttpImage
import test.bccard.android.assignment.domain.model.Photo
import test.bccard.android.assignment.domain.model.PhotoDetail
import test.bccard.android.assignment.domain.model.PhotoExif
import test.bccard.android.assignment.domain.model.PhotoLocation
import test.bccard.android.assignment.domain.model.PhotoUser
import test.bccard.android.assignment.ui.viewmodel.PhotoDetailViewModel


@Composable
fun PhotoDetailScreen(
    photo: Photo,
    viewModel: PhotoDetailViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PhotoDetailScreenContent(
        photo = photo,
        photoDetail = uiState.photoDetail,
        modifier = modifier,
        isLiked = uiState.isLiked,
        isLoading = uiState.isLoading,
        error = uiState.error,
        isLocationDraw = viewModel::isPhotoLocationDraw,
        isExifDraw = viewModel::isPhotoExifDraw,
        onBackClick = onBackClick,
        onLikeClick = viewModel::toggleLike,
    )
}

@Composable
private fun PhotoDetailScreenContent(
    photo: Photo,
    photoDetail: PhotoDetail?,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    isLoading: Boolean = false,
    error: String? = null,
    isLocationDraw: (PhotoLocation?) -> Boolean = { true },
    isExifDraw: (PhotoExif?) -> Boolean = { true },
    onBackClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp),
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_back),
                    modifier = Modifier.align(Alignment.Center),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp),
                text = "Unsplash Images",
                fontSize = 20.sp
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),
                onClick = onLikeClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_heart),
                    modifier = Modifier.align(Alignment.Center),
                    contentDescription = null,
                    tint = if (isLiked) Color.Red else Color.Gray
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .aspectRatio((photo.width.toFloat() / photo.height.toFloat()))
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    if (photo.urlDetail == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                        )
                    } else {
                        HttpImage(
                            modifier = Modifier.fillMaxSize(),
                            url = photo.urlDetail ?: ""
                        )
                    }
                }
            }

            photo.user?.let { user -> item { PhotoUserItem(user) } }

            if (photoDetail != null) {
                item {
                    PhotoCountItem(photoDetail.views, photoDetail.downloads)
                }

                if (isExifDraw(photoDetail.exif)) {
                    item {
                        PhotoInfoItem(
                            make = photoDetail.exif?.make,
                            model = photoDetail.exif?.model,
                            name = photoDetail.exif?.name,
                            exposureTime = photoDetail.exif?.exposureTime,
                            aperture = photoDetail.exif?.aperture,
                            focalLength = photoDetail.exif?.focalLength,
                            iso = photoDetail.exif?.iso,
                        )
                    }
                }

                if (isLocationDraw(photoDetail.location)) {
                    item {
                        PhotoLocationItem(
                            photoDetail.location?.name,
                            photoDetail.location?.city,
                            photoDetail.location?.country,
                            photoDetail.location?.latitude,
                            photoDetail.location?.longitude,
                        )
                    }
                }

                if (photoDetail.tags.isNotEmpty()) {
                    item { PhotoTagItem(photoDetail.tags) }
                }
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
                            fontSize = 14.sp,
                            color = Color.Gray,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.size(100.dp))
            }
        }
    }
}

@Composable
private fun PhotoTitleItem(title: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .align(Alignment.TopCenter)
        )
        Text(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp),
            text = title,
            fontSize = 12.sp,
            color = Color.LightGray,
            fontWeight = FontWeight(600)
        )
    }
}

@Composable
private fun PhotoBodyItem(title: String, text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 32.dp),
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight(600)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp),
            text = text,
            fontSize = 14.sp,
            color = Color.Black,
        )
    }
}

@Composable
private fun PhotoUserItem(
    user: PhotoUser
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp)),
        ) {
            if (user.profileImageUrl == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            } else {
                HttpImage(
                    modifier = Modifier.fillMaxSize(),
                    url = user.profileImageUrl ?: ""
                )
            }
        }
        val name = user.name ?: user.username ?: "(이름없음)"
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 12.dp),
            text = name,
            fontSize = 16.sp,
            color = Color.Black,
        )
    }
}

@Composable
private fun PhotoCountItem(
    views: Int,
    downloads: Int,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PhotoBodyItem("views", "$views")
        PhotoBodyItem("downloads", "$downloads")
        Spacer(modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun PhotoInfoItem(
    make: String? = null,
    model: String? = null,
    name: String? = null,
    exposureTime: String? = null,
    aperture: String? = null,
    focalLength: String? = null,
    iso: Int? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PhotoTitleItem("기본정보")
        make?.let { PhotoBodyItem("make", it) }
        model?.let { PhotoBodyItem("model", it) }
        name?.let { PhotoBodyItem("name", it) }
        exposureTime?.let { PhotoBodyItem("exposureTime", it) }
        aperture?.let { PhotoBodyItem("aperture", it) }
        focalLength?.let { PhotoBodyItem("focalLength", it) }
        iso?.let { PhotoBodyItem("iso", "$it") }
        Spacer(modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun PhotoLocationItem(
    name: String? = null,
    city: String? = null,
    country: String? = null,
    latitude: Double? = null,
    longitude: Double? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PhotoTitleItem("위치정보")
        name?.let { PhotoBodyItem("name", it) }
        city?.let { PhotoBodyItem("city", it) }
        country?.let { PhotoBodyItem("country", it) }
        latitude?.let { PhotoBodyItem("latitude", "$it") }
        longitude?.let { PhotoBodyItem("longitude", "$it") }
        Spacer(modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun PhotoTagItem(
    list: List<String>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PhotoTitleItem("태그")
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 32.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            list.forEach { tag ->
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    text = tag,
                    fontSize = 14.sp,
                    color = Color.Black,
                )
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoDetailScreenPreview() {
    PhotoDetailScreenContent(
        Photo(
            id = "id",
            user = PhotoUser(
                id = "user id",
                username = "user username",
                name = "user name",
                bio = "user bio",
                profileImageUrl = "user profileImageUrl",
            ),
            width = 1000,
            height = 500
        ),
        PhotoDetail(
            views = 1004,
            downloads = 1004,
            exif = PhotoExif(
                make = "exif make",
                model = "exif model",
                name = "exif name",
                exposureTime = "exif exposureTime",
                aperture = "exif aperture",
                focalLength = "exif focalLength",
                iso = 1004,
            ),
            location = PhotoLocation(
                name = "location name",
                city = "location city",
                country = "location country",
                latitude = 37.0,
                longitude = 128.0,
            ),
            tags = listOf("tag0, tag1"),
        )
    )
}
