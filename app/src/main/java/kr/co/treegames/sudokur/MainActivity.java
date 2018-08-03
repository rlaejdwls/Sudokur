package kr.co.treegames.sudokur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Hwang on 2018-07-25.
 *
 * Description :
 */
public class MainActivity extends AppCompatActivity {
    public static final int BOARD_ROW = 9;
    public static final int BOARD_COLUMN = 9;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.btn_thinking).setOnClickListener((v) -> {
            for (int column = 0; column < temp.length; column++) {
                for (int row = 0; row < temp[column].length; row++) {
                    Data data = new Data();
                    data.answer = temp[column][row];
                    if (data.answer != 0) {
                        data.isFixed = true;
                    }
                    board[column][row] = data;
                }
            }
            Log.d(this.getClass().getName(), "-");
            for (int column = 0; column < board.length; column++) {
                StringBuilder line = new StringBuilder();
                for (int row = 0; row < board[column].length; row++) {
                    line.append(board[column][row].isFixed ? board[column][row].answer : " ").append(" ");
                }
                Log.d(this.getClass().getName(), line.toString());
            }
        });
    }
}
