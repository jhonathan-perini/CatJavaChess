package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardHandler;
import com.chess.engine.board.Movement;
import com.chess.engine.board.Movement.PawnAttackMovement;
import com.chess.engine.board.Movement.PawnEnPassantAttack;
import com.chess.engine.board.Movement.PawnJump;
import com.chess.engine.board.Movement.PawnMovement;
import com.chess.engine.board.Movement.PawnPromotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Pawn
        extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9};

    public Pawn(final Color allegiance,
                final int piecePosition) {
        super(PieceType.PAWN, allegiance, piecePosition, true);
    }

    public Pawn(final Color color,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.PAWN, color, piecePosition, isFirstMove);
    }

    @Override
    public Collection<Movement> calculateLegalMoves(final Board board) {
        final List<Movement> legalMovements = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate =
                    this.piecePosition + (this.pieceColor.getDirection() * currentCandidateOffset);
            if (!BoardHandler.VerifyCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 && board.getPiece(candidateDestinationCoordinate) == null) {
                if (this.pieceColor.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMovements.add(new PawnPromotion(
                            new PawnMovement(board, this, candidateDestinationCoordinate), PieceUtils.INSTANCE.getMovedQueen(this.pieceColor, candidateDestinationCoordinate)));
                    legalMovements.add(new PawnPromotion(
                            new PawnMovement(board, this, candidateDestinationCoordinate), PieceUtils.INSTANCE.getMovedRook(this.pieceColor, candidateDestinationCoordinate)));
                    legalMovements.add(new PawnPromotion(
                            new PawnMovement(board, this, candidateDestinationCoordinate), PieceUtils.INSTANCE.getMovedBishop(this.pieceColor, candidateDestinationCoordinate)));
                    legalMovements.add(new PawnPromotion(
                            new PawnMovement(board, this, candidateDestinationCoordinate), PieceUtils.INSTANCE.getMovedKnight(this.pieceColor, candidateDestinationCoordinate)));
                }
                else {
                    legalMovements.add(new PawnMovement(board, this, candidateDestinationCoordinate));
                }
            }
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardHandler.INSTANCE.SECOND_ROW.get(this.piecePosition) && this.pieceColor.isBlack()) ||
                            (BoardHandler.INSTANCE.SEVENTH_ROW.get(this.piecePosition) && this.pieceColor.isWhite()))) {
                final int behindCandidateDestinationCoordinate =
                        this.piecePosition + (this.pieceColor.getDirection() * 8);
                if (board.getPiece(candidateDestinationCoordinate) == null &&
                        board.getPiece(behindCandidateDestinationCoordinate) == null) {
                    legalMovements.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }
            else if (currentCandidateOffset == 7 &&
                    !((BoardHandler.INSTANCE.EIGHTH_COLUMN.get(this.piecePosition) && this.pieceColor.isWhite()) ||
                            (BoardHandler.INSTANCE.FIRST_COLUMN.get(this.piecePosition) && this.pieceColor.isBlack()))) {
                if(board.getPiece(candidateDestinationCoordinate) != null) {
                    final Piece pieceOnCandidate = board.getPiece(candidateDestinationCoordinate);
                    if (this.pieceColor != pieceOnCandidate.getPieceAllegiance()) {
                        if (this.pieceColor.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate, pieceOnCandidate), PieceUtils.INSTANCE.getMovedQueen(this.pieceColor, candidateDestinationCoordinate)));
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate, pieceOnCandidate), PieceUtils.INSTANCE.getMovedRook(this.pieceColor, candidateDestinationCoordinate)));
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate, pieceOnCandidate), PieceUtils.INSTANCE.getMovedBishop(this.pieceColor, candidateDestinationCoordinate)));
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate, pieceOnCandidate), PieceUtils.INSTANCE.getMovedKnight(this.pieceColor, candidateDestinationCoordinate)));
                        }
                        else {
                            legalMovements.add(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition + (this.pieceColor.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceColor != pieceOnCandidate.getPieceAllegiance()) {
                        legalMovements.add(
                                new PawnEnPassantAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate));

                    }
                }
            }
            else if (currentCandidateOffset == 9 &&
                    !((BoardHandler.INSTANCE.FIRST_COLUMN.get(this.piecePosition) && this.pieceColor.isWhite()) ||
                            (BoardHandler.INSTANCE.EIGHTH_COLUMN.get(this.piecePosition) && this.pieceColor.isBlack()))) {
                if(board.getPiece(candidateDestinationCoordinate) != null) {
                    if (this.pieceColor !=
                            board.getPiece(candidateDestinationCoordinate).getPieceAllegiance()) {
                        if (this.pieceColor.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate,
                                            board.getPiece(candidateDestinationCoordinate)), PieceUtils.INSTANCE.getMovedQueen(this.pieceColor, candidateDestinationCoordinate)));
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate,
                                            board.getPiece(candidateDestinationCoordinate)), PieceUtils.INSTANCE.getMovedRook(this.pieceColor, candidateDestinationCoordinate)));
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate,
                                            board.getPiece(candidateDestinationCoordinate)), PieceUtils.INSTANCE.getMovedBishop(this.pieceColor, candidateDestinationCoordinate)));
                            legalMovements.add(new PawnPromotion(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate,
                                            board.getPiece(candidateDestinationCoordinate)), PieceUtils.INSTANCE.getMovedKnight(this.pieceColor, candidateDestinationCoordinate)));
                        }
                        else {
                            legalMovements.add(
                                    new PawnAttackMovement(board, this, candidateDestinationCoordinate,
                                            board.getPiece(candidateDestinationCoordinate)));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition - (this.pieceColor.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceColor != pieceOnCandidate.getPieceAllegiance()) {
                        legalMovements.add(
                                new PawnEnPassantAttack(board, this, candidateDestinationCoordinate, pieceOnCandidate));

                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMovements);
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    @Override
    public Pawn movePiece(final Movement movement) {
        return PieceUtils.INSTANCE.getMovedPawn(movement.getSelectedPiece().getPieceAllegiance(), movement.getSecondCoordinate());
    }

}