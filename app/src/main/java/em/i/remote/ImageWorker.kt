package em.i.remote

import android.text.TextUtils
import androidx.work.Worker

class ImageWorker: Worker() {

    companion object {
        val URL_KEY = "url_key"
    }

    override fun doWork(): WorkerResult {

        val url = inputData.getString(URL_KEY, "")

        if (TextUtils.isEmpty(url)) {
            return WorkerResult.FAILURE
        }

        Thread.sleep(3000)

        return WorkerResult.SUCCESS
    }
}
