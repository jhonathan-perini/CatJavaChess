package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardHandler;
import com.chess.engine.board.Movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1,
            7, 8, 9 };

    public Queen(final Color color, final int piecePosition) {
        super(PieceType.QUEEN, color, piecePosition, true);
    }

    public Queen(final Color color,
                 final int piecePosition,
                 final boolean isFirstMove) {
        super(PieceType.QUEEN, color, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Movement> calculateLegalMoves(final Board board) {
        final List<Movement> legalMovements = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (true) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                        isEightColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (!BoardHandler.VerifyCoordinate(candidateDestinationCoordinate)) {
                    break;
                } else {
                    final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                    if (pieceAtDestination == null) {
                        legalMovements.add(new Movement.MajorMovement(board, this, candidateDestinationCoordinate));
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
    public Queen movePiece(final Movement movement) {
        return PieceUtils.INSTANCE.getMovedQueen(movement.getSelectedPiece().getPieceAllegiance(), movement.getSecondCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidatePosition) {
        return BoardHandler.INSTANCE.FIRST_COLUMN.get(candidatePosition) && ((currentPosition == -9)
                || (currentPosition == -1) || (currentPosition == 7));
    }

    private static boolean isEightColumnExclusion(final int currentPosition,
                                                  final int candidatePosition) {
        return BoardHandler.INSTANCE.EIGHTH_COLUMN.get(candidatePosition) && ((currentPosition == -7)
                || (currentPosition == 1) || (currentPosition == 9));
    }

}