package em.i.repository.local

import android.content.Context
import android.util.Base64
import java.nio.charset.Charset

class InformationsStore(context: Context) {

    private val PREFERENCES_KEY = "emi"
    private val PREFERENCES_TEXT_KEY = "informations"

    fun save(text: String) {
        val bytes = text.toByteArray(charset)
        val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
        preferences.edit().putString(PREFERENCES_TEXT_KEY, encoded).apply()
    }

    fun read(): String {
        val encoded = preferences.getString(PREFERENCES_TEXT_KEY, "")
        val bytes = Base64.decode(encoded, Base64.DEFAULT)
        return String(bytes, charset)
    }

    private val preferences by lazy {
        context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    private val charset by lazy {
        Charset.forName("UTF-8")
    }
}