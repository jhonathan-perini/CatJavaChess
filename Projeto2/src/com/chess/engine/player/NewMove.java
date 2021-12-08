package com.chess.engine.player;

import com.chess.engine.board.Board;

public class NewMove {

    private final Board toBoard;
    private final CurrentMove currentMove;

    public NewMove(final Board toBoard,
                   final CurrentMove currentMove) {
        this.toBoard = toBoard;
        this.currentMove = currentMove;
    }

    public Board getToBoard() {
        return this.toBoard;
    }

    public CurrentMove getMoveStatus() {
        return this.currentMove;
    }
}

