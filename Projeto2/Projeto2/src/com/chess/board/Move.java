package com.chess.board;

import com.chess.pieces.ChessPiece;
import com.chess.pieces.Knight;

public abstract class Move {

    final Board board;
    final ChessPiece movedPiece;
    final int destinationCoordinate;

    Move(final Board board, final ChessPiece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final ChessPiece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class AttackMove extends Move {

        final ChessPiece attackedPiece;
        public AttackMove(final Board board, final ChessPiece movedPiece, final int destinationCoordinate, final ChessPiece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }



}
