package com.chess.pieces;

import com.chess.Alliance;
import com.chess.board.Board;
import com.chess.board.Move;
import com.chess.board.Move.MajorMove;
import com.chess.board.Move.AttackMove;
import com.chess.board.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends ChessPiece{

    private final static int[] CANDIDATE_MOVE_COORDINATE={ 8 , 16 , 7 , 9 };

    public Pawn( final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinarioCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            if(!Utils.isValidTileCoordinate(candidateDestinarioCoordinate)){
                continue;
            }

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinarioCoordinate).isTileNotEmpty()){

                legalMoves.add(new Move.MajorMove(board,this, candidateDestinarioCoordinate));
            } else if(currentCandidateOffset == 16 && this.isFirstMove() && (Utils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isBlack()) || (Utils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite())){

                    final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                    if(board.getTile(behindCandidateDestinationCoordinate).isTileNotEmpty() && !board.getTile(candidateDestinarioCoordinate).isTileNotEmpty()){
                        legalMoves.add(new Move.MajorMove(board,this, candidateDestinarioCoordinate));
                    }
            } else if(currentCandidateOffset == 7 && !(Utils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || (Utils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
                    if(board.getTile(candidateDestinarioCoordinate).isTileNotEmpty()){
                        final ChessPiece pieceCandidate = board.getTile(candidateDestinarioCoordinate).getPiece();
                        if(this.pieceAlliance != pieceCandidate.getPieceAlliance()){
                            legalMoves.add(new MajorMove(board,this,candidateDestinarioCoordinate));
                        }
                    }
            } else if(currentCandidateOffset == 9 && !(Utils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || (Utils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
                if(board.getTile(candidateDestinarioCoordinate).isTileNotEmpty()){
                    final ChessPiece pieceCandidate = board.getTile(candidateDestinarioCoordinate).getPiece();
                    if(this.pieceAlliance != pieceCandidate.getPieceAlliance()){
                        legalMoves.add(new MajorMove(board,this,candidateDestinarioCoordinate));
                    }
                }

            }
        }

        return Collections.unmodifiableList(legalMoves);
    }
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
