package em.i.views

import android.graphics.Bitmap
import android.util.Log
import android.util.Size
import em.i.utils.ImageUtils

class PhotoManipulator {

    enum class Action {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        ZOOM_IN,
        ZOOM_OUT
    }

    private val panStep = 100
    private val zoomStep = 0.1
    private val maxZoom = 2.0
    private val minZoom = 1.0

    var zoom = 1.0
    private var panX = 0
    private var panY = 0

    fun manipulate(uri: String, containerSize: Size, imageSize: Size, action: Action): Bitmap? {
        if (checkPanForDefaultZoom(action)) return null

        when (action) {
            Action.ZOOM_IN -> zoom = Math.min(maxZoom, zoom + zoomStep)
            Action.ZOOM_OUT -> zoom = Math.max(minZoom, zoom - zoomStep)
            Action.LEFT -> panX -= panStep
            Action.RIGHT -> panX += panStep
            Action.UP -> panY -= panStep
            Action.DOWN -> panY += panStep
        }

        val newSize = Size(
                (imageSize.width * zoom).toInt(),
                (imageSize.height * zoom).toInt()
        )

        Log.v("--------------", "Image size: ${imageSize.width} / ${imageSize.height}")
        Log.v("--------------", "Zoom: $zoom")
        return imageUtils.zoomAndPanImage(uri, containerSize, newSize, panX, panY)
    }

    private fun checkPanForDefaultZoom(action: Action) = zoom == 1.0 && action.ordinal < 4

    private val imageUtils by lazy { ImageUtils() }
}