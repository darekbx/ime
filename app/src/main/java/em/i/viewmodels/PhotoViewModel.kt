package em.i.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.util.Size
import em.i.utils.ImageUtils
import em.i.views.PhotoManipulator
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class PhotoViewModel : ViewModel() {

    private val photoManipulator = PhotoManipulator()
    var zoom: MutableLiveData<Double> = MutableLiveData()
    var computedImage: MutableLiveData<Bitmap> = MutableLiveData()

    private lateinit var loadedImageSize: Size
    lateinit var pathToImage: String
    lateinit var containerSize: Size

    fun loadImage() {
        launch(UI) {
            async(CommonPool) {
                val image = ImageUtils().prepareBitmap(containerSize, pathToImage)
                image?.let { image ->
                    loadedImageSize = Size(image.width, image.height)
                    computedImage.postValue(image)
                }
            }
        }
    }

    fun doAction(action: PhotoManipulator.Action) {
        launch(UI) {
            async(CommonPool) {
                val outputImage = photoManipulator.manipulate(pathToImage, containerSize, loadedImageSize, action)
                computedImage.postValue(outputImage)
                zoom.postValue(photoManipulator.zoom)
            }
        }
    }
}