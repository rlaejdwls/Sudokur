package kr.co.treegames.sudokur.service.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.treegames.core.manage.Logger

/**
 * Created by Hwang on 2018-09-03.
 *
 * Description :
 */
class PushMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Logger.d(remoteMessage?.data.toString())
    }
}