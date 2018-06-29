package em.i.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.ktx.toWorkData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ImageWorker: Worker() {

    companion object {
        val URL_KEY = "url_key"
        val OUT_FILE_KEY = "out_file_key"
    }

    override fun doWork(): WorkerResult {
        val urlString = inputData.getString(URL_KEY, "")
        val fileString = inputData.getString(OUT_FILE_KEY, "")

        if (TextUtils.isEmpty(urlString)) {
            return WorkerResult.FAILURE
        }

        try {
            val image = downloadImage(urlString)
            val outStream = FileOutputStream(File(fileString))
            outStream.use {
                image.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            outputData = mapOf(OUT_FILE_KEY to fileString).toWorkData()
            return WorkerResult.SUCCESS
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return WorkerResult.FAILURE
        } catch (e: IOException) {
            e.printStackTrace()
            return WorkerResult.FAILURE
        }
    }

    private fun downloadImage(urlString: String): Bitmap {
        val url = URL(urlString)
        with(url.openConnection() as HttpURLConnection) {
            doInput = true
            connect()
            inputStream.use {
                return BitmapFactory.decodeStream(it)
            }
        }
    }
}
