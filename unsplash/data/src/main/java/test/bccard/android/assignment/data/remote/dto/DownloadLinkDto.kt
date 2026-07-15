package test.bccard.android.assignment.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DownloadLinkDto(
    @SerialName("url") val url: String? = null,
)
