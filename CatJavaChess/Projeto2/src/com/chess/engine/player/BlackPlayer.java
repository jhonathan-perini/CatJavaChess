package com.chess.engine.player;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardHandler;
import com.chess.engine.board.Movement;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.pieces.Piece.PieceType.ROOK;


    public final class BlackPlayer extends Player {

        public BlackPlayer(final Board board,
                           final Collection<Movement> whiteStandardLegals,
                           final Collection<Movement> blackStandardLegals) {
            super(board, blackStandardLegals, whiteStandardLegals);
        }

        @Override
        protected Collection<Movement> calculateKingCastles(final Collection<Movement> playerLegals,
                                                            final Collection<Movement> opponentLegals) {

            if (hasCastleOpportunities()) {
                return Collections.emptyList();
            }

            final List<Movement> kingCastles = new ArrayList<>();

            if (this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 4 && !this.isInCheck) {
                if (this.board.getPiece(5) == null && this.board.getPiece(6) == null) {
                    final Piece kingSideRook = this.board.getPiece(7);
                    if (kingSideRook != null && kingSideRook.isFirstMove() &&
                            Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            kingSideRook.getPieceType() == ROOK) {
                        if (BoardHandler.isKingPawnTrap(this.board, this.playerKing, 12)) {
                            kingCastles.add(
                                    new Movement.KingSideCastleMovement(this.board, this.playerKing, 6, (Rook) kingSideRook, kingSideRook.getPiecePosition(), 5));

                        }
                    }
                }
                if (this.board.getPiece(1) == null && this.board.getPiece(2) == null &&
                        this.board.getPiece(3) == null) {
                    final Piece queenSideRook = this.board.getPiece(0);
                    if (queenSideRook != null && queenSideRook.isFirstMove() &&
                            Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                            queenSideRook.getPieceType() == ROOK) {
                        if (BoardHandler.isKingPawnTrap(this.board, this.playerKing, 12)) {
                            kingCastles.add(
                                    new Movement.QueenSideCastleMovement(this.board, this.playerKing, 2, (Rook) queenSideRook, queenSideRook.getPiecePosition(), 3));
                        }
                    }
                }
            }
            return Collections.unmodifiableList(kingCastles);
        }

        @Override
        public WhitePlayer getOpponent() {
            return this.board.whitePlayer();
        }

        @Override
        public Collection<Piece> getActivePieces() {
            return this.board.getBlackPieces();
        }

        @Override
        public Color getAlliance() {
            return Color.BLACK;
        }

        @Override
        public String toString() {
            return Color.BLACK.toString();
        }

    }

