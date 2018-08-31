package kr.co.treegames.sudokur.task.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.BaseObj;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import kr.co.treegames.core.manage.Debugger;
import kr.co.treegames.core.manage.Logger;
import kr.co.treegames.sudokur.BuildConfig;
import kr.co.treegames.sudokur.R;
import kr.co.treegames.sudokur.manage.analytics.Analytics;
import kr.co.treegames.sudokur.task.test.TestActivity;

/**
 * Created by Hwang on 2018-07-25.
 *
 * Description :
 */
public class MainActivity extends AppCompatActivity {
    public enum LoopResult {
        NONE,
        BREAK_OUTSIDE,
        BREAK_INSIDE,
        CONTINUE
    }

    public static final int BOARD_ROW = 9;
    public static final int BOARD_COLUMN = 9;
    public static final int RC_SIGN_IN = 10001;

    public class Data {
        int answer = 0;
        boolean isFixed = false;
        int[] hint = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    private int[][] temp = new int[][] {
            { 0, 8, 0, 0, 0, 0, 2, 0, 0 },
            { 0, 1, 2, 0, 8, 0, 0, 3, 4 },
            { 0, 3, 6, 2, 5, 9, 0, 1, 0 },
            { 0, 9, 0, 8, 0, 0, 0, 0, 5 },
            { 0, 0, 0, 5, 3, 6, 0, 0, 0 },
            { 1, 0, 0, 0, 0, 4, 0, 8, 0 },
            { 0, 6, 0, 4, 9, 8, 3, 5, 0 },
            { 3, 5, 0, 0, 6, 0, 1, 4, 0 },
            { 0, 0, 4, 0, 0, 0, 0, 6, 0 }
    };
    private Data[][] board = new Data[BOARD_COLUMN][BOARD_ROW];

    public interface OnLoopListener {
        LoopResult onLoop(int col, int row);
    }

    private TextView txtTestData;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser signUpUser;

    private boolean isSignUp = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTestData = findViewById(R.id.txt_test_data);

        //Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Firebase Messaging
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult ->
                Logger.d(instanceIdResult.getToken()));

        initialize();
        updateHint();
        print();

