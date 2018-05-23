package em.i.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import em.i.R

class OptionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            findViewById<View>(R.id.statistics)?.setOnClickListener {
                view.findNavController().navigate(R.id.optionsToStatistics)
            }
            findViewById<View>(R.id.informations)?.setOnClickListener {
                view.findNavController().navigate(R.id.optionsToInformations)
            }
            findViewById<View>(R.id.photos)?.setOnClickListener {
                view.findNavController().navigate(R.id.optionsToPhotos)
            }
        }
    }
}