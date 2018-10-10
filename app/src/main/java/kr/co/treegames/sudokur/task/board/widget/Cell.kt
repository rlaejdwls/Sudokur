package kr.co.treegames.sudokur.task.board.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import kr.co.treegames.core.AppCore
import kr.co.treegames.sudokur.R

/**
 * Created by Hwang on 2018-10-08.
 *
 * Description :
 */
class Cell(context: Context?) : AppCompatTextView(context) {
    private val start: PointF = PointF()
    private var action: ((col: Int, row: Int, number: Int) -> Unit)? = null
    private var isActionDown = false
    var column: Int = -1
    var row: Int = -1
    var number: Int = -1
    var preview: Int = -1

    private var popupView: View = LayoutInflater.from(context).inflate(R.layout.popup_select_number, null)
    private var popupWindow: PopupWindow

    private var popupViewSize: Int = (AppCore.getDensity() * 50 + 0.5f).toInt()

    init {
        popupWindow = PopupWindow(popupView, popupViewSize, popupViewSize)
        popupWindow.isFocusable = false
    }

    private val paint = Paint().apply {
        color = Color.argb(160, 255, 0, 0)
    }

    fun setOnNumberChangeListener(action: ((col: Int, row: Int, number: Int) -> Unit)?) {
        this.action = action
    }
    private fun getAngle(dx: Double, dy: Double): Double {
        return Math.atan2(dy, dx) * (180.0/Math.PI)
    }
    private fun getNumber(angle:Double): Int {
        if (angle >= -157.5 && angle < -112.5) {
            return 0
        } else if (angle >= -112.5 && angle < -67.5) {
            return 1
        } else if (angle >= -67.5 && angle < -22.5) {
            return 2
        } else if ((angle >= 157.5 && angle <= 180) ||
                (angle >= -180 && angle < -157.5)) {
            return 3
        } else if ((angle >= -22.5 && angle < 0) ||
                (angle >= 0 && angle < 22.5)) {
            return 5
        } else if (angle >= 112.5 && angle < 157.5) {
            return 6
        } else if (angle >= 67.5 && angle < 112.5) {
            return 7
        } else if (angle >= 22.5 && angle < 67.5) {
            return 8
        }
        return -1
    }
    private fun getNumber(event: MotionEvent): Int {
        return if (event.x > 0f && event.y > 0f && event.x < width && event.y < height) {
            4
        } else {
            getNumber(getAngle((event.x - start.x).toDouble(), (event.y - start.y).toDouble()))
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    start.x = event.x
                    start.y = event.y
                    preview = 4
                    isActionDown = true
                    if (popupWindow.isShowing) {
                        popupWindow.dismiss()
                    }
                    popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, left + (width / 2) - (popupViewSize / 2),
                            ((parent as LinearLayout).top + AppCore.getStatusBarHeight()) - popupViewSize - 10)
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    preview = getNumber(event)
                    popupView.findViewById<TextView>(R.id.txt_select_number).text = (number + 1).toString()
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    if (preview != number) {
                        number = getNumber(event)
                        action?.invoke(column, row, number + 1)
                    } else {
                        number = getNumber(event)
                    }
                    text = (number + 1).toString()
                    performClick()
                    isActionDown = false
                    if (popupWindow.isShowing) {
                        popupWindow.dismiss()
                    }
                }
                else -> {
                }
            }
        }
        return super.onTouchEvent(event)
    }
    override fun performClick(): Boolean {
        return super.performClick()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            if (isActionDown) {
                it.drawRect((width / 3.0f) * (preview % 3), (height / 3.0f) * (preview / 3),
                        (width / 3.0f) * (preview % 3 + 1), (height / 3.0f) * (preview / 3 + 1), paint)
            }
        }
    }
}