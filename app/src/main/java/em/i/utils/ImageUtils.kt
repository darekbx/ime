package em.i.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Size

class ImageUtils {

    fun prepareBitmap(viewSize: Size, uri: String?): Bitmap? {
        val optionsForBounds = BitmapFactory.Options()
        optionsForBounds.inJustDecodeBounds = true
        BitmapFactory.decodeFile(uri, optionsForBounds)

        val optionsInSample = applyInSampleSize(optionsForBounds, viewSize)
        val inSampleBitmap = BitmapFactory.decodeFile(uri, optionsInSample)

        if (inSampleBitmap == null) {
            return null
        }

        val aspectRatio = inSampleBitmap.getWidth().toFloat() / inSampleBitmap.getHeight().toFloat()
        val targetHeight = Math.round(viewSize.width / aspectRatio)

        val scaledBitmap = Bitmap.createScaledBitmap(inSampleBitmap, viewSize.width, targetHeight, true)
        return scaledBitmap
    }

    private fun applyInSampleSize(optionsForBounds: BitmapFactory.Options, viewSize: Size): BitmapFactory.Options {
        val bitmapSize = Size(optionsForBounds.outWidth, optionsForBounds.outHeight)
        var calculatedInSampleSize = 1

        if (bitmapSize.height > viewSize.height || bitmapSize.width > viewSize.width) {
            val halfHeight = bitmapSize.height / 2
            val halfWidth = bitmapSize.width / 2
            while ((halfHeight / calculatedInSampleSize) >= viewSize.height && (halfWidth / calculatedInSampleSize) >= viewSize.width) {
                calculatedInSampleSize *= 2
            }
        }

        val optionsInSample = BitmapFactory.Options().apply { inSampleSize = calculatedInSampleSize }
        return optionsInSample
    }
}