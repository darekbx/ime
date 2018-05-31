package em.i.remote

import androidx.work.Worker

class ImageWorker: Worker() {

    override fun doWork(): WorkerResult {

        Thread.sleep(3000)

        return WorkerResult.SUCCESS
    }
}
