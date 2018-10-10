package kr.co.treegames.sudokur.widget.dialog.bundle.progress.solving

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * Created by Hwang on 2018-10-10.
 *
 * Description :
 */
class SolveProgressUIHandler(private val context: Context): Handler(Looper.getMainLooper()) {
    enum class Status {
        SHOW,
        DISMISS
    }
    private var progress: SolveProgressDialog? = null

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            Status.SHOW.ordinal -> {
                progress?.run {
                    if (isShowing) { this.dismiss() }
                }
                progress = SolveProgressDialog(context)
                progress!!.show()
            }
            Status.DISMISS.ordinal -> {
                progress?.run {
                    if (isShowing) { this.dismiss() }
                }
            }
        }
    }
}