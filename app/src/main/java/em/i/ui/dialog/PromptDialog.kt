package em.i.ui.dialog

import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.TextView
import em.i.R
import kotlinx.android.synthetic.main.dialog_prompt.*
import kotlinx.android.synthetic.main.dialog_prompt.view.*
import kotlinx.android.synthetic.main.fragment_photo.*

class PromptDialog: DialogFragment() {

    var listener: ((url: String) -> Unit) = { _ -> }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let { context ->
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialog = inflater.inflate(R.layout.dialog_prompt, null)

            with (dialog) {
                builder.setView(this)
                button_paste.setOnClickListener { pasteFromClipboard(context) }
                button_add.setOnClickListener { handleAdd() }
                button_cancel.setOnClickListener { dismiss() }
            }

            return builder.create()
        } ?: throw IllegalStateException()
    }

    private fun handleAdd() {
        val url = dialog.text.text.toString()
        listener.invoke(url)
    }

    private fun pasteFromClipboard(context: Context) {
        val text = takeValueFromClipboard(context)
        if (!TextUtils.isEmpty(text)) {
            dialog.label.setText(text)
            dialog.button_add.isEnabled = true
        }
    }

    val label by lazy { dialog.findViewById<TextView>(R.id.label) }

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