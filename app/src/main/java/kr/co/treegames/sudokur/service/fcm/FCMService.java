package kr.co.treegames.sudokur.service.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import kr.co.treegames.core.manage.Logger;

/**
 * Created by Hwang on 2018-08-30.
 *
 * Description :
 */
public class FCMService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Logger.d(remoteMessage.getData().toString());
    }
}
