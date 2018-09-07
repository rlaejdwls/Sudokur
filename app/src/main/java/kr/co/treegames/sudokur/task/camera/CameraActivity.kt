package kr.co.treegames.sudokur.task.camera

import android.os.Bundle
import android.view.WindowManager
import kr.co.treegames.sudokur.R
import kr.co.treegames.sudokur.task.DefaultActivity
import kr.co.treegames.sudokur.task.DefaultFragment
import kr.co.treegames.sudokur.task.replaceFragmentInActivity

/**
 * Created by Hwang on 2018-09-06.
 *
 * Description :
 */
class CameraActivity: DefaultActivity() {
    private lateinit var presenter: CameraContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_camera)

        val fragment: CameraFragment = supportFragmentManager.findFragmentById(R.id.content) as CameraFragment?
                ?: DefaultFragment.create(CameraFragment::class.java).also {
                    replaceFragmentInActivity(it, R.id.content)
                }

        presenter = CameraPresenter(fragment)
    }
}