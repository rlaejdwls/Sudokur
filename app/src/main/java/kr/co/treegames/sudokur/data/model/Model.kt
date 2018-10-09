package kr.co.treegames.sudokur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Hwang on 2018-09-04.
 *
 * Description :
 */
data class Root<out E>(val code: Int, val message: String, val data: E)
@Parcelize data class Account(var email: String, var pwd: String): Parcelable
@Parcelize data class User(var id: String, var email: String?, var name: String?): Parcelable
@Parcelize data class Data(var answer: Int = 0, var isFixed: Boolean = false, var hint: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Data

        if (answer != other.answer) return false
        if (isFixed != other.isFixed) return false
        if (!Arrays.equals(hint, other.hint)) return false

        return true
    }
    override fun hashCode(): Int {
        var result = answer
        result = 31 * result + isFixed.hashCode()
        result = 31 * result + Arrays.hashCode(hint)
        return result
    }
}