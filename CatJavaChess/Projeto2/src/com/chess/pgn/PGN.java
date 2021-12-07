package com.chess.pgn;

import com.chess.engine.board.Movement;
import gui.ChessBoard.MoveLog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PGN {

    private PGN() {
        throw new RuntimeException("Not Instantiable!");
    }

    public static void writeGameToPGNFile(final File pgnFile,
                                          final MoveLog moveLog) throws IOException {
        final StringBuilder builder = new StringBuilder();
        builder.append(calculateEventString()).append("\n");
        builder.append(calculateDateString()).append("\n");
        builder.append(calculatePlyCountString(moveLog)).append("\n");
        for(final Movement movement : moveLog.getMoves()) {
            builder.append(movement.toString()).append(" ");
        }
        try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pgnFile, true)))) {
            writer.write(builder.toString());
        }
    }

    private static String calculateEventString() {
        return "[Event \"" +"JCat Chess Game"+ "\"]";
    }

    private static String calculateDateString() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return "[Date \"" + dateFormat.format(new Date()) + "\"]";
    }

    private static String calculatePlyCountString(final MoveLog moveLog) {
        return "[PlyCount \"" +moveLog.size() + "\"]";
    }












}
