package em.i.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import em.i.ImeApplication
import em.i.R
import em.i.repository.local.InformationsStore
import kotlinx.android.synthetic.main.fragment_item.*
import javax.inject.Inject

class InformationsFragment : Fragment() {

    @Inject
    lateinit var informationsStore: InformationsStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as ImeApplication).appComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        informations.setText(informationsStore.read())
    }

    override fun onPause() {
        super.onPause()
        informationsStore.save(informations.text.toString())
    }
}
