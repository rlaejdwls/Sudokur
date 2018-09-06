package kr.co.treegames.sudokur.data.model

/**
 * Created by Hwang on 2018-09-05.
 *
 * Description :
 */
interface Key {
    interface Intent {
        companion object {
            const val DATA: String = "DATA"
        }
    }
    interface SharedPreferences {
        companion object {
            const val DEFAULT_NAME: String = "default_name"
            const val UUID: String = "UUID"
        }
    }
}