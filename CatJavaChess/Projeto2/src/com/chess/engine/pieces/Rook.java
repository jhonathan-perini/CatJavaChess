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

public final class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -8, -1, 1, 8 };

    public Rook(final Color color, final int piecePosition) {
        super(PieceType.ROOK, color, piecePosition, true);
    }

    public Rook(final Color color,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.ROOK, color, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Movement> calculateLegalMoves(final Board board) {
        final List<Movement> legalMovements = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardHandler.VerifyCoordinate(candidateDestinationCoordinate)) {
                if (isColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
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
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMovements);
    }

    @Override
    public Rook movePiece(final Movement movement) {
        return PieceUtils.INSTANCE.getMovedRook(movement.getSelectedPiece().getPieceAllegiance(), movement.getSecondCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    private static boolean isColumnExclusion(final int currentCandidate,
                                             final int candidateDestinationCoordinate) {
        return (BoardHandler.INSTANCE.FIRST_COLUMN.get(candidateDestinationCoordinate) && (currentCandidate == -1)) ||
                (BoardHandler.INSTANCE.EIGHTH_COLUMN.get(candidateDestinationCoordinate) && (currentCandidate == 1));
    }

}