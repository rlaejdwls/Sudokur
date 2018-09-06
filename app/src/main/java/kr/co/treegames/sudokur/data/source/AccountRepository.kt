package kr.co.treegames.sudokur.data.source

import android.support.annotation.VisibleForTesting
import kr.co.treegames.sudokur.data.model.Account
import kr.co.treegames.sudokur.data.model.User

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
class AccountRepository(private val remote: AccountDataSource): AccountDataSource {
    companion object {
        private var INSTANCE: AccountRepository? = null

        @JvmStatic fun getInstance(remote: AccountDataSource): AccountRepository {
            return INSTANCE ?: synchronized(AccountRepository::javaClass) {
                AccountRepository(remote).apply { INSTANCE = this }
            }
        }
        @VisibleForTesting fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun automatic(success: (User?) -> Unit, failure: (Int, String?) -> Unit) {
        remote.automatic(success, failure)
    }
    override fun signIn(account: Account?, success: (User?) -> Unit, failure: (Int, String?) -> Unit) {
        remote.signIn(account, success, failure)
    }
    override fun signUp(account: Account?, success: (User?) -> Unit, failure: (Int, String?) -> Unit) {
        remote.signUp(account, success, failure)
    }
    override fun signOut() {
        remote.signOut()
    }
}