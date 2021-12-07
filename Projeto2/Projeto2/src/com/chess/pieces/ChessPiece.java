package com.chess.pieces;

import com.chess.Alliance;
import com.chess.board.Board;
import com.chess.board.Move;

import java.util.Collection;

public abstract class ChessPiece {
    protected final int piecePosition;
    protected final PieceType pieceType;
    protected final Alliance pieceAlliance;

    protected final boolean isFirstMove;


    ChessPiece(final PieceType pieceType, int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.pieceType = pieceType;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public  PieceType getPieceType() {
        return this.pieceType;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum PieceType {

        PAWN("P"){
            @Override
            public  boolean isKing() {
                return false;
            }
        },
        KNIGHT("N"){
            @Override
            public  boolean isKing() {
                return false;
            }
        },
        BISHOP("B"){
            @Override
            public  boolean isKing() {
                return false;
            }
        },
        ROOK("R"){
            @Override
            public  boolean isKing() {
                return false;
            }
        },
        QUEEN("Q"){
            @Override
            public  boolean isKing() {
                return false;
            }
        },
        KING("K"){
            @Override
            public  boolean isKing() {
                return true;
            }
        };

       private String pieceName;

        PieceType (final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }
        public  abstract boolean isKing();
    }
}