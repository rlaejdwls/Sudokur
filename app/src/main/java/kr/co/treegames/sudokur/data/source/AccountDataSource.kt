package kr.co.treegames.sudokur.data.source

import kr.co.treegames.sudokur.data.model.Account
import kr.co.treegames.sudokur.data.model.User

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
interface AccountDataSource {
    fun automatic(success: (User?) -> Unit, failure: (Int, String?) -> Unit)
    fun signIn(account: Account? = null, success: (User?) -> Unit, failure: (Int, String?) -> Unit)
    fun signUp(account: Account? = null, success: (User?) -> Unit, failure: (Int, String?) -> Unit)
    fun signOut()
}