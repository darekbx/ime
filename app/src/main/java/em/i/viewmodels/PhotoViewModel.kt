package em.i.viewmodels

import android.arch.lifecycle.ViewModel
import em.i.views.PhotoManipulator

class PhotoViewModel : ViewModel() {

    val photoManipulator = PhotoManipulator()

    fun doAction(action: PhotoManipulator.Action) {


    }
}