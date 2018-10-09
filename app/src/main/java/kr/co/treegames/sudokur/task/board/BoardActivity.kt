package kr.co.treegames.sudokur.task.board

import android.os.Bundle
import kr.co.treegames.sudokur.Injection
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultActivity
import kr.co.treegames.sudokur.task.replaceFragmentInActivity

class BoardActivity: DefaultActivity() {
    private lateinit var presenter: BoardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        val fragment: BoardFragment = supportFragmentManager.findFragmentById(R.id.content) as BoardFragment?
                ?: Injection.provideFragment(BoardFragment::class.java).also {
                    replaceFragmentInActivity(it, R.id.content)
                }

        presenter = BoardPresenter(fragment)
    }
}