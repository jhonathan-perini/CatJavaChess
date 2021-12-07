package com.chess.board;

import com.chess.pieces.ChessPiece;
import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class TileBoard {

    protected final int TCoordinate; //Coordenada do quadrado

    private static final Map<Integer,EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTile = new HashMap<>(); //lista com todas as posições do board

        for(int i=0; i<Utils.NUM_TILES; i++){
            emptyTile.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTile);
       // return ImmutableMap.copyOf(emptyTile);
    }
    public static TileBoard createTile(final int tileCoordinate, final ChessPiece piece ){
        return piece != null ? new NotEmptyTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
    }
    TileBoard(final int TCoordinate){
        this.TCoordinate = TCoordinate;
    }

    public abstract boolean isTileNotEmpty();

    public abstract ChessPiece getPiece();

    public static final class EmptyTile extends TileBoard{
        private EmptyTile(final int coordinate) {
            super(coordinate);
        }

        @Override

        public boolean isTileNotEmpty() {
            return false;
        }

        @Override

        public ChessPiece getPiece() {
            return null;
        }
    }

    public static final class NotEmptyTile extends TileBoard {

       private final ChessPiece pieceOnTile;

        private NotEmptyTile(int tileCoordinate, ChessPiece pieceOnTile ){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override

        public boolean isTileNotEmpty(){
            return true;
        }

        @Override

        public ChessPiece getPiece(){
            return this.pieceOnTile;
        }


    }
}
