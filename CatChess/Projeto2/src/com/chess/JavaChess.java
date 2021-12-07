package com.chess;

import com.chess.engine.board.Board;
import gui.StartScreen;

public class JavaChess {


    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.println(board);


        StartScreen startScreen = new StartScreen();
    }
}
