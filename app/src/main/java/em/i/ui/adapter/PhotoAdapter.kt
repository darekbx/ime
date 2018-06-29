package em.i.ui.adapter

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import em.i.R
import em.i.databinding.AdapterPhotoBinding
import android.util.Size
import em.i.utils.ImageUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

@BindingAdapter("app:imageUri")
fun setImageUri(view: ImageView, uri:String?) {
    val viewSize = Size(view.width, view.height)
    uri?.let {
        launch(UI) {
            var scaledBitmap: Bitmap? = null
            async(CommonPool) { scaledBitmap = ImageUtils().prepareBitmap(viewSize, uri) }.await()
            scaledBitmap?.let { view.setImageBitmap(it) }
        }
    }
}

class PhotoAdapter(context: Context) : ArrayAdapter<String>(context, R.layout.adapter_photo) {

    val inflater by lazy { LayoutInflater.from(context) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) =
            when (convertView) {
                null -> AdapterPhotoBinding.inflate(inflater, parent, false)
                else -> DataBindingUtil.bind(convertView)!!
            }
                    .apply { uri = getItem(position) }
                    .root
}