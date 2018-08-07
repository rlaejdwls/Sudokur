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
        initialize();
        print();

        this.findViewById(R.id.btn_thinking).setOnClickListener((v) -> {
            thinking();
        });
    }
    public void initialize() {
        for (int column = 0; column < temp.length; column++) {
            for (int row = 0; row < temp[column].length; row++) {
                Data data = new Data();
                data.answer = temp[column][row];
                if (data.answer != 0) {
                    data.isFixed = true;
                }
                board[column][row] = data;

                if (!board[column][row].isFixed) {
                    board[column][row].hint = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                }
            }
        }
    }
    public void thinking() {
        new Thread(() -> {
            while (true) {
                updateHint();
                if (algorithmOnlyOne()) break;
                if (checkSuccess()) break;
            }
            Log.d("DEBUG", "STOP");
            print();
        }).start();
    }
    public void updateHint() {
        for (int column = 0; column < board.length; column++) {
            for (int row = 0; row < board[column].length; row++) {
                if (board[column][row].isFixed) {
                    int number = board[column][row].answer;
                    int columnStart = column - (column % 3);
                    int rowStart = row - (row % 3);

                    for (int subCol = columnStart; subCol < columnStart + 3; subCol++) {
                        for (int subRow = rowStart; subRow < rowStart + 3; subRow++) {
                            board[subCol][subRow].hint[number - 1] = 0;
                        }
                    }
                    for (int subRow = 0; subRow < BOARD_ROW; subRow++) {
                        board[column][subRow].hint[number - 1] = 0;
                    }
                    for (int subCol = 0; subCol < BOARD_COLUMN; subCol++) {
                        board[subCol][row].hint[number - 1] = 0;
                    }
                }
            }
        }
    }
    public boolean algorithmOnlyOne() {
        for (int column = 0; column < board.length; column++) {
            for (int row = 0; row < board[column].length; row++) {
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
                            board[column][row].hint = new int[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
        Log.d(this.getClass().getName(), "-");
        Log.d(this.getClass().getName(), line.toString());
    }
}
