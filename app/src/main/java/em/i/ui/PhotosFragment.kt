package em.i.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import em.i.R
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
                R.id.action_add_photo -> {
                    val dialog = PromptDialog()
                    dialog.show(this.fragmentManager!!, "PromptDialog::class.java.simpleName")
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
