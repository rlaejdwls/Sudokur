package kr.co.treegames.sudokur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

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

    private static final String TAG = "Sudokur";

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

    public interface OnLoopListener {
        LoopResult onLoop(int col, int row);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        updateHint();
        print();

        this.findViewById(R.id.btn_thinking).setOnClickListener((v) -> {
            rollback();
            thinking();
        });
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
    public void thinking() {
        new Thread(() -> {
            while (true) {
                if (algorithmOnlyOneInsideBoard() && algorithmOnlyOneInsideBlock()) break;
                if (checkSuccess()) break;
            }
            Log.d(TAG, "STOP");
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
        Log.d(TAG, "-");
        Log.d(TAG, line.toString());
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
