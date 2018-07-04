package em.i.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import em.i.ImeApplication
import em.i.R
import em.i.viewmodels.PhotoViewModel
import em.i.views.PhotoManipulator
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File
import javax.inject.Inject

class PhotoFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as ImeApplication).appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[PhotoViewModel::class.java]
    }

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
                R.id.arrow_up -> viewModel.doAction(PhotoManipulator.Action.UP)
                R.id.arrow_down -> viewModel.doAction(PhotoManipulator.Action.DOWN)
                R.id.arrow_right -> viewModel.doAction(PhotoManipulator.Action.RIGHT)
                R.id.arrow_left -> viewModel.doAction(PhotoManipulator.Action.LEFT)
                R.id.zoom_in -> viewModel.doAction(PhotoManipulator.Action.ZOOM_IN)
                R.id.zoom_out -> viewModel.doAction(PhotoManipulator.Action.ZOOM_OUT)
            }
        }
    }
}