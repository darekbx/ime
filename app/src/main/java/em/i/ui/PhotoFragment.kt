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

class PhotoFragment : Fragment() {

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

    }
}
