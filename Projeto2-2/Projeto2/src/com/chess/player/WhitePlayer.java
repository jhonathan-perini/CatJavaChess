package com.chess.player;

import com.chess.Alliance;
import com.chess.board.Board;
import com.chess.board.Move;
import com.chess.pieces.ChessPiece;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WhitePlayer extends Player {

    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<ChessPiece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }


}
