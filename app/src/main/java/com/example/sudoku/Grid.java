package com.example.sudoku;

import android.util.Log;

public class Grid {
    private int markNum;
    private int[] mark = {1, 1, 1, 1, 1, 1, 1, 1, 1};
    private int fillingNumber;
    private int current = -1;

    public Grid() {
        markNum = 9;
        fillingNumber = 0;
    }

    //
    public Grid(int num) {
        if (num == 0) {
            markNum = 9;
            fillingNumber = 0;
        } else {
            fillingNumber = num;
            markNum = 0;
            for (int i = 0; i < 9; i++) {
                mark[i] = 0;
            }
        }
    }

    public int getFillingNumber() {
        return fillingNumber;
    }

    //修改当前格子状态，当被标记数仅剩1时找到余下的索引，填入数字为索引+1，返回当前fillingNumber
    public int alter(int index) {
        if (mark[index - 1] != 0) {
            mark[index - 1] = 0;
            markNum--;
        }
        if (markNum == 1) {
            for (int i = 0; i < mark.length; i++) {
                if (mark[i] == 1) {
                    fill(i + 1);
                    break;
                }
            }
        }
        return fillingNumber;
    }

    public void fill(int number) {
        fillingNumber = number;
        markNum = 0;
        for (int i = 0; i < 9; i++) {
            mark[i] = 0;
        }
    }

    //回溯法求剩余解时,获取下一个可能解
    public int getNext() {
        for (int i = current + 1; i < 9; i++) {
            if (mark[i] == 1) {
                current = i;
                fillingNumber = i + 1;
                return fillingNumber;
            }
        }
        return 0;
    }
    public void goBack(){
        current=-1;
        fillingNumber=0;
    }

    public int[] getStatus() {
        return mark;
    }

    public void showStatus() {
        Log.e("alter Test:", Integer.toString(mark[0]) + " " + Integer.toString(mark[1]) + " " + Integer.toString(mark[2]) + " " + Integer.toString(mark[3]) + " " + Integer.toString(mark[4]) + " " + Integer.toString(mark[5]) + " " + Integer.toString(mark[6]) + " " + Integer.toString(mark[7]) + " " + Integer.toString(mark[8]));
        return;
    }
}
