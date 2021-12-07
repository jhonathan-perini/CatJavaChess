package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardHandler;
import com.chess.engine.board.Movement;
import com.chess.engine.board.Movement.MajorMovement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

    public Knight(final Color color,
                  final int piecePosition) {
        super(PieceType.KNIGHT, color, piecePosition, true);
    }

    public Knight(final Color color,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT, color, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Movement> calculateLegalMoves(final Board board) {
        final List<Movement> legalMovements = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardHandler.VerifyCoordinate(candidateDestinationCoordinate)) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                if (pieceAtDestination == null) {
                    legalMovements.add(new MajorMovement(board, this, candidateDestinationCoordinate));
                } else {
                    final Color pieceAtDestinationAllegiance = pieceAtDestination.getPieceAllegiance();
                    if (this.pieceColor != pieceAtDestinationAllegiance) {
                        legalMovements.add(new Movement.MajorAttackMovement(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMovements);
    }

    @Override
    public Knight movePiece(final Movement movement) {
        return PieceUtils.INSTANCE.getMovedKnight(movement.getSelectedPiece().getPieceAllegiance(), movement.getSecondCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidateOffset) {
        return BoardHandler.INSTANCE.FIRST_COLUMN.get(currentPosition) && ((candidateOffset == -17) ||
                (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardHandler.INSTANCE.SECOND_COLUMN.get(currentPosition) && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition,
                                                    final int candidateOffset) {
        return BoardHandler.INSTANCE.SEVENTH_COLUMN.get(currentPosition) && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardHandler.INSTANCE.EIGHTH_COLUMN.get(currentPosition) && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }

}