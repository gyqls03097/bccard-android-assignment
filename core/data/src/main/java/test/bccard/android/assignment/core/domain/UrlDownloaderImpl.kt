package test.bccard.android.assignment.core.domain

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import test.bccard.android.assignment.core.domain.repository.UrlDownloader

class UrlDownloaderImpl(
    private val context: Context,
) : UrlDownloader {

    override fun download(url: String, fileName: String) {
        val request = DownloadManager.Request(url.toUri())
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}