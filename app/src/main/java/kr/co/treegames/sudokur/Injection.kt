package kr.co.treegames.sudokur

import android.content.Context
import kr.co.treegames.sudokur.data.source.AccountRepository
import kr.co.treegames.sudokur.data.source.SharedPreferencesRepository
import kr.co.treegames.sudokur.data.source.io.SharedPreferencesLocalDataSource
import kr.co.treegames.sudokur.data.source.remote.AccountRemoteDataSource

/**
 * Created by Hwang on 2018-09-04.
 *
 * Description :
 */
object Injection {
    fun provideAccountRepository(): AccountRepository {
        return AccountRepository.getInstance(AccountRemoteDataSource.getInstance())
    }
    fun provideSharedPreferences(context: Context): SharedPreferencesRepository {
        return SharedPreferencesRepository.getInstance(SharedPreferencesLocalDataSource.getInstance(context))
    }
}