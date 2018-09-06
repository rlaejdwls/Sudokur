package kr.co.treegames.sudokur.task.main

import android.graphics.Bitmap
import kr.co.treegames.sudokur.data.model.Key
import kr.co.treegames.sudokur.data.source.SharedPreferencesRepository

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
class MainPresenter(private val preferences: SharedPreferencesRepository,
                    val view: MainContract.View)
    : MainContract.Presenter {
    init {
        view.presenter = this
    }

    override fun start() {
        view.showMessage("resume main view:uuid:" + preferences.getString(Key.SharedPreferences.UUID))
    }
}