package kr.co.treegames.sudokur.data.source

import android.support.annotation.VisibleForTesting
import kr.co.treegames.sudokur.data.source.io.SharedPreferencesLocalDataSource

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class SharedPreferencesRepository(private val local:SharedPreferencesLocalDataSource): SharedPreferencesDataSource {
    companion object {
        private var INSTANCE: SharedPreferencesRepository? = null

        @JvmStatic fun getInstance(local: SharedPreferencesLocalDataSource): SharedPreferencesRepository {
            return INSTANCE ?: synchronized(SharedPreferencesRepository::javaClass) {
                SharedPreferencesRepository(local).apply { INSTANCE = this }
            }
        }
        @VisibleForTesting fun clearInstance() {
            SharedPreferencesRepository.INSTANCE = null
        }
    }
    override fun put(key: String, strValue: String?, intValue: Int?, boolValue: Boolean?) {
        local.put(key, strValue, intValue, boolValue)
    }
    override fun getString(key: String): String? {
        return local.getString(key)
    }
}