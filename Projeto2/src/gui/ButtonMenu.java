package gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;


import java.io.File;
import java.io.Serial;


import static gui.ChessBoard.*;

public class ButtonMenu extends JPanel {


    @Serial
    private static final long serialVersionUID = 1L;
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_PANEL_DIMENSION = new Dimension(80, 40);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public ButtonMenu(String[] name) {
        super(new BorderLayout());

        setBackground(Color.decode("0xFDF5E6"));
        setBorder(PANEL_BORDER);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        JPanel leftPanel = new JPanel(new GridLayout(3, 8));
        JPanel rightPanel = new JPanel(new GridLayout(3, 8));

        JButton highlightButton = new JButton("Highlight Moves");
        JButton drawButton = new JButton("Propose Draw");
        JButton surrenderButton = new JButton("Surrender");
        JButton savePGNButton = new JButton("Save to PGN");
        leftPanel.setBackground(PANEL_COLOR);
        rightPanel.setBackground(PANEL_COLOR);


        savePGNButton.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return ".pgn";
                }
                @Override
                public boolean accept(final File file) {
                    return file.isDirectory() || file.getName().toLowerCase().endsWith("pgn");
                }
            });
            final int option = chooser.showSaveDialog(ChessBoard.get().getGameFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                savePGNFile(chooser.getSelectedFile());
            }
        });



        highlightButton.addActionListener(e -> highlightLegalMoves = !highlightLegalMoves);
        drawButton.addActionListener(e -> {

            final String[] pName = new String[1];

            if(chessBoard.currentPlayer().getAlliance().isWhite()) {
                pName[0] = name[0];
            } else {
                pName[0] = name[1];
            }

                        JFrame drawScreen = new JFrame();
                        drawScreen.setSize(395, 399);
                        drawScreen.setLocationRelativeTo(null);
                        drawScreen.getContentPane().setBackground(new Color(153, 51, 255));
                        drawScreen.getContentPane().setLayout(null);
                        JLabel text2 = new JLabel("        Do you             accept?");
                        text2.setHorizontalAlignment(SwingConstants.LEFT);
                        text2.setForeground(Color.WHITE);
                        text2.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
                        text2.setBounds(0, 67, 400, 49);
                        drawScreen.getContentPane().add(text2);
                        JButton btnYes = new JButton("");
                        btnYes.setBounds(33, 303, 71, 62);
                        btnYes.setOpaque(false);
                        btnYes.setContentAreaFilled(false);
                        btnYes.setBorderPainted(false);
                        btnYes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        drawScreen.getContentPane().add(btnYes);
                        JButton btnNo = new JButton("");
                        btnNo.setBounds(291, 303, 71, 62);
                        btnNo.setOpaque(false);
                        btnNo.setContentAreaFilled(false);
                        btnNo.setBorderPainted(false);
                        btnNo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        drawScreen.getContentPane().add(btnNo);
                        JLabel text1 = new JLabel(pName[0].substring(3).toUpperCase() + " wants a draw. ");
                        text1.setHorizontalAlignment(SwingConstants.CENTER);
                        text1.setForeground(Color.WHITE);
                        text1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
                        text1.setBounds(0, 6, 400, 49);
                        drawScreen.getContentPane().add(text1);
                        JLabel lblImage = new JLabel();
                        lblImage.setIcon(new ImageIcon("art/draw.png"));
                        lblImage.setBounds(0, 0, 400, 373);
                        drawScreen.getContentPane().add(lblImage);
                        drawScreen.setVisible(true);

            btnYes.addActionListener(e1 -> {
                stopSoundLoop();
                drawScreen.dispose();
                new Draw();
            });

            btnNo.addActionListener(e1 -> drawScreen.dispose());


        });

        surrenderButton.addActionListener(e -> {

            JFrame surrenderFrame = new JFrame();
            surrenderFrame.setSize(395, 399);
            surrenderFrame.getContentPane().setBackground(new Color(153, 51, 255));
            surrenderFrame.getContentPane().setLayout(null);

            JButton btnYes = new JButton("");
            btnYes.setBounds(33, 303, 71, 62);
            surrenderFrame.getContentPane().add(btnYes);
            btnYes.setOpaque(false);
            btnYes.setContentAreaFilled(false);
            btnYes.setBorderPainted(false);
            btnYes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JButton noButton = new JButton("");
            noButton.setBounds(291, 303, 71, 62);
            noButton.setOpaque(false);
            noButton.setContentAreaFilled(false);
            noButton.setBorderPainted(false);
            noButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            surrenderFrame.getContentPane().add(noButton);

            JLabel lblBackground = new JLabel();
            lblBackground.setIcon(new ImageIcon("art/surrender.png"));
            lblBackground.setBounds(0, 0, 400, 373);
            surrenderFrame.getContentPane().add(lblBackground);
            surrenderFrame.setLocationRelativeTo(null);
            surrenderFrame.setVisible(true);

         btnYes.addActionListener(e1 -> {
             gameFrame.dispose();
             stopSoundLoop();
             surrenderFrame.dispose();
             new StartScreen();
         });

            noButton.addActionListener(e1 -> surrenderFrame.dispose());



        });
        highlightButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        int width = 150;
        int height = 40;
        Dimension d = new Dimension(width, height);
        highlightButton.setPreferredSize(d);
        highlightButton.setMaximumSize(d);

        drawButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        drawButton.setPreferredSize(d);
        drawButton.setMaximumSize(d);
        savePGNButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        savePGNButton.setPreferredSize(d);
        savePGNButton.setMaximumSize(d);
        surrenderButton.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        surrenderButton.setPreferredSize(d);
        surrenderButton.setMaximumSize(d);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        add(highlightButton);
        add(drawButton);
        add(surrenderButton);
        add(savePGNButton);
        setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
    }

public  void flip (){
    boardDirection = boardDirection.opposite();
    boardPanel.drawBoard(chessBoard);
}

}
