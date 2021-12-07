package com.chess.player;

import com.chess.Alliance;
import com.chess.board.Board;
import com.chess.board.Move;
import com.chess.pieces.ChessPiece;
import com.chess.pieces.King;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;


    Player(final Board board, final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves ){
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();

    }

    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move: moves ) {
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    protected King establishKing(){
        for(final ChessPiece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    protected  boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move); //ver move
            if(transition.getMoveStatus().isDone()){
                return true;
            }

        }
        return  false;
    }

    public boolean isInStaleMate() {
   return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        return null;
    }
    public abstract Collection<ChessPiece> getActivePieces();

    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}

