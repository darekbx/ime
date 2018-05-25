package em.i

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import em.i.ui.InformationsFragment
import em.i.ui.PhotoFragment

class MainActivity : AppCompatActivity(),
        PhotoFragment.OnFragmentInteractionListener,
        InformationsFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onPause() {
        super.onPause()
        finish()
    }
}
