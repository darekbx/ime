package em.i.ui

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import em.i.R
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File

class PhotoFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val arguments = PhotoFragmentArgs.fromBundle(it)
            image_view.setImageURI(Uri.fromFile(File(arguments.photo_path)))
        }

        bindControls()
    }

    fun bindControls() {
        arrow_up.setOnClickListener(this)
        arrow_down.setOnClickListener(this)
        arrow_left.setOnClickListener(this)
        arrow_right.setOnClickListener(this)
        zoom_in.setOnClickListener(this)
        zoom_out.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        view?.let { view ->
            when (view.id) {
                R.id.arrow_up -> { }
                R.id.arrow_down -> { }
                R.id.arrow_right -> { }
                R.id.arrow_left -> { }
                R.id.zoom_in -> { }
                R.id.zoom_out -> { }
            }
        }
    }
}