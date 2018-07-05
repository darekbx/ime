package em.i.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Size

class ImageUtils {

    fun zoomAndPanImage(pathToImage: String, containerSize: Size, destinationSize: Size, panX: Int, panY: Int): Bitmap? {
        if (pathToImage == null) {
            return null
        }

        val originalSize = computeImageSize(pathToImage)
        val optionsInSample = applyInSampleSize(originalSize, destinationSize)
        val inSampleBitmap = BitmapFactory.decodeFile(pathToImage, optionsInSample)

        if (inSampleBitmap == null) {
            return null
        }

        val scaledBitmap = Bitmap.createScaledBitmap(inSampleBitmap, destinationSize.width, destinationSize.height, true)

        val x = Math.max(0, ((scaledBitmap.width - containerSize.width) / 2) + panX)
        val y = Math.max(0, ((scaledBitmap.height - containerSize.height) / 2) + panY)

        var targetOutWidth = 0
        var targetOutHeight = 0

        if (destinationSize.width > containerSize.width) {
            targetOutWidth = containerSize.width
        }else {
            targetOutWidth = destinationSize.width
        }

        targetOutHeight = Math.min(destinationSize.height, containerSize.height)

        Log.v("--------------", "Scaled bitmap: ${scaledBitmap.width} / ${scaledBitmap.height}")
        Log.v("--------------", "TargetOut : $targetOutWidth / $targetOutHeight")
        Log.v("--------------", "Offset : $x / $y")
        Log.v("--------------", "Pan : $panX / $panY")

        val outBitmap = Bitmap.createBitmap(scaledBitmap, x, y, targetOutWidth, targetOutHeight)

        Log.v("--------------", "New image: ${outBitmap.width} / ${outBitmap.height}")
        return outBitmap
    }

    fun computeImageSize(uri: String): Size {
        val optionsForBounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        with(optionsForBounds) {
            BitmapFactory.decodeFile(uri, this)
            return Size(outWidth, outHeight)
        }
    }

    fun prepareBitmap(viewSize: Size, pathToImage: String?): Bitmap? {
        if (pathToImage == null) {
            return null
        }

        val originalSize = computeImageSize(pathToImage)
        val optionsInSample = applyInSampleSize(originalSize, viewSize)
        val inSampleBitmap = BitmapFactory.decodeFile(pathToImage, optionsInSample)

        if (inSampleBitmap == null) {
            return null
        }

        val aspectRatio = inSampleBitmap.getWidth().toFloat() / inSampleBitmap.getHeight().toFloat()
        val targetHeight = Math.round(viewSize.width / aspectRatio)

        return Bitmap.createScaledBitmap(inSampleBitmap, viewSize.width, targetHeight, true)
    }

    private fun applyInSampleSize(bitmapSize: Size, viewSize: Size): BitmapFactory.Options {
        var calculatedInSampleSize = 1

        if (bitmapSize.height > viewSize.height || bitmapSize.width > viewSize.width) {
            val halfHeight = bitmapSize.height / 2
            val halfWidth = bitmapSize.width / 2
            while ((halfHeight / calculatedInSampleSize) >= viewSize.height
                    && (halfWidth / calculatedInSampleSize) >= viewSize.width) {
                calculatedInSampleSize *= 2
            }
        }

        return BitmapFactory.Options().apply { inSampleSize = calculatedInSampleSize }
    }
}