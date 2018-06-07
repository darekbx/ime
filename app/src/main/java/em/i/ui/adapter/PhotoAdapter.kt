package em.i.ui.adapter

import android.content.Context
import android.databinding.BindingAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import em.i.R
import em.i.databinding.AdapterPhotoBinding

@BindingAdapter("app:imageUri")
fun setImageUri(view: ImageView, uri:String?) {
    uri?.let {

        Log.v("-------------", uri)
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