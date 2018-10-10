package kr.co.treegames.sudokur.widget.dialog.bundle.progress.solving

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.airbnb.lottie.LottieDrawable
import kotlinx.android.synthetic.main.dialog_solving_progress_bar.*
import kr.co.treegames.sudokur.R

/**
 * Created by Hwang on 2018-10-10.
 *
 * Description :
 */
class SolveProgressDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_solving_progress_bar)
        window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        anim_progress.repeatCount = LottieDrawable.INFINITE
        anim_progress.playAnimation()

        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setOnDismissListener {
            anim_progress.cancelAnimation()
        }
    }
}