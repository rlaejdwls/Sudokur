package kr.co.treegames.sudokur.task.board

import kr.co.treegames.sudokur.data.model.Data
import kr.co.treegames.sudokur.task.BasePresenter
import kr.co.treegames.sudokur.task.BaseView

interface BoardContract {
    interface View: BaseView<Presenter> {
        fun update(col: Int, row: Int, data: Data)
        fun setLoadingIndicator(isShow: Boolean)
        fun showMessage(message: String)
    }
    interface Presenter: BasePresenter {
        fun update(col: Int, row: Int, number: Int)
        fun sample()
        fun thinking()
        fun clear()
    }
}