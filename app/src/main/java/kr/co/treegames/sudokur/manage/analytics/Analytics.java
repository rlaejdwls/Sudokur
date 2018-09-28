package kr.co.treegames.sudokur.manage.analytics;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.firebase.analytics.FirebaseAnalytics;

import kr.co.treegames.sudokur.App;
import kr.co.treegames.sudokur.BuildConfig;

/**
 * Created by Hwang on 2018-08-31.
 *
 * Description :
 */
public class Analytics {
    private Builder builder;

    private Analytics(Builder builder) {
        this.builder = builder;
    }
    public static Builder with(String event) {
        return with(App.getApplication().getApplicationContext(), event);
    }
    public static Builder with(Context context, String event) {
        return new Builder(context, event);
    }
    public void event() {
        if (builder != null && (BuildConfig.ANALYTICS || builder.isDebugSend)) {
            if (builder.event == null || builder.event.equals("")) {
                return;
            }
            FirebaseAnalytics.getInstance(builder.context)
                    .logEvent("DEBUGEvent" + builder.event, builder.param);
        }
    }
    public static class Builder {
        private Context context;
        private String event;
        private Bundle param = new Bundle();

        private boolean isDebugSend = false;

        public Builder(Context context, String event) {
            this.context = context;
            this.event = event;
        }
        public Builder put(String key, boolean value) {
            param.putBoolean(key, value);
            return this;
        }
        public Builder put(String key, int value) {
            param.putInt(key, value);
            return this;
        }
        public Builder put(String key, String value) {
            param.putString(key, value);
            return this;
        }
        public Builder put(Bundle value) {
            param.putAll(value);
            return this;
        }
        public <O extends Parcelable> Builder put(String key, O value) {
            param.putParcelable(key, value);
            return this;
        }
        public Builder isDebugSend(boolean isDebugSend) {
            this.isDebugSend = isDebugSend;
            return this;
        }
        public Analytics build() {
            return new Analytics(this);
        }
        public void event() {
            new Analytics(this).event();
        }
    }
}
