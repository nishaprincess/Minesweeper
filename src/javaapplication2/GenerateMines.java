/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.Scanner;

/**
 *
 * @author acer
 */public class GenerateMines {
    
     
   
// all 100 of the board pieces values (0=no bomb near 1=1 bomb near, etc)
    int[][] board = new int[10][10];

    public GenerateMines() {
        //this.<error> = s.nextInt();
        int temp;
        int row, column;

        final int NO_MINE = 0;
        final int BOMB_VALUE = 99;
        final int NUM_OF_BLOCKS = 100;
        final int NUM_OF_MINES = 10;

        for (int j = 0; j < NUM_OF_MINES; j++) {
            do {
                temp = (int) (Math.random() * NUM_OF_BLOCKS);
                row = temp / 10;
                column = temp % 10;
            } while (board[row][column] == BOMB_VALUE);
            board[row][column] = BOMB_VALUE;
        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y <10; y++) {
                if (board[x][y] >= BOMB_VALUE) {
                    if (y != 9)// East
                        board[x][y + 1] += 1;
                    if (y != 0)// West
                        board[x][y - 1] += 1;
                    if (x != 0)// North
                        board[x - 1][y] = board[x - 1][y] + 1;
                    if (x != 9)// South
                        board[x + 1][y] = board[x + 1][y] + 1;
                    if (y != 9 && x != 0)// N.E.
                        board[x - 1][y + 1] += 1;
                    if (x != 0 && y != 0)// N.W.
                        board[x - 1][y - 1] += 1;
                    if (x != 9 && y != 0)// S.W.
                        board[x + 1][y - 1] += 1;
                    if (x != 9 && y != 9)// S.E.
                        board[x + 1][y + 1] += 1;
                }
            }
        }
    }

    public int getMinePos(int r, int c) {// sends value of each piece to main
        return (board[r][c]);
    }

    
}