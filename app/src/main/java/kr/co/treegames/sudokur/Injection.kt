package kr.co.treegames.sudokur

import android.content.Context
import android.os.Bundle
import kr.co.treegames.sudokur.data.source.AccountRepository
import kr.co.treegames.sudokur.data.source.SharedPreferencesRepository
import kr.co.treegames.sudokur.data.source.io.SharedPreferencesLocalDataSource
import kr.co.treegames.sudokur.data.source.remote.AccountRemoteDataSource
import kr.co.treegames.sudokur.task.DefaultFragment

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
    fun <T: DefaultFragment> provideFragment(clazz: Class<T>): T {
        return clazz.newInstance()
    }
    fun <T: DefaultFragment> provideFragment(clazz: Class<T>, params: Bundle?): T? {
        val fragment: T? = clazz.newInstance()
        params?.let {
            fragment?.arguments
        }
        return fragment
    }
}