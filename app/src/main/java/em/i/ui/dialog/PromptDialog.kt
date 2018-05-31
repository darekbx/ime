package em.i.ui.dialog

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.work.WorkManager
import androidx.work.ktx.OneTimeWorkRequestBuilder
import em.i.R
import em.i.remote.ImageWorker
import kotlinx.android.synthetic.main.dialog_prompt.*
import kotlinx.android.synthetic.main.dialog_prompt.view.*

class PromptDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let { context ->
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialog = inflater.inflate(R.layout.dialog_prompt, null)
            builder.setView(dialog)


            dialog.button_paste.setOnClickListener {
                context?.let { context ->
                    pasteFromClipboard(context)
                }
            }
            dialog.button_add.setOnClickListener {

                val work = OneTimeWorkRequestBuilder<ImageWorker>().build()
                with(WorkManager.getInstance()) {
                    enqueue(work)
                    this.getStatusById(work.id).observe(this@PromptDialog, Observer {  status ->
                        Toast.makeText(context, "$status", Toast.LENGTH_SHORT).show()
                    })
                }
            }
            dialog.button_cancel.setOnClickListener { dismiss() }

            return builder.create()
        } ?: throw IllegalStateException()
    }

    private fun pasteFromClipboard(context: Context) {
        val text = takeValueFromClipboard(context)
        if (!TextUtils.isEmpty(text)) {
            dialog.label.setText(text)
            dialog.button_add.isEnabled = true
        }
    }

    val label by lazy { dialog.findViewById<TextView>(R.id.label) }
    fun buttonPaste(dialog: View) = dialog.findViewById<TextView>(R.id.button_paste)
    fun buttonAdd(dialog: View) = dialog.findViewById<TextView>(R.id.button_add)
    fun buttonCancel(dialog: View) = dialog.findViewById<TextView>(R.id.button_cancel)

    private fun takeValueFromClipboard(context: Context): CharSequence? {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipboardManager.primaryClip
                .takeIf { it.itemCount > 0 }
                .let { primaryClip ->
                    val value = primaryClip?.getItemAt(0)
                    value?.let { value -> return value.text }
                }
    }
}