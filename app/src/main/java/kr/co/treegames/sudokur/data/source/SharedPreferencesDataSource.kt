package kr.co.treegames.sudokur.data.source

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
interface SharedPreferencesDataSource {
    fun put(key: String, strValue: String? = null, intValue: Int? = null, boolValue: Boolean? = null)
    fun getString(key: String): String?
}