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

public final class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    public Bishop(final Color color,
                  final int piecePosition) {
        super(PieceType.BISHOP, color, piecePosition, true);
    }

    public Bishop(final Color color,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.BISHOP, color, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Movement> calculateLegalMoves(final Board board) {
        final List<Movement> legalMovements = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardHandler.VerifyCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                        isEighthColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (BoardHandler.VerifyCoordinate(candidateDestinationCoordinate)) {
                    final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                    if (pieceAtDestination == null) {
                        legalMovements.add(new MajorMovement(board, this, candidateDestinationCoordinate));
                    }
                    else {
                        final Color pieceColor = pieceAtDestination.getPieceAllegiance();
                        if (this.pieceColor != pieceColor) {
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
    public Bishop movePiece(final Movement movement) {
        return PieceUtils.INSTANCE.getMovedBishop(movement.getSelectedPiece().getPieceAllegiance(), movement.getSecondCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return (BoardHandler.INSTANCE.FIRST_COLUMN.get(candidateDestinationCoordinate) &&
                ((currentCandidate == -9) || (currentCandidate == 7)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardHandler.INSTANCE.EIGHTH_COLUMN.get(candidateDestinationCoordinate) &&
                ((currentCandidate == -7) || (currentCandidate == 9));
    }

}