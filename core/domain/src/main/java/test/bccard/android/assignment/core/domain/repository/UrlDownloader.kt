package test.bccard.android.assignment.core.domain.repository

fun interface UrlDownloader {

    fun download(url: String, fileName: String)
}