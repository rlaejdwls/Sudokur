package kr.co.treegames.sudokur.task.splash

import kr.co.treegames.sudokur.data.model.Key
import kr.co.treegames.sudokur.data.source.AccountRepository
import kr.co.treegames.sudokur.data.source.SharedPreferencesDataSource

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class SplashPresenter(private val preferences: SharedPreferencesDataSource,
                      private val repository: AccountRepository,
                      val view: SplashContract.View)
    : SplashContract.Presenter {
    init {
        view.presenter = this
    }

    override fun start() {
    }
    override fun automatic() {
        view.setLoadingIndicator(true)
        repository.automatic({
            preferences.put(Key.SharedPreferences.UUID, it?.id)
            view.setLoadingIndicator(false)
            view.startMainActivity()
        }, { code, message ->
            view.showMessage("code:$code:message:$message")
            view.setLoadingIndicator(false)
            view.startAccountActivity()
        })
    }
}