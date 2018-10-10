package kr.co.treegames.sudokur.task.board

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_board.*
import kr.co.treegames.core.AppCore
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.data.model.Data
import kr.co.treegames.sudokur.task.DefaultFragment
import kr.co.treegames.sudokur.task.board.widget.Cell

class BoardFragment: DefaultFragment(), BoardContract.View {
    private val percentage: Float = 0.90f

    override lateinit var presenter: BoardContract.Presenter

    private val board = Array(9) { arrayOfNulls<Cell>(9) }

    private val onClick = fun(view: View) {
        when (view.id) {
            R.id.btn_sample -> presenter.sample()
            R.id.btn_action -> presenter.thinking()
            R.id.btn_clear -> presenter.clear()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_sample.setOnClickListener(onClick)
        btn_action.setOnClickListener(onClick)
        btn_clear.setOnClickListener(onClick)

        activity?.run {
            val with: Float = AppCore.getScreenWidth().toFloat()
            val cellWith: Float = (with * percentage / 9) - 2

            for (col in 0..8) {
                val layout_column = LinearLayout(this)
                layout_column.orientation = LinearLayout.HORIZONTAL
                layout_column.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                layout_column.gravity = Gravity.CENTER
                for (row in 0..8) {
                    val cell = Cell(this)
                    cell.column = col
                    cell.row = row
                    cell.setOnNumberChangeListener { col, row, number -> presenter.update(col, row, number) }
                    cell.layoutParams = LinearLayout.LayoutParams(cellWith.toInt() - 1, cellWith.toInt() - 1).apply {
                        this.leftMargin = 1
                        this.rightMargin = 1
                        this.topMargin = 1
                        this.bottomMargin = 1
                    }
                    cell.setBackgroundColor(Color.argb(255, 100, 100, 100))
                    cell.gravity = Gravity.CENTER
                    board[col][row] = cell
                    layout_column.addView(cell)
                }
                layout_board.addView(layout_column)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        presenter.start()
    }
    override fun update(col: Int, row: Int, data: Data) {
        Handler(Looper.getMainLooper()).post {
            if (data.isFixed) {
                board[col][row]?.run {
                    number = (data.answer - 1)
                    text = data.answer.toString()
                    invalidate()
                }
            } else {
                board[col][row]?.run {
                    number = -1
                    text = ""
                    invalidate()
                }
            }
        }
    }
    override fun showMessage(message: String) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(activity, message, Toast.LENGTH_LONG).show() }
    }
}