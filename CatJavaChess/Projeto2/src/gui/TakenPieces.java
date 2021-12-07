package gui;

import com.chess.engine.board.Movement;
import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;
import gui.ChessBoard.MoveLog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.*;
import java.util.List;

class TakenPieces extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;



    @Serial
    private static final long serialVersionUID = 1L;
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_PANEL_DIMENSION = new Dimension(40, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPieces() {
        super(new BorderLayout());
        setBackground(Color.decode("0xFDF5E6"));
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);

        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {
        southPanel.removeAll();
        northPanel.removeAll();

        final List<Piece> whiteTakenTes = new ArrayList<>();
        final List<Piece> blackTakenTes = new ArrayList<>();

        for(final Movement movement : moveLog.getMoves()) {
            if(movement.isAttack()) {
                final Piece takenPiece = movement.getAttackedPiece();
                if(takenPiece.getPieceAllegiance().isWhite()) {
                    whiteTakenTes.add(takenPiece);
                } else if(takenPiece.getPieceAllegiance().isBlack()){
                    blackTakenTes.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        whiteTakenTes.sort((p1, p2) -> Ints.compare(p1.getPieceValue(), p2.getPieceValue()));

        blackTakenTes.sort((p1, p2) -> Ints.compare(p1.getPieceValue(), p2.getPieceValue()));

        for (final Piece takenPiece : whiteTakenTes) {
            try {
                final BufferedImage image = ImageIO.read(new File("art/"
                        + takenPiece.getPieceAllegiance().toString().charAt(0) + "" + takenPiece
                        + ".gif"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 5, ic.getIconWidth() - 5, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenTes) {
            try {
                final BufferedImage image = ImageIO.read(new File("art/"
                        + takenPiece.getPieceAllegiance().toString().charAt(0) + "" + takenPiece
                        + ".gif"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 5, ic.getIconWidth() - 5, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        validate();
    }
}
