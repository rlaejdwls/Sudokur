package kr.co.treegames.sudokur.task

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.treegames.core.util.StringUtils

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */
open class DefaultFragment : Fragment() {
    fun getColor(@ColorRes id: Int, theme: Resources.Theme): Int? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.resources?.getColor(id, theme)
        } else {
            activity?.resources?.getColor(id)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = "fragment_" + StringUtils.getAliasWithUnderBar(javaClass.simpleName.replace("Fragment", ""))
        val view: View = inflater.inflate(resources.getIdentifier(layout, "layout", activity?.packageName), container, false)
        return onCreateView(view, inflater, container, savedInstanceState)
    }
    open fun onCreateView(view: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return view
    }
}