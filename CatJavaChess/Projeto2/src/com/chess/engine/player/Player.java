package com.chess.engine.player;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Movement;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.chess.engine.pieces.Piece.PieceType.KING;
import static java.util.stream.Collectors.collectingAndThen;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Movement> legalMovements;
    protected final boolean isInCheck;

    Player(final Board board,
           final Collection<Movement> playerLegals,
           final Collection<Movement> opponentLegals) {
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
        playerLegals.addAll(calculateKingCastles(playerLegals, opponentLegals));
        this.legalMovements = Collections.unmodifiableCollection(playerLegals);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && hasEscapeMoves();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && hasEscapeMoves();
    }

    public boolean isCastled() {
        return this.playerKing.isCastled();
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    private King establishKing() {
        return (King) getActivePieces().stream()
                .filter(piece -> piece.getPieceType() == KING)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private boolean hasEscapeMoves() {
        return this.legalMovements.stream()
                .noneMatch(move -> makeMove(move)
                        .getMoveStatus().isDone());
    }

    public Collection<Movement> getLegalMoves() {
        return this.legalMovements;
    }

    static Collection<Movement> calculateAttacksOnTile(final int tile,
                                                       final Collection<Movement> movements) {
        return movements.stream()
                .filter(move -> move.getSecondCoordinate() == tile)
                .collect(collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public NewMove makeMove(final Movement movement) {
        if (!this.legalMovements.contains(movement)) {
            return new NewMove(this.board, CurrentMove.ILLEGAL_MOVE);
        }
        final Board transitionedBoard = movement.execute();
        return transitionedBoard.currentPlayer().getOpponent().isInCheck() ?
                new NewMove(this.board, CurrentMove.LEAVES_PLAYER_IN_CHECK) :
                new NewMove(transitionedBoard, CurrentMove.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Color getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Movement> calculateKingCastles(Collection<Movement> playerLegals,
                                                                 Collection<Movement> opponentLegals);
    protected boolean hasCastleOpportunities() {
        return this.isInCheck || this.playerKing.isCastled() ||
                (!this.playerKing.isKingSideCastleCapable() && !this.playerKing.isQueenSideCastleCapable());
    }

}


