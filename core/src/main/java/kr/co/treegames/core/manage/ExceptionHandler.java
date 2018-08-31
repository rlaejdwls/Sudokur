package kr.co.treegames.core.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import kr.co.treegames.core.widget.CrashReportDialog;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Hwang on 2016-10-15.
 * 작성자 : 황의택
 * 내용 : Application 범위에서 Exception을 관리하고 핸들링하는 클래스
 * 참고 : ACRA 라이브러리로 대체 가능
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context context;

    public ExceptionHandler(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            String LINE_SEPARATOR = "\n";
            StringWriter stackTrace = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTrace));
            StringBuilder errorContent = new StringBuilder();
            errorContent.append("************ CAUSE OF ERROR ************");
            errorContent.append(LINE_SEPARATOR);
            errorContent.append(stackTrace.toString());
            errorContent.append("************ CAUSE OF ERROR ************");
            errorContent.append(LINE_SEPARATOR);
            StringBuilder causeOfError = new StringBuilder();
            causeOfError.append(stackTrace.toString());

            errorContent.append(LINE_SEPARATOR);
            errorContent.append("************ DEVICE INFORMATION ***********");
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Brand: ");
            errorContent.append(Build.BRAND);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Device: ");
            errorContent.append(Build.DEVICE);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Model: ");
            errorContent.append(Build.MODEL);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Id: ");
            errorContent.append(Build.ID);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Product: ");
            errorContent.append(Build.PRODUCT);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("************ DEVICE INFORMATION ***********");
            errorContent.append(LINE_SEPARATOR);
            StringBuilder deviceInformation = new StringBuilder();
            errorContent.append("Brand: ");
            deviceInformation.append(Build.BRAND);
            deviceInformation.append(LINE_SEPARATOR);
            deviceInformation.append("Device: ");
            deviceInformation.append(Build.DEVICE);
            deviceInformation.append(LINE_SEPARATOR);
            deviceInformation.append("Model: ");
            deviceInformation.append(Build.MODEL);
            deviceInformation.append(LINE_SEPARATOR);
            deviceInformation.append("Id: ");
            deviceInformation.append(Build.ID);
            deviceInformation.append(LINE_SEPARATOR);
            deviceInformation.append("Product: ");
            deviceInformation.append(Build.PRODUCT);

            errorContent.append(LINE_SEPARATOR);
            errorContent.append("************ FIRMWARE OF ANDROID ************");
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("SDK: ");
            errorContent.append(Build.VERSION.SDK_INT);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Release: ");
            errorContent.append(Build.VERSION.RELEASE);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("Incremental: ");
            errorContent.append(Build.VERSION.INCREMENTAL);
            errorContent.append(LINE_SEPARATOR);
            errorContent.append("************ FIRMWARE OF ANDROID ************");
            errorContent.append(LINE_SEPARATOR);
            StringBuilder firmwareOfAndroid = new StringBuilder();
            firmwareOfAndroid.append("SDK: ");
            firmwareOfAndroid.append(Build.VERSION.SDK_INT);
            firmwareOfAndroid.append(LINE_SEPARATOR);
            firmwareOfAndroid.append("Release: ");
            firmwareOfAndroid.append(Build.VERSION.RELEASE);
            firmwareOfAndroid.append(LINE_SEPARATOR);
            firmwareOfAndroid.append("Incremental: ");
            firmwareOfAndroid.append(Build.VERSION.INCREMENTAL);

            Logger.printStackTrace(errorContent.toString(), ex);
            notifyDialog(errorContent, causeOfError, deviceInformation, firmwareOfAndroid);
            System.exit(1);
        } catch (Exception e) {
            Logger.e("Exception Logger Failed!", e);
        }
    }

    private void notifyDialog(StringBuilder errorContent, StringBuilder causeOfError,
            StringBuilder deviceInformation, StringBuilder firmwareOfAndroid) {
        try {
            context.startActivity(new Intent(context, CrashReportDialog.class)
                    .putExtra("errorContent", errorContent.toString())
                    .putExtra("causeOfError", causeOfError.toString())
                    .putExtra("deviceInformation", deviceInformation.toString())
                    .putExtra("firmwareOfAndroid", firmwareOfAndroid.toString())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
