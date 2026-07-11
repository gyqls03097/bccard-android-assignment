package test.bccard.android.assignment.data

import android.content.Context
import androidx.core.content.edit

class Pref(context: Context) {

    private val pref = context.applicationContext.getSharedPreferences("localPref", Context.MODE_PRIVATE)

    var accessKey: String
        get() = pref.getString("ACCESS_KEY", "") ?: ""
        set(value) = run { pref.edit { putString("ACCESS_KEY", value) } }
}