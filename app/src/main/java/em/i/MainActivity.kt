package em.i

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import em.i.ui.InformationsFragment
import em.i.ui.PhotoFragment
import em.i.ui.StatisticsFragment

class MainActivity : AppCompatActivity(),
        PhotoFragment.OnFragmentInteractionListener,
        InformationsFragment.OnFragmentInteractionListener,
        StatisticsFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}
