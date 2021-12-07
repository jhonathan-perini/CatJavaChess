package com.chess.engine.board;

import com.chess.engine.Color;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Board {

    private final Map<Integer, Piece> currentBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
    private static final Board STANDARD_BOARD = createStandardBoardImpl();

    private Board(final Builder builder) {
        this.currentBoard = Collections.unmodifiableMap(builder.currentBoard);
        this.whitePieces = calculateActivePieces(builder, Color.WHITE);
        this.blackPieces = calculateActivePieces(builder, Color.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Movement> whiteLegalMovements = calculateLegalMoves(this.whitePieces);
        final Collection<Movement> blackLegalMovements = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMovements, blackLegalMovements);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMovements, blackLegalMovements);
        this.currentPlayer = builder.nextRound.choosePlayerByAlliance(this.whitePlayer, this.blackPlayer);
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardHandler.NUM_TILES; i++) {
            final String tileText = prettyPrint(this.currentBoard.get(i));
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % 8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private static String prettyPrint(final Piece Piece) {
        if (Piece != null) {
            return Piece.getPieceAllegiance().isBlack() ?
                    Piece.toString().toLowerCase() : Piece.toString();
        }
        return "-";
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Collection<Piece> getAllPieces() {
        return Stream.concat(this.whitePieces.stream(),
                this.blackPieces.stream()).collect(Collectors.toList());
    }

    public Collection<Movement> getAllLegalMoves() {
        return Stream.concat(this.whitePlayer.getLegalMoves().stream(),
                this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList());
    }

    public WhitePlayer whitePlayer() {
        return this.whitePlayer;
    }

    public BlackPlayer blackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Piece getPiece(final int coordinate) {
        return this.currentBoard.get(coordinate);
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public static Board createStandardBoard() {

        return STANDARD_BOARD;
    }

    private static Board createStandardBoardImpl() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Color.BLACK, 0));
        builder.setPiece(new Knight(Color.BLACK, 1));
        builder.setPiece(new Bishop(Color.BLACK, 2));
        builder.setPiece(new Queen(Color.BLACK, 3));
        builder.setPiece(new King(Color.BLACK, 4, true, true));
        builder.setPiece(new Bishop(Color.BLACK, 5));
        builder.setPiece(new Knight(Color.BLACK, 6));
        builder.setPiece(new Rook(Color.BLACK, 7));
        builder.setPiece(new Pawn(Color.BLACK, 8));
        builder.setPiece(new Pawn(Color.BLACK, 9));
        builder.setPiece(new Pawn(Color.BLACK, 10));
        builder.setPiece(new Pawn(Color.BLACK, 11));
        builder.setPiece(new Pawn(Color.BLACK, 12));
        builder.setPiece(new Pawn(Color.BLACK, 13));
        builder.setPiece(new Pawn(Color.BLACK, 14));
        builder.setPiece(new Pawn(Color.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Color.WHITE, 48));
        builder.setPiece(new Pawn(Color.WHITE, 49));
        builder.setPiece(new Pawn(Color.WHITE, 50));
        builder.setPiece(new Pawn(Color.WHITE, 51));
        builder.setPiece(new Pawn(Color.WHITE, 52));
        builder.setPiece(new Pawn(Color.WHITE, 53));
        builder.setPiece(new Pawn(Color.WHITE, 54));
        builder.setPiece(new Pawn(Color.WHITE, 55));
        builder.setPiece(new Rook(Color.WHITE, 56));
        builder.setPiece(new Knight(Color.WHITE, 57));
        builder.setPiece(new Bishop(Color.WHITE, 58));
        builder.setPiece(new Queen(Color.WHITE, 59));
        builder.setPiece(new King(Color.WHITE, 60, true, true));
        builder.setPiece(new Bishop(Color.WHITE, 61));
        builder.setPiece(new Knight(Color.WHITE, 62));
        builder.setPiece(new Rook(Color.WHITE, 63));
        //white to move
        builder.nextRound(Color.WHITE);
        //build the board
        return builder.build();
    }

    private Collection<Movement> calculateLegalMoves(final Collection<Piece> Piece) {
        return Piece.stream().flatMap(piece -> piece.calculateLegalMoves(this).stream())
                .collect(Collectors.toList());
    }

    private static Collection<Piece> calculateActivePieces(final Builder builder,
                                                           final Color color) {
        return builder.currentBoard.values().stream()
                .filter(piece -> piece.getPieceAllegiance() == color)
                .collect(Collectors.toList());
    }

    public static class Builder {

        Map<Integer, Piece> currentBoard;
        Color nextRound;
        Pawn enPassantPawn;
        Movement currentMove;

        public Builder() {
            this.currentBoard = new HashMap<>(32, 1.0f);
        }

        public Builder setPiece(final Piece Piece) {
            this.currentBoard.put(Piece.getPiecePosition(), Piece);
            return this;
        }

        public Builder nextRound(final Color nextRound) {
            this.nextRound = nextRound;
            return this;
        }

        public Builder enPassantPawn(final Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
            return this;
        }

        public Builder currentMove(final Movement currentMove) {
            this.currentMove = currentMove;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

    }
}
