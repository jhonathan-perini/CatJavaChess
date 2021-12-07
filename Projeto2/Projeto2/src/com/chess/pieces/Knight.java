package com.chess.pieces;

import com.chess.Alliance;
import com.chess.board.Board;
import com.chess.board.Move;
import com.chess.board.TileBoard;
import com.chess.board.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.board.Move.*;

public class Knight extends ChessPiece {

    private final static int[] CANDIDATE_MOVES = { -17, -15, -10, -6, 6, 10, 15, 17};

    public Knight( final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
       final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidate: CANDIDATE_MOVES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;

            if(Utils.isValidTileCoordinate(candidateDestinationCoordinate)){

                if(isFirstColumn(this.piecePosition, currentCandidate) ||
                        isSecondColumn(this.piecePosition, currentCandidate) ||
                        isSeventhColumn(this.piecePosition, currentCandidate) ||
                        isEighthColumn(this.piecePosition, currentCandidate)){
                    continue;
                }
            final TileBoard candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

            if(!candidateDestinationTile.isTileNotEmpty()){
                legalMoves.add(new MajorMove(board, this,candidateDestinationCoordinate ));
            } else {
                final ChessPiece pieceAtDestination = candidateDestinationTile.getPiece();
                final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                if(this.pieceAlliance != pieceAlliance){
                    legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            }
            }

        }
        return Collections.unmodifiableList(legalMoves);
    }

    public String toString(){
        return PieceType.KNIGHT.toString();
    }
        private static boolean isFirstColumn(final int currentPosition, final int candidateOffset ){
        return Utils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
                (candidateOffset == 6) || (candidateOffset==15));
        }

    private static boolean isSecondColumn(final int currentPosition, final int candidateOffset ){
        return Utils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumn(final int currentPosition, final int candidateOffset ){
        return Utils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == 10) || (candidateOffset == -6));
    }

    private static boolean isEighthColumn(final int currentPosition, final int candidateOffset ){
        return Utils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == 17) || (candidateOffset == 10)
                || (candidateOffset == -15) || (candidateOffset == -6));
    }
}