        this.findViewById(R.id.btn_authentication).setOnClickListener(v -> {
            if (!isSignUp) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } else {
                FirebaseAuth.getInstance().signOut();
                updateUI(null);
            }
        });
        this.findViewById(R.id.btn_analytics).setOnClickListener(v -> {
            if (signUpUser != null) {
                Analytics.with("AnalyticsButtonClick")
                        .put("test_param", "TestParameter")
                        .isDebugSend(true)
                        .event();
            } else {
                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
        this.findViewById(R.id.btn_remote_config).setOnClickListener(v -> {
            FirebaseRemoteConfig.getInstance().setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(Debugger.DEBUG)
                    .build());
            FirebaseRemoteConfig.getInstance()
                    .fetch(Debugger.DEBUG ? 0 : 600)
                    .addOnSuccessListener(result -> {
                        FirebaseRemoteConfig.getInstance().activateFetched();
                        txtTestData.setText(FirebaseRemoteConfig.getInstance().getString("remote_config_test"));
                    })
                    .addOnFailureListener(Logger::printStackTrace);
        });
        this.findViewById(R.id.btn_test).setOnClickListener(v -> startActivity(new Intent(this, TestActivity.class)));
        this.findViewById(R.id.btn_crash).setOnClickListener(v -> { throw new RuntimeException("Crash RuntimeException"); });
        this.findViewById(R.id.btn_exception).setOnClickListener(v -> {
            try {
                throw new RuntimeException("Try-Catch RuntimeException");
            } catch (Exception e) {
                Logger.printStackTrace(e);
                Crashlytics.logException(e);
            }
        });
        this.findViewById(R.id.btn_thinking).setOnClickListener(v -> {
            rollback();
            thinking();
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            isSignUp = true;
            FirebaseAnalytics.getInstance(getApplicationContext()).setUserId(user.getUid());
            FirebaseAnalytics.getInstance(getApplicationContext()).setUserProperty("TEST", "Debug");
            signUpUser = user;
            this.findViewById(R.id.btn_analytics).setEnabled(true);
            ((Button) this.findViewById(R.id.btn_authentication)).setText("Sign out");
            String line = "uid:" + user.getUid() + "\n" +
                    "name:" + user.getDisplayName() + "\n" +
                    "email:" + user.getEmail() + "\n";
            txtTestData.setText(line);
        } else {
            isSignUp = false;
            signUpUser = null;
            this.findViewById(R.id.btn_analytics).setEnabled(false);
            ((Button) this.findViewById(R.id.btn_authentication)).setText(R.string.btn_authentication);
        }
    }
    public void rollback() {
        temp = new int[][] {
                { 0, 8, 0, 0, 0, 0, 2, 0, 0 },
                { 0, 1, 2, 0, 8, 0, 0, 3, 4 },
                { 0, 3, 6, 2, 5, 9, 0, 1, 0 },
                { 0, 9, 0, 8, 0, 0, 0, 0, 5 },
                { 0, 0, 0, 5, 3, 6, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 4, 0, 8, 0 },
                { 0, 6, 0, 4, 9, 8, 3, 5, 0 },
                { 3, 5, 0, 0, 6, 0, 1, 4, 0 },
                { 0, 0, 4, 0, 0, 0, 0, 6, 0 }
        };
        initialize();
        updateHint();
    }
    public void initialize() {
        loopBoard((column, row) -> {
            Data data = new Data();
            data.answer = temp[column][row];
            if (data.answer != 0) {
                data.isFixed = true;
            }
            board[column][row] = data;

            if (!board[column][row].isFixed) {
                board[column][row].hint = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            }
            return LoopResult.NONE;
        });
    }
    @AddTrace(name = "thinking", enabled = BuildConfig.PERFORMANCE)
    public void thinking() {
        new Thread(() -> {
            while (true) {
                if (algorithmOnlyOneInsideBoard() && algorithmOnlyOneInsideBlock()) break;
                if (checkSuccess()) break;
            }
            Logger.d("STOP");
            print();
        }).start();
    }
    public void updateHint() {
        loopBoard((column, row) -> {
            if (board[column][row].isFixed) {
                int number = board[column][row].answer;
                loopBox(column, row, (subCol, subRow) -> {
                    board[subCol][subRow].hint[number - 1] = 0;
                    return LoopResult.NONE;
                });

                for (int subRow = 0; subRow < BOARD_ROW; subRow++) {
                    board[column][subRow].hint[number - 1] = 0;
                }
                for (int subCol = 0; subCol < BOARD_COLUMN; subCol++) {
                    board[subCol][row].hint[number - 1] = 0;
                }
            }
            return LoopResult.NONE;
        });
    }
    /**
     * 보드의 모든 힌트 중에 유일하게 하나만 대입 가능할 경우 답변을 확정하는 함수
     * @return
     */
    public boolean algorithmOnlyOneInsideBoard() {
        AtomicBoolean result = new AtomicBoolean(true);
        loopBoard((column, row) -> {
            if (!board[column][row].isFixed) {
                int count = 0;
                for (int index = 0; index < board[column][row].hint.length; index++) {
                    if (board[column][row].hint[index] == 0) {
                        count = count + 1;
                    }
                }
                if (count == 8) {
                    for (int index = 0; index < board[column][row].hint.length; index++) {
                        if (board[column][row].hint[index] > 0) {
                            board[column][row].answer = board[column][row].hint[index];
                            board[column][row].isFixed = true;
                            board[column][row].hint = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
                            updateHint();
                            result.set(false);
                            return LoopResult.BREAK_OUTSIDE;
                        }
                    }
                }
            }
            return LoopResult.NONE;
        });
        return result.get();
    }
    public boolean algorithmOnlyOneInsideBlock() {
        AtomicBoolean result = new AtomicBoolean(true);
        loopBoard((column, row) -> {
            if (!board[column][row].isFixed) {
                for (int index = 0; index < board[column][row].hint.length; index++) {
                    int number = board[column][row].hint[index];
                    if (number != 0) {
                        //박스에서 유일하게 하나만 존재할 경우
                        AtomicBoolean isOnlyOne = new AtomicBoolean(true);
                        loopBox(column, row, (subCol, subRow) ->  {
                            if (!board[subCol][subRow].isFixed && !(column == subCol && row == subRow)) {
                                for (int subIndex = 0; subIndex < board[subCol][subRow].hint.length; subIndex++) {
                                    if (number == board[subCol][subRow].hint[subIndex]) {
                                        isOnlyOne.set(false);
                                        return LoopResult.BREAK_OUTSIDE;
                                    }
                                }
                            }
                            return LoopResult.NONE;
                        });
                        if (isOnlyOne.get()) {
                            board[column][row].answer = number;
                            board[column][row].isFixed = true;
                            updateHint();
                            result.set(false);
                            return LoopResult.CONTINUE;
                        }
                        //가로 체크 세로 체크 넣어야함
//                        isOnlyOne.set(false);
//
//                        for (int subRow = 0; subRow < BOARD_ROW; subRow++) {
//                        }
//
//                        if (isOnlyOne.get()) {
//                            board[column][row].answer = number;
//                            board[column][row].isFixed = true;
//                            updateHint();
//                            return LoopResult.CONTINUE;
//                        }
                    }
                }
            }
            return LoopResult.NONE;
        });
        return result.get();
    }
    public boolean checkSuccess() {
        for (int column = 0; column < board.length; column++) {
            for (int row = 0; row < board[column].length; row++) {
                if (!board[column][row].isFixed) {
                    return false;
                }
            }
        }
        return true;
    }
    public void print() {
        StringBuilder line = new StringBuilder();
        for (int column = 0; column < board.length; column++) {
            for (int row = 0; row < board[column].length; row++) {
                line.append(board[column][row].isFixed ? board[column][row].answer : " ").append(" ");
            }
            line.append("\n");
        }
        txtTestData.setText(line.toString());
        Logger.d(line.insert(0, "\n").toString());
    }
    public void loopBoard(OnLoopListener listener) {
        outside: for (int column = 0; column < board.length; column++) {
            inside: for (int row = 0; row < board[column].length; row++) {
                if (listener != null) {
                    switch (listener.onLoop(column, row)) {
                        case CONTINUE:
                            continue;
                        case BREAK_OUTSIDE:
                            break outside;
                        case BREAK_INSIDE:
                            break inside;
                    }
                }
            }
        }
    }
    public void loopBox(int column, int row, OnLoopListener listener) {
        int columnStart = column - (column % 3);
        int rowStart = row - (row % 3);

        outside: for (int subCol = columnStart; subCol < columnStart + 3; subCol++) {
            inside: for (int subRow = rowStart; subRow < rowStart + 3; subRow++) {
                if (listener != null) {
                    switch (listener.onLoop(subCol, subRow)) {
                        case CONTINUE:
                            continue;
                        case BREAK_OUTSIDE:
                            break outside;
                        case BREAK_INSIDE:
                            break inside;
                    }
                }
            }
        }
    }
}
