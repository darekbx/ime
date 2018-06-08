package em.i.ui.adapter

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import em.i.R
import em.i.databinding.AdapterPhotoBinding
import android.os.SystemClock
import android.util.Size

@BindingAdapter("app:imageUri")
fun setImageUri(view: ImageView, uri:String?) {
    uri?.let {

        val start = SystemClock.currentThreadTimeMillis()

        val viewSize = Size(view.width, view.height)

        val optionsForBounds = BitmapFactory.Options()
        optionsForBounds.inJustDecodeBounds = true
        BitmapFactory.decodeFile(uri, optionsForBounds)

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
        val inSampleBitmap = BitmapFactory.decodeFile(uri, optionsInSample)

        val aspectRatio = inSampleBitmap.getWidth().toFloat() / inSampleBitmap.getHeight().toFloat()
        val targetHeight = Math.round(viewSize.width / aspectRatio)

        val scaledBitmap = Bitmap.createScaledBitmap(inSampleBitmap, viewSize.width, targetHeight, true)

        val end = SystemClock.currentThreadTimeMillis()

        Log.v("--------------", "View: ${viewSize.width} / ${viewSize.height}")
        Log.v("--------------", "Bitmap: ${bitmapSize.width} / ${bitmapSize.height}")
        Log.v("--------------", "InSampleSize: $calculatedInSampleSize")
        Log.v("--------------", "Sampled bitmap: ${inSampleBitmap.width} / ${inSampleBitmap.height}")
        Log.v("--------------", "Scaled bitmap: ${scaledBitmap.width} / ${scaledBitmap.height}")
        Log.v("--------------", "In: ${end - start}ms")

        view.setImageBitmap(scaledBitmap)
    }
}

class PhotoAdapter(context: Context) : ArrayAdapter<String>(context, R.layout.adapter_photo) {

    val inflater by lazy { LayoutInflater.from(context) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) =
            when (convertView) {
                null -> AdapterPhotoBinding.inflate(inflater, parent, false)
                else -> AdapterPhotoBinding.bind(convertView)
            }
                    .apply { uri = getItem(position) }
                    .root
}