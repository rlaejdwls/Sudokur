package kr.co.treegames.core.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import kr.co.treegames.core.R;

public class CrashReportDialog extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_crash_report);

        String errorContent = "";
        if (getIntent().getStringExtra("errorContent") != null) {
            errorContent = getIntent().getStringExtra("errorContent");
        }

        ((TextView) findViewById(R.id.lbl_title)).setText(getResources().getString(R.string.core_error_dialog_title));
        ((TextView) findViewById(R.id.lbl_error_content)).setText(errorContent);
    }

    public void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        finish();
        startActivity(i);
    }
}