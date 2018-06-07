package em.i.ui

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.work.*
import androidx.work.ktx.OneTimeWorkRequestBuilder
import em.i.R
import em.i.remote.ImageWorker
import em.i.ui.adapter.PhotoAdapter
import em.i.ui.dialog.PromptDialog
import kotlinx.android.synthetic.main.fragment_photos.*
import java.io.File
import java.util.*

class PhotosFragment : Fragment() {

    companion object {
        val FILE_PREFIX = "ime_"
        val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val PERMISSION_REQUEST_CODE = 1
    }

    lateinit var adapter: PhotoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_add_photo -> checkPermissions()
            }
            return@setOnNavigationItemSelectedListener true
        }

        initAdapter(view)
        refreshList()
    }

    private fun initAdapter(view: View) {
        adapter = PhotoAdapter(view.context)
        list.adapter = adapter
    }

    private fun refreshList() {
        context?.let { context ->

            context.dataDir.list()
                    .filter { it.substring(0, 4) == FILE_PREFIX }
                    .map { File(context.dataDir, it).absolutePath }
                    .map { adapter.add(it) }

            adapter.notifyDataSetChanged()
        }
    }

    private fun startWork(url: String) {
        val dataIn = createInputData(url)
        val work = OneTimeWorkRequestBuilder<ImageWorker>()
                .setInputData(dataIn)
                .build()
        with(WorkManager.getInstance()) {
            enqueue(work)
            this.getStatusById(work.id).observe(this@PhotosFragment, Observer { status ->
                status?.let { status -> handleState(status.state) }
            })
        }
    }

    private fun handleState(state:State) {
        when (state) {
            State.SUCCEEDED -> refreshList()
            State.FAILED -> Toast.makeText(activity, state.name, Toast.LENGTH_SHORT)
            else -> { }
        }
    }

    private fun createInputData(url: String): Data {
        val fileUuid = UUID.randomUUID().toString()
        val file = File(context?.dataDir, FILE_PREFIX + fileUuid).absolutePath
        return Data
                .Builder()
                .putString(ImageWorker.URL_KEY, url)
                .putString(ImageWorker.OUT_FILE_KEY, file)
                .build()
    }

    private fun checkPermissions() {
        context?.let { context ->
            val result = ContextCompat.checkSelfPermission(context, WRITE_PERMISSION)
            when (result) {
                PackageManager.PERMISSION_GRANTED -> openPrompt()
                else -> requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        activity?.let { activity ->
            ActivityCompat.requestPermissions(activity, arrayOf(WRITE_PERMISSION), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPrompt()
                }
            }
        }
    }

    private fun openPrompt() {
        val dialog = PromptDialog()
        with(dialog) {
            listener = { url -> startWork(url) }
            show(this@PhotosFragment.fragmentManager, PromptDialog::class.java.simpleName)
        }
    }
}
