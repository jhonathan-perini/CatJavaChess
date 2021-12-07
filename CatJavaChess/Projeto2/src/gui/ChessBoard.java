package gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardHandler;
import com.chess.engine.board.Movement;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.NewMove;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import static com.chess.pgn.PGN.writeGameToPGNFile;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class ChessBoard {


    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(900, 800);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(550, 550);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    public static ChessBoard INSTANCE;
    public static  JFrame gameFrame;

    public static MoveHistory moveHistory;
    private final ButtonMenu buttonMenu;
    private final TakenPieces takenPieces;
    public static BoardPanel boardPanel;
    private final MoveLog moveLog;
    public static Board chessBoard;
    public static BoardDirection boardDirection;
    private Piece sourceTile;
    private Piece humanMovedPiece;
    private static final String defaultPieceImagesPath ="art/";
    private final Color  lightTileColor = Color.decode("#FFFFFF");
    private final Color darkTileColor = Color.decode("#52307c");
    private Board getGameBoard() {
        return chessBoard;
    }
   private final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/background.wav").getAbsoluteFile());
   private static Clip clip;



    public static boolean highlightLegalMoves;


    public ChessBoard(String[] Name) throws UnsupportedAudioFileException, IOException {
        gameFrame = new JFrame("Chess game");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setSize(OUTER_FRAME_DIMENSION);
        chessBoard = Board.createStandardBoard();
        RulesScreen rulesScreen = new RulesScreen();
        moveHistory = new MoveHistory(Name);
        this.buttonMenu = new ButtonMenu(Name);
        this.takenPieces = new TakenPieces();
        boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        gameFrame.setLocationRelativeTo(null);
        boardDirection = BoardDirection.NORMAL;
        highlightLegalMoves = true;
        gameFrame.setResizable(false);
        rulesScreen.setResizable(false);
        rulesScreen.setLocationRelativeTo(null);
        gameFrame.add(this.takenPieces,BorderLayout.WEST);
        gameFrame.add(boardPanel, BorderLayout.CENTER);
        gameFrame.add(moveHistory,BorderLayout.EAST);
        gameFrame.add(this.buttonMenu, BorderLayout.SOUTH);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);






       gameFrame.setVisible(true);
       rulesScreen.setVisible(true);
        playSoundLoop();

    }




    public static ChessBoard get() {
        return INSTANCE;
    }


    JFrame getGameFrame() {
        return gameFrame;
    }




    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    static void savePGNFile(final File pgnFile) {
        try {
            writeGameToPGNFile(pgnFile, ChessBoard.get().getMoveLog());
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public enum BoardDirection{

        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }

    public class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardHandler.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard (final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog {
        private final List<Movement> movements;

         MoveLog(){
        this.movements = new ArrayList<>();
         }

         public List<Movement> getMoves(){
             return this.movements;
         }

         public void addMove(final Movement movement){
            this.movements.add(movement);
         }

         public int size(){
             return this.movements.size();
         }

    }

    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);


            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {






                    if(isRightMouseButton(e)){


                        sourceTile = null;
                        humanMovedPiece = null;


                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getPiece(tileId);
                            humanMovedPiece = sourceTile;
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {

                            final Movement movement = Movement.MoveFactory.createMove(chessBoard, sourceTile.getPiecePosition(), tileId);
                            final NewMove transition = chessBoard.currentPlayer().makeMove(movement);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getToBoard();
                                moveLog.addMove(movement);
                                ButtonUtils b = new ButtonUtils();
                                b.playSound("sounds/chessMove.wav");
                                buttonMenu.flip();
                                if(BoardHandler.isEndGame(ChessBoard.get().getGameBoard())) {
                                    System.out.println("End game");
                                    new GameOverScreen(chessBoard.currentPlayer());
                                    stopSoundLoop();
                                    playSound("sounds/winner.wav");
                                    return;
                                }
                                if(BoardHandler.isEndGameStale(ChessBoard.get().getGameBoard())) {
                                    System.out.println("End game");
                                    new Draw();
                                    stopSoundLoop();
                                    playSound("sounds/winner.wav");
                                    return;
                                }
                            }
                            sourceTile = null;

                            humanMovedPiece = null;
                        }

                    }

                    SwingUtilities.invokeLater(() -> {
                        moveHistory.redo(chessBoard,moveLog);
                        takenPieces.redo(moveLog);
                        boardPanel.drawBoard(chessBoard);

                    });


                }


                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });




            validate();

        }

        public void drawTile (final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
        }

        private void highLightLegals(final Board board){
            if(highlightLegalMoves) {
                for(final Movement movement : pieceLegalMoves(board)){
                    if(movement.getSecondCoordinate() == this.tileId) {
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/paw.png")))));
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

        private Collection<Movement> pieceLegalMoves(final Board board){
            if(humanMovedPiece != null && humanMovedPiece.getPieceAllegiance() == board.currentPlayer().getAlliance()){
                return  humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getPiece(this.tileId)!=null){
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                            board.getPiece(this.tileId).getPieceAllegiance().toString().charAt(0) +
                            board.getPiece(this.tileId).toString() +".gif"));

                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        private void assignTileColor() {
            if (BoardHandler.INSTANCE.FIRST_ROW.get(this.tileId) ||
                    BoardHandler.INSTANCE.THIRD_ROW.get(this.tileId) ||
                    BoardHandler.INSTANCE.FIFTH_ROW.get(this.tileId) ||
                    BoardHandler.INSTANCE.SEVENTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardHandler.INSTANCE.SECOND_ROW.get(this.tileId) ||
                    BoardHandler.INSTANCE.FOURTH_ROW.get(this.tileId) ||
                    BoardHandler.INSTANCE.SIXTH_ROW.get(this.tileId) ||
                    BoardHandler.INSTANCE.EIGHTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);

            }
        }
    }
    public void playSoundLoop (){
        try{


            clip= AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void stopSoundLoop (){
        try{



            clip.stop();


        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void playSound (String soundName){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip= AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
