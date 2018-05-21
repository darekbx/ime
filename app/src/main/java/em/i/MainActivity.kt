package em.i

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import em.i.ui.secure.SecureFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SecureFragment.newInstance())
                    .commitNow()
        }
    }

}
