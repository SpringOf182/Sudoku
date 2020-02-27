package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    Grid[][] record = new Grid[9][9];
    int rest = 81;
    int lastR=8;
    int lastC=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*int[][] test = {
                {0, 8, 0, 0, 0, 0, 6, 0, 0},
                {0, 0, 0, 4, 0, 0, 0, 0, 9},
                {0, 7, 0, 0, 0, 0, 8, 0, 5},
                {4, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 3, 0, 0, 6, 0, 0, 9, 0},
                {0, 0, 0, 7, 2, 0, 1, 0, 0},
                {0, 9, 3, 2, 0, 0, 0, 6, 4},
                {8, 1, 0, 3, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 5, 0, 0, 0},//
                /*{0, 0, 0, 0, 6, 3, 5, 0, 2},
                {0, 0, 0, 7, 0, 0, 0, 0, 0},
                {0, 0, 7, 2, 1, 5, 0, 0, 9},
                {6, 1, 0, 0, 2, 0, 8, 0, 7},
                {0, 0, 2, 0, 0, 0, 0, 3, 0},
                {0, 0, 5, 0, 8, 0, 6, 0, 0},
                {9, 0, 0, 0, 0, 0, 0, 0, 5},
                {0, 0, 4, 0, 0, 0, 0, 8, 0},
                {0, 0, 6, 0, 0, 0, 7, 0, 0},//
                {1, 0, 0, 0, 0, 4, 3, 0, 0},
                {0, 0, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 3, 0, 5, 0, 0, 1, 6},
                {0, 0, 0, 8, 0, 0, 1, 0, 3},
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {7, 5, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 2, 3, 0, 0, 0, 9, 5},
                {0, 0, 9, 4, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 6, 0, 8, 0},//
                {0, 5, 0, 0, 0, 0, 0, 2, 0},
                {4, 0, 0, 2, 0, 6, 0, 0, 7},
                {0, 0, 8, 0, 3, 0, 1, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 6, 0},
                {0, 0, 9, 0, 0, 0, 5, 0, 0},
                {0, 7, 0, 0, 0, 0, 0, 9, 0},
                {0, 0, 5, 0, 8, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {0, 2, 0, 0, 0, 0, 0, 7, 0},//
                {0, 0, 0, 0, 8, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 8, 7, 9},
                {0, 9, 0, 7, 0, 6, 0, 0, 0},
                {2, 0, 5, 0, 3, 7, 0, 6, 0},
                {0, 0, 7, 4, 1, 0, 2, 9, 0},
                {3, 4, 0, 0, 0, 8, 5, 1, 0},
                {0, 3, 0, 0, 7, 1, 0, 0, 6},
                {0, 6, 0, 8, 4, 2, 0, 3, 5},
                {5, 7, 4, 6, 9, 0, 1, 0, 0},
        };*/
        String question="006074090" +
                "009800000" +
                "300200800" +
                "000000070" +
                "057680000" +
                "080000200" +
                "108042600" +
                "000130000" +
                "400000050";
        start(question);
        print();
    }

    public String start(String question) {
        //Log.e("1","start()被调用");
        //initialize(question);
        toGrid(question);

        uniqueSolution();//填写符合唯一余数法的格子
        if (rest != 0) {
            lastR=lastNull()[0];
            lastC=lastNull()[1];
            backtrack(firstNull()[0],firstNull()[1]);
        }

        return transToString();
    }

    //将int数组初始化为Grid数组
    /*public void initialize(int[][] question) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                record[i][j] = new Grid(question[i][j]);
                if (record[i][j].getFillingNumber() != 0)
                    rest--;
            }
        }
        //print();
    }*/

    //将字符串转化为Grid数组
    public void toGrid(String originQuestion){
        int index=0;
        if(originQuestion.length()==81){
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    int temp=Integer.parseInt(originQuestion.substring(index,index+1));
                    if(temp!=0)
                        rest--;
                    record[i][j]=new Grid(temp);
                    index++;
                }
            }
        }
        else{
            Log.e("ERROR!!!!","字符串位数不对！！！");
        }
    }

    public void uniqueSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int number = record[i][j].getFillingNumber();
                if (number != 0) {
                    findOnly(number, i, j);//消除同行列宫格其他格子的对应候选数
                    //judgeUnique(i,j);//检验同行、列、宫格是否有某一候选数仅保留于单个格子
                }
            }
        }
        //initializeJudgeUnique();
    }

    /*public void initializeJudgeUnique() {
        //判断各行中是否有某一候选数仅保留于单个格子
        for (int i = 0; i < 9; i++) {
            rowJudge(i);
        }
        //判断各列中是否有某一候选数仅保留于单个格子
        for (int i = 0; i < 9; i++) {
            colJudge(i);
        }
        //判断各宫格中是否有某一候选数仅保留于单个格子
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                blockJudge(i, j);
            }
        }
    }*/

    public void judgeUnique(int row, int column) {
        rowJudge(row);
        colJudge(column);
        blockJudge(row, column);
    }

    public void rowJudge(int row) {
        int[][] markRow = new int[9][9];//用来标记的数组，i为该行第（i-1）个格子，每一行为改格子的状态，及mark数组
        for (int i = 0; i < 9; i++) {
            markRow[i] = record[row][i].getStatus();
        }
        for (int j = 0; j < 9; j++) {
            int alternativeNum = 0;//用来标记包含(j+1)这个候选数的格子数
            int alternativeIndex = 0;
            for (int i = 0; i < 9; i++) {
                if (markRow[i][j] != 0) {
                    alternativeNum++;
                    alternativeIndex = i;
                }
            }
            if (alternativeNum == 1 && fillVerify(row, alternativeIndex, j + 1)) {
                fillAndJudge(row, alternativeIndex, j + 1);
                markRow[alternativeIndex] = record[row][alternativeIndex].getStatus();
            }
        }
    }

    public void colJudge(int column) {
        int[][] markCol = new int[9][9];//用来标记的数组，i为该列第（i-1）个格子，每一行为该格子的状态，及mark数组
        for (int i = 0; i < 9; i++) {
            markCol[i] = record[i][column].getStatus();
        }
        for (int j = 0; j < 9; j++) {
            int alternativeNum = 0;//用来标记包含(j+1)这个候选数的格子数
            int alternativeIndex = 0;
            for (int i = 0; i < 9; i++) {
                if (markCol[i][j] != 0) {
                    alternativeNum++;
                    alternativeIndex = i;
                }
            }
            if (alternativeNum == 1 && fillVerify(alternativeIndex, column, j + 1)) {
                fillAndJudge(alternativeIndex, column, j + 1);
                markCol[alternativeIndex] = record[alternativeIndex][column].getStatus();
            }
        }
    }

    public void blockJudge(int row, int column) {
        int startX = row / 3 * 3;
        int startY = column / 3 * 3;
        int[][] markBlock = new int[9][9];
        int markIndex = 0;
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                markBlock[markIndex] = record[i][j].getStatus();
                markIndex++;
            }
        }
        for (int j = 0; j < 9; j++) {
            int alternativeNum = 0;//用来标记包含(j+1)这个候选数的格子数
            int alternativeIndex = 0;
            for (int i = 0; i < 9; i++) {
                if (markBlock[i][j] != 0) {
                    alternativeNum++;
                    alternativeIndex = i;
                }
            }
            if (alternativeNum == 1) {
                int rIndex = alternativeIndex / 3 + startX;
                int cIndex = alternativeIndex % 3 + startY;
                if (fillVerify(rIndex, cIndex, j + 1)) {
                    fillAndJudge(rIndex, cIndex, j + 1);
                    markBlock[alternativeIndex] = record[rIndex][cIndex].getStatus();
                }
            }
        }
    }

    public void findOnly(int number, int row, int column) {
        //Log.e("findOnly——","("+Integer.toString(row)+","+Integer.toString(column)+")关于"+Integer.toString(number)+"的标记");
        //找到同列中其他坐标
        for (int i = 0; i < 9; i++) {
            alterAndJudge(i, column, number);
            //Log.e("findOnly","("+Integer.toString(i)+","+Integer.toString(column)+")不可以填"+Integer.toString(number));
        }
        //找到同行中其他坐标
        for (int j = 0; j < 9; j++) {
            alterAndJudge(row, j, number);
            //Log.e("findOnly","("+Integer.toString(row)+","+Integer.toString(j)+")不可以填"+Integer.toString(number));
        }
        //找到同格子中其它坐标
        for (int i = row / 3 * 3; i < (row / 3 + 1) * 3; i++) {
            for (int j = column / 3 * 3; j < (column / 3 + 1) * 3; j++) {
                if (i != row && j != column) {
                    alterAndJudge(i, j, number);
                    //Log.e("findOnly","("+Integer.toString(i)+","+Integer.toString(j)+")不可以填"+Integer.toString(number));
                }
            }
        }
    }

    public void alterAndJudge(int i, int j, int number) {
        if (record[i][j].getFillingNumber() == 0) {
            int feedback = record[i][j].alter(number);
            if (feedback != 0) {
                rest--;
                Log.e("3", "——坐标：（" + Integer.toString(i) + "," + Integer.toString(j) + "),填入：" + Integer.toString(feedback));
                findOnly(feedback, i, j);
                judgeUnique(i, j);
            }
        }
    }

    public void fillAndJudge(int i, int j, int number) {
        record[i][j].fill(number);
        rest--;
        findOnly(number, i, j);
        judgeUnique(i, j);
    }

    public boolean fillVerify(int row, int column, int number) {
        //
        for (int i = 0; i < 9; i++) {
            if (i!=row&&record[i][column].getFillingNumber() == number)
                return false;
        }
        //找到同行中其他坐标
        for (int j = 0; j < 9; j++) {
            if (j!=column&&record[row][j].getFillingNumber() == number)
                return false;
        }
        //找到同格子中其它坐标
        for (int i = row / 3 * 3; i < (row / 3 + 1) * 3; i++) {
            for (int j = column / 3 * 3; j < (column / 3 + 1) * 3; j++) {
                if (i != row && j != column) {
                    if (i!=row&&j!=column&&record[i][j].getFillingNumber() == number)
                        return false;
                }
            }
        }
        return true;
    }

    public boolean backtrack(int row,int column){
        int nowFilled=record[row][column].getNext();
        while (nowFilled!=0){
            boolean judge=fillVerify(row,column,nowFilled);
            if(row==lastR&&column==lastC&&judge)
                return true;
            if(judge) {
                int nextR=nextNullGrid(row,column)[0];
                int nextC=nextNullGrid(row,column)[1];
                boolean res = backtrack(nextR,nextC);
                if(res)
                    return true;
                else
                    record[nextR][nextC].goBack();
            }
            nowFilled=record[row][column].getNext();
        }
        return false;
    }

    public int[] nextNullGrid(int row,int column) {
        int[] next=new int[2];
        for(int j=column+1;j<9;j++){
            if(record[row][j].getFillingNumber()==0) {
                next[0] = row;
                next[1] = j;
                return next;
            }
        }
        for(int i=row+1;i<9;i++){
            for(int j=0;j<9;j++){
                if(record[i][j].getFillingNumber()==0) {
                    next[0] = i;
                    next[1] = j;
                    return next;
                }
            }
        }
        return next;
    }

    public int[] firstNull(){
        int first[]=new int[2];
        for(int i=0;i<9;i++){
            for(int j=0;i<9;j++){
                if(record[i][j].getFillingNumber()==0) {
                    first[0] = i;
                    first[1] = j;
                    return first;
                }
            }
        }
        return first;
    }

    public int[] lastNull(){
        int[] last=new int[2];
        for(int i=8;i>=0;i--){
            for(int j=8;j>=0;j--){
                if(record[i][j].getFillingNumber()==0) {
                    last[0] = i;
                    last[1] = j;
                    return last;
                }
            }
        }
        return last;
    }

    public String transToString(){
        String result="";
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                result +=Integer.toString(record[i][j].getFillingNumber());
            }
        }
        return result;
    }

    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Log.e("record", "（" + Integer.toString(i) + "," + Integer.toString(j) + ")：" + Integer.toString(record[i][j].getFillingNumber()));
                if (record[i][j].getFillingNumber() == 0)
                    record[i][j].showStatus();
            }
            Log.e("change", "换行");
        }
    }
}