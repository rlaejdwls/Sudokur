package kr.co.treegames.sudokur.data.source.io

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import kr.co.treegames.sudokur.data.model.Key
import kr.co.treegames.sudokur.data.source.SharedPreferencesDataSource

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class SharedPreferencesLocalDataSource(private val preferences: SharedPreferences): SharedPreferencesDataSource {
    companion object {
        private var INSTANCE: SharedPreferencesLocalDataSource? = null

        @JvmStatic fun getInstance(context: Context): SharedPreferencesLocalDataSource {
            return INSTANCE ?: synchronized(SharedPreferencesLocalDataSource::javaClass) {
                SharedPreferencesLocalDataSource(context.getSharedPreferences(Key.SharedPreferences.DEFAULT_NAME,
                        Activity.MODE_PRIVATE)).apply { INSTANCE = this }
            }
        }
        @VisibleForTesting fun clearInstance() {
            INSTANCE = null
        }
    }
    override fun put(key: String, strValue: String?, intValue: Int?, boolValue: Boolean?) {
        with(preferences.edit()) {
            strValue?.let { putString(key, strValue) }
            intValue?.let { putInt(key, intValue) }
            boolValue?.let { putBoolean(key, boolValue) }
            apply()
        }
    }
    override fun getString(key: String): String? {
        return preferences.getString(key, null)
    }
}