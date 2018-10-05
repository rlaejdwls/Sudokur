package kr.co.treegames.sudokur.data.source.remote

import androidx.annotation.VisibleForTesting
import com.google.firebase.auth.FirebaseAuth
import kr.co.treegames.sudokur.data.model.Account
import kr.co.treegames.sudokur.data.model.User
import kr.co.treegames.sudokur.data.source.AccountDataSource

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
class AccountRemoteDataSource(private val auth: FirebaseAuth): AccountDataSource {
    companion object {
        private var INSTANCE: AccountRemoteDataSource? = null

        @JvmStatic fun getInstance(): AccountRemoteDataSource {
            return INSTANCE ?: synchronized(AccountRemoteDataSource::javaClass) {
                AccountRemoteDataSource(FirebaseAuth.getInstance()).apply { INSTANCE = this }
            }
        }
        @VisibleForTesting fun clearInstance() {
            INSTANCE = null
        }
    }
    override fun automatic(success: (User?) -> Unit, failure: (Int, String?) -> Unit) {
        auth.currentUser?.let { User(it.uid, it.email, it.displayName) }?.also(success) ?: failure(-1, "You are not signed in")
    }
    override fun signIn(account: Account?, success: (User?) -> Unit, failure: (Int, String?) -> Unit) {
        account?.let { it ->
            auth.signInWithEmailAndPassword(it.email, it.pwd)
                    .addOnSuccessListener { automatic(success, failure) }
                    .addOnFailureListener { e -> failure(-3, e.message) }
        }
    }
    override fun signUp(account: Account?, success: (User?) -> Unit, failure: (Int, String?) -> Unit) {
        account?.let { it ->
            auth.createUserWithEmailAndPassword(it.email, it.pwd)
                    .addOnSuccessListener { automatic(success, failure) }
                    .addOnFailureListener { e -> failure(-2, e.message) }
        }
    }
    override fun signOut() {
        auth.signOut()
    }
}