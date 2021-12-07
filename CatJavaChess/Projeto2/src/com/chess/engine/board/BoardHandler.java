package com.chess.engine.board;

import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PieceType;

import java.util.*;

public enum BoardHandler {

    INSTANCE;

    public final List<Boolean> FIRST_COLUMN = initialColumn(0);
    public final List<Boolean> SECOND_COLUMN = initialColumn(1);
    public final List<Boolean> SEVENTH_COLUMN = initialColumn(6);
    public final List<Boolean> EIGHTH_COLUMN = initialColumn(7);
    public final List<Boolean> FIRST_ROW = initialRow(0);
    public final List<Boolean> SECOND_ROW = initialRow(8);
    public final List<Boolean> THIRD_ROW = initialRow(16);
    public final List<Boolean> FOURTH_ROW = initialRow(24);
    public final List<Boolean> FIFTH_ROW = initialRow(32);
    public final List<Boolean> SIXTH_ROW = initialRow(40);
    public final List<Boolean> SEVENTH_ROW = initialRow(48);
    public final List<Boolean> EIGHTH_ROW = initialRow(56);
    public final List<String> ALGEBRAIC_NOTATION = AlgebricNotation();
    public final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    public static final int START_TILE_INDEX = 0;
    public static final int TILES_PER_ROW = 8;
    public static final int NUM_TILES = 64;

    private static List<Boolean> initialColumn(int columnNumber) {
        final Boolean[] column = new Boolean[NUM_TILES];
        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);
        return List.of((column));
    }

    private static List<Boolean> initialRow(int rowNumber) {
        final Boolean[] row = new Boolean[NUM_TILES];
        Arrays.fill(row, false);
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % TILES_PER_ROW != 0);

        return List.of(row);
    }

    private Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static List<String> AlgebricNotation() {
        return List.of("a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1");
    }

    public static boolean VerifyCoordinate(final int coordinate) {
        return coordinate >= START_TILE_INDEX && coordinate < NUM_TILES;
    }

    public int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    } //?

    public String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    } //?





    public static boolean isKingPawnTrap(final Board board,
                                         final King king,
                                         final int frontTile) {
        final Piece Piece = board.getPiece(frontTile);
        return Piece == null ||
                Piece.getPieceType() != PieceType.PAWN ||
                Piece.getPieceAllegiance() == king.getPieceAllegiance();
    }


    public static boolean isEndGame(final Board board) {
        return board.currentPlayer().isInCheckMate();

    }

    public static boolean isEndGameStale(final Board board) {
        return board.currentPlayer().isInStaleMate();
    }
}

