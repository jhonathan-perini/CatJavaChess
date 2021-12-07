package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Rook;

public abstract class Movement {

    protected final Board board;
    protected final int destinationCoordinate;
    protected final Piece movedPiece;
    protected final boolean isFirstMove;

    private Movement(final Board board,
                     final Piece pieceMoved,
                     final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = pieceMoved;
        this.isFirstMove = pieceMoved.isFirstMove();
    }

    private Movement(final Board board,
                     final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof final Movement otherMovement)) {
            return false;
        }
        return getFirstCoordinate() == otherMovement.getFirstCoordinate() &&
                getSecondCoordinate() == otherMovement.getSecondCoordinate() &&
                getSelectedPiece().equals(otherMovement.getSelectedPiece());
    }

    public Board getBoard() {
        return this.board;
    }

    public int getFirstCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public int getSecondCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getSelectedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {
        final Board.Builder builder = new Builder();
        this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
        this.board.currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.nextRound(this.board.currentPlayer().getOpponent().getAlliance());
        builder.currentMove(this);
        return builder.build();
    }

    String disambiguationFile() {
        for(final Movement movement : this.board.currentPlayer().getLegalMoves()) {
            if(movement.getSecondCoordinate() == this.destinationCoordinate && !this.equals(movement) &&
                    this.movedPiece.getPieceType().equals(movement.getSelectedPiece().getPieceType())) {
                return BoardHandler.INSTANCE.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
    }

    public static class PawnPromotion
            extends PawnMovement {

        final Movement decoratedMovement;
        final Pawn promotedPawn;
        final Piece promotionPiece;

        public PawnPromotion(final Movement decoratedMovement,
                             final Piece promotionPiece) {
            super(decoratedMovement.getBoard(), decoratedMovement.getSelectedPiece(), decoratedMovement.getSecondCoordinate());
            this.decoratedMovement = decoratedMovement;
            this.promotedPawn = (Pawn) decoratedMovement.getSelectedPiece();
            this.promotionPiece = promotionPiece;
        }

        @Override
        public int hashCode() {
            return decoratedMovement.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }

        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decoratedMovement.execute();
            final Board.Builder builder = new Builder();
            pawnMovedBoard.currentPlayer().getActivePieces().stream().filter(piece -> !this.promotedPawn.equals(piece)).forEach(builder::setPiece);
            pawnMovedBoard.currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
            builder.setPiece(this.promotionPiece.movePiece(this));
            builder.nextRound(pawnMovedBoard.currentPlayer().getAlliance());
            builder.currentMove(this);
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMovement.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMovement.getAttackedPiece();
        }

        @Override
        public String toString() {
            return BoardHandler.INSTANCE.getPositionAtCoordinate(this.movedPiece.getPiecePosition()) + "-" +
                    BoardHandler.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate) + "=" + this.promotionPiece.getPieceType();
        }

    }

    public static class MajorMovement
            extends Movement {

        public MajorMovement(final Board board,
                             final Piece pieceMoved,
                             final int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMovement && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + disambiguationFile() +
                    BoardHandler.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class MajorAttackMovement
            extends AttackMovement {

        public MajorAttackMovement(final Board board,
                                   final Piece pieceMoved,
                                   final int destinationCoordinate,
                                   final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMovement && super.equals(other);

        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + disambiguationFile() + "x" +
                    BoardHandler.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnMovement
            extends Movement {

        public PawnMovement(final Board board,
                            final Piece pieceMoved,
                            final int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMovement && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardHandler.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnAttackMovement
            extends AttackMovement {

        public PawnAttackMovement(final Board board,
                                  final Piece pieceMoved,
                                  final int destinationCoordinate,
                                  final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMovement && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardHandler.INSTANCE.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).charAt(0) + "x" +
                    BoardHandler.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnEnPassantAttack extends PawnAttackMovement {

        public PawnEnPassantAttack(final Board board,
                                   final Piece pieceMoved,
                                   final int destinationCoordinate,
                                   final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnEnPassantAttack && super.equals(other);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Builder();
            this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
            this.board.currentPlayer().getOpponent().getActivePieces().stream().filter(piece -> !piece.equals(this.getAttackedPiece())).forEach(builder::setPiece);
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.nextRound(this.board.currentPlayer().getOpponent().getAlliance());
            builder.currentMove(this);
            return builder.build();
        }

    }

    public static class PawnJump
            extends Movement {

        public PawnJump(final Board board,
                        final Pawn pieceMoved,
                        final int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Builder();
            this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
            this.board.currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.enPassantPawn(movedPawn);
            builder.nextRound(this.board.currentPlayer().getOpponent().getAlliance());
            builder.currentMove(this);
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardHandler.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    static abstract class CastleMovement
            extends Movement {

        final Rook castleRook;
        final int castleRookStart;
        final int castleRookDestination;

        CastleMovement(final Board board,
                       final Piece pieceMoved,
                       final int destinationCoordinate,
                       final Rook castleRook,
                       final int castleRookStart,
                       final int castleRookDestination) {
            super(board, pieceMoved, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Builder();
            for (final Piece Piece : this.board.getAllPieces()) {
                if (!this.movedPiece.equals(Piece) && !this.castleRook.equals(Piece)) {
                    builder.setPiece(Piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            //calling movePiece here doesn't work, we need to explicitly create a new Rook
            builder.setPiece(new Rook(this.castleRook.getPieceAllegiance(), this.castleRookDestination, false));
            builder.nextRound(this.board.currentPlayer().getOpponent().getAlliance());
            builder.currentMove(this);
            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof final CastleMovement otherCastleMove)) {
                return false;
            }
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }

    }

    public static class KingSideCastleMovement
            extends CastleMovement {

        public KingSideCastleMovement(final Board board,
                                      final Piece pieceMoved,
                                      final int destinationCoordinate,
                                      final Rook castleRook,
                                      final int castleRookStart,
                                      final int castleRookDestination) {
            super(board, pieceMoved, destinationCoordinate, castleRook, castleRookStart,
                    castleRookDestination);
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof final KingSideCastleMovement otherKingSideCastleMove)) {
                return false;
            }
            return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove.getCastleRook());
        }

        @Override
        public String toString() {
            return "O-O";
        }

    }

    public static class QueenSideCastleMovement
            extends CastleMovement {

        public QueenSideCastleMovement(final Board board,
                                       final Piece pieceMoved,
                                       final int destinationCoordinate,
                                       final Rook castleRook,
                                       final int castleRookStart,
                                       final int rookCastleDestination) {
            super(board, pieceMoved, destinationCoordinate, castleRook, castleRookStart,
                    rookCastleDestination);
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof final QueenSideCastleMovement otherQueenSideCastleMove)) {
                return false;
            }
            return super.equals(otherQueenSideCastleMove) && this.castleRook.equals(otherQueenSideCastleMove.getCastleRook());
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

    }

    public static abstract class AttackMovement
            extends Movement {

        private final Piece attackedPiece;

        AttackMovement(final Board board,
                       final Piece pieceMoved,
                       final int destinationCoordinate,
                       final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate);
            this.attackedPiece = pieceAttacked;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof final AttackMovement otherAttackMove)) {
                return false;
            }
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

    }

    private static class NullMovement
            extends Movement {

        private NullMovement() {
            super(null, -1);
        }

        @Override
        public int getFirstCoordinate() {
            return -1;
        }

        @Override
        public int getSecondCoordinate() {
            return -1;
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute null move!");
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }

    public static class MoveFactory {

        private static final Movement NULL_MOVEMENT = new NullMovement();

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }

        public static Movement getNullMove() {
            return NULL_MOVEMENT;
        }

        public static Movement createMove(final Board board,
                                          final int currentCoordinate,
                                          final int destinationCoordinate) {
            for (final Movement movement : board.getAllLegalMoves()) {
                if (movement.getFirstCoordinate() == currentCoordinate &&
                        movement.getSecondCoordinate() == destinationCoordinate) {
                    return movement;
                }
            }
            return NULL_MOVEMENT;
        }
    }
}