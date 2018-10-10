package kr.co.treegames.sudokur.task.board

import kr.co.treegames.sudokur.data.model.Data
import java.util.concurrent.atomic.AtomicBoolean

class BoardPresenter(val view: BoardContract.View)
    : BoardContract.Presenter {
    enum class LoopResult {
        NONE,
        BREAK_OUTSIDE,
        BREAK_INSIDE,
        CONTINUE
    }

    private val board = Array(9) { arrayOfNulls<Data>(9) }
    private var temp = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )

    init {
        view.presenter = this
    }
    override fun start() {
        initialize()
        updateHint()
        print()
    }

    override fun update(col: Int, row: Int, number: Int) {
        board[col][row]?.run {
            answer = number
            isFixed = true
            hint = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
    }
    override fun sample() {
        temp = arrayOf(
                intArrayOf(0, 8, 0, 0, 0, 0, 2, 0, 0),
                intArrayOf(0, 1, 2, 0, 8, 0, 0, 3, 4),
                intArrayOf(0, 3, 6, 2, 5, 9, 0, 1, 0),
                intArrayOf(0, 9, 0, 8, 0, 0, 0, 0, 5),
                intArrayOf(0, 0, 0, 5, 3, 6, 0, 0, 0),
                intArrayOf(1, 0, 0, 0, 0, 4, 0, 8, 0),
                intArrayOf(0, 6, 0, 4, 9, 8, 3, 5, 0),
                intArrayOf(3, 5, 0, 0, 6, 0, 1, 4, 0),
                intArrayOf(0, 0, 4, 0, 0, 0, 0, 6, 0)
        )
        initialize()
        updateHint()
        print()
    }
    override fun clear() {
        temp = arrayOf(
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
        initialize()
        updateHint()
        print()
    }
    override fun thinking() {
        Thread {
            updateHint()
            while (true) {
                if (algorithmOnlyOneInsideBoard() && algorithmOnlyOneInsideBlock()) break
                if (checkSuccess()) break
            }
            print()
        }.start()
    }

    private fun initialize() {
        loopBoard { column, row ->
            val data = Data()
            data.answer = temp[column][row]
            if (data.answer != 0) {
                data.isFixed = true
            }
            board[column][row] = data
            board[column][row]?.let {
                if (!it.isFixed) {
                    it.hint = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
                }
            }
            return@loopBoard LoopResult.NONE
        }
    }
    private fun updateHint() {
        loopBoard { column, row ->
            board[column][row]?.run {
                if (isFixed) {
                    val number = answer
                    loopBox(column, row) { subCol, subRow ->
                        board[subCol][subRow]?.let { it.hint[number - 1] = 0 }
                        return@loopBox LoopResult.NONE
                    }

                    for (subRow in 0 until 9) {
                        board[column][subRow]?.let { it.hint[number - 1] = 0 }
                    }
                    for (subCol in 0 until 9) {
                        board[subCol][row]?.let { it.hint[number - 1] = 0 }
                    }
                }
            }
            return@loopBoard LoopResult.NONE
        }
    }
    /**
     * 보드의 모든 힌트 중에 유일하게 하나만 대입 가능할 경우 답변을 확정하는 함수
     * @return
     */
    private fun algorithmOnlyOneInsideBoard(): Boolean {
        val result = AtomicBoolean(true)
        loopBoard { column, row ->
            board[column][row]?.run {
                if (!isFixed) {
                    var count = 0
                    for (index in 0 until hint.size) {
                        if (hint[index] == 0) {
                            count += 1
                        }
                    }
                    if (count == 8) {
                        for (index in 0 until hint.size) {
                            if (hint[index] > 0) {
                                answer = hint[index]
                                isFixed = true
                                hint = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
                                updateHint()
                                result.set(false)
                                return@loopBoard LoopResult.BREAK_OUTSIDE
                            }
                        }
                    }
                }
            }
            return@loopBoard LoopResult.NONE
        }
        return result.get()
    }
    private fun algorithmOnlyOneInsideBlock(): Boolean {
        val result = AtomicBoolean(true)
        loopBoard { column, row ->
            board[column][row]?.run {
                if (!isFixed) {
                    for (index in 0 until hint.size) {
                        val number = hint[index]
                        if (number != 0) {
                            //박스에서 유일하게 하나만 존재할 경우
                            val isOnlyOne = AtomicBoolean(true)
                            loopBox(column, row) { subCol, subRow ->
                                board[subCol][subRow]?.let {
                                    if (!it.isFixed && !(column == subCol && row == subRow)) {
                                        for (subIndex in 0 until it.hint.size) {
                                            if (number == it.hint[subIndex]) {
                                                isOnlyOne.set(false)
                                                return@loopBox LoopResult.BREAK_OUTSIDE
                                            }
                                        }
                                    }
                                }
                                return@loopBox LoopResult.NONE
                            }
                            if (isOnlyOne.get()) {
                                answer = number
                                isFixed = true
                                updateHint()
                                result.set(false)
                                return@loopBoard LoopResult.CONTINUE
                            }
                            //가로 체크 세로 체크 넣어야함
                            //                        isOnlyOne.set(false);
                            //
                            //                        for (int subRow = 0; subRow < BOARD_ROW; subRow++) {
                            //                        }
                            //
                            //                        if (isOnlyOne.get()) {
                            //                            board[column][row].answer = number;
                            //                            board[column][row].isFixed = true;
                            //                            updateHint();
                            //                            return LoopResult.CONTINUE;
                            //                        }
                        }
                    }
                }
            }
            return@loopBoard LoopResult.NONE
        }
        return result.get()
    }
    private fun checkSuccess(): Boolean {
        for (column in 0 until board.size) {
            for (row in 0 until board[column].size) {
                board[column][row]?.run {
                    if (!isFixed) {
                        return false
                    }
                }
            }
        }
        return true
    }
    private fun print() {
        for (column in 0 until board.size) {
            for (row in 0 until board[column].size) {
                board[column][row]?.run {
                    view.update(column, row, this)
                }
//                line.append(if (board[column][row].isFixed) board[column][row].answer else " ").append(" ")
            }
        }
    }

    private fun loopBoard(action: (col: Int, row: Int) -> BoardPresenter.LoopResult) {
        outside@ for (column in 0 until board.size) {
            inside@ for (row in 0 until board[column].size) {
                when (action(column, row)) {
                    BoardPresenter.LoopResult.CONTINUE -> continue@inside
                    BoardPresenter.LoopResult.BREAK_OUTSIDE -> break@outside
                    BoardPresenter.LoopResult.BREAK_INSIDE -> break@inside
                    else -> {}
                }
            }
        }
    }
    private fun loopBox(column: Int, row: Int, action: (col: Int, row: Int) -> BoardPresenter.LoopResult) {
        val columnStart = column - column % 3
        val rowStart = row - row % 3

        outside@ for (subCol in columnStart until columnStart + 3) {
            inside@ for (subRow in rowStart until rowStart + 3) {
                when (action(subCol, subRow)) {
                    BoardPresenter.LoopResult.CONTINUE -> continue@inside
                    BoardPresenter.LoopResult.BREAK_OUTSIDE -> break@outside
                    BoardPresenter.LoopResult.BREAK_INSIDE -> break@inside
                    else -> {}
                }
            }
        }
    }
}