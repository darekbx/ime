package em.i.ui.secure

import android.arch.lifecycle.ViewModelProviders
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import em.i.R
import em.i.authentication.Fingerprint

class SecureFragment : Fragment() {

    private lateinit var viewModel: SecureViewModel
    private lateinit var fingerprint: Fingerprint

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.secure_fragment, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        fingerprint.stopListeningAuthentication()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fingerprint = Fingerprint()
        viewModel = ViewModelProviders.of(this).get(SecureViewModel::class.java)

        context?.let { context ->
            fingerprint.listenForFingerprint(context, object : FingerprintManager.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                    openPreviewAndClear()
                }
            })
        }
    }

    fun openPreviewAndClear() {
        val clearTaskOption = NavOptions.Builder().setClearTask(true).build()
        findNavController().navigate(R.id.action_secureFragment_to_previewFragment,
                null, clearTaskOption)
    }
}
