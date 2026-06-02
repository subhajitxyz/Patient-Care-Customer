package com.real.patientcare.domain.repo

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class VideoDownloader(

    private val context: Context
) {

    suspend fun downloadVideo(
        url: String,
        eventId: String
    ): String = withContext(Dispatchers.IO) {

        val directory = File(context.cacheDir, "videos")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$eventId.mp4")

        val connection = URL(url).openConnection()
        connection.connect()

        val input = connection.getInputStream()
        val output = FileOutputStream(file)

        input.copyTo(output)

        output.close()
        input.close()

        file.absolutePath
    }
}