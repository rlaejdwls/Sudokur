package kr.co.treegames.sudokur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Hwang on 2018-09-04.
 *
 * Description :
 */
data class Root<out E>(val code: Int, val message: String, val data: E)
@Parcelize data class Account(var email: String, var pwd: String): Parcelable
@Parcelize data class User(var id: String, var email: String?, var name: String?): Parcelable