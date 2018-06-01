package em.i.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.ktx.OneTimeWorkRequestBuilder
import em.i.R
import em.i.remote.ImageWorker
import em.i.ui.dialog.PromptDialog
import kotlinx.android.synthetic.main.fragment_photos.*

class PhotosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_add_photo -> openPrompt()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun startWork(url: String) {
        val dataIn = Data.Builder().putString(ImageWorker.URL_KEY, url).build()
        val work = OneTimeWorkRequestBuilder<ImageWorker>()
                .setInputData(dataIn)
                .build()
        with(WorkManager.getInstance()) {
            enqueue(work)
            this.getStatusById(work.id).observe(this@PhotosFragment, Observer { status ->
                Toast.makeText(context, "$status", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun openPrompt() {
        val dialog = PromptDialog()
        with(dialog) {
            listener = { url -> startWork(url) }
            show(fragmentManager, PromptDialog::class.java.simpleName)
        }
    }
}
