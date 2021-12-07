package com.chess.engine.player;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardHandler;
import com.chess.engine.board.Movement;
import com.chess.engine.board.Movement.KingSideCastleMovement;
import com.chess.engine.board.Movement.QueenSideCastleMovement;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.pieces.Piece.PieceType.ROOK;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Movement> whiteStandardLegals,
                       final Collection<Movement> blackStandardLegals) {
        super(board, whiteStandardLegals, blackStandardLegals);
    }

    @Override
    protected Collection<Movement> calculateKingCastles(final Collection<Movement> playerLegals,
                                                        final Collection<Movement> opponentLegals) {

        if(hasCastleOpportunities()) {
            return Collections.emptyList();
        }

        final List<Movement> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 60 && !this.isInCheck()) {
            //whites king side castle
            if(this.board.getPiece(61) == null && this.board.getPiece(62) == null) {
                final Piece kingSideRook = this.board.getPiece(63);
                if(kingSideRook != null && kingSideRook.isFirstMove()) {
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            kingSideRook.getPieceType() == ROOK) {
                        if(BoardHandler.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            kingCastles.add(new KingSideCastleMovement(this.board, this.playerKing, 62, (Rook) kingSideRook, kingSideRook.getPiecePosition(), 61));
                        }
                    }
                }
            }
            //whites queen side castle
            if(this.board.getPiece(59) == null && this.board.getPiece(58) == null &&
                    this.board.getPiece(57) == null) {
                final Piece queenSideRook = this.board.getPiece(56);
                if(queenSideRook != null && queenSideRook.isFirstMove()) {
                    if(Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() && queenSideRook.getPieceType() == ROOK) {
                        if(BoardHandler.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            kingCastles.add(new QueenSideCastleMovement(this.board, this.playerKing, 58, (Rook) queenSideRook, queenSideRook.getPiecePosition(), 59));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }

    @Override
    public BlackPlayer getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Color getAlliance() {
        return Color.WHITE;
    }

    @Override
    public String toString() {
        return Color.WHITE.toString();
    }

}
