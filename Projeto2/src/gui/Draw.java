package gui;

import javax.swing.*;
import java.awt.*;


import static gui.ChessBoard.gameFrame;

public class Draw extends JFrame {

    public Draw(){
        setSize(525, 475);
        getContentPane().setBackground(new Color(147, 112, 219));
        getContentPane().setLayout(null);
    setLocationRelativeTo(null);
        JLabel lblNewGame = new JLabel("New Game");
        lblNewGame.setBounds(85, 389, 93, 29);
        getContentPane().add(lblNewGame);

        JButton btnNewGame = new JButton("");
        btnNewGame.setBounds(36, 380, 173, 47);
        getContentPane().add(btnNewGame);

        btnNewGame.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));

        JLabel lblText = new JLabel("");
        lblText.setIcon(new ImageIcon("art/YYDraw.png"));
        lblText.setBounds(0, 25, 248, 323);
        getContentPane().add(lblText);

        JLabel lblExitGame = new JLabel("Exit Game");
        lblExitGame.setBounds(342, 396, 85, 16);
        getContentPane().add(lblExitGame);


        JButton btnExitGame = new JButton("");
        btnExitGame.setBounds(293, 380, 173, 47);

        btnExitGame.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        getContentPane().add(btnExitGame);

        JLabel lblTextDraw = new JLabel("");
        lblTextDraw.setIcon(new ImageIcon("art/drawSc.png"));
        lblTextDraw.setBounds(251, 127, 248, 132);
        getContentPane().add(lblTextDraw);


        btnNewGame.addActionListener(e -> {
            dispose();
            gameFrame.dispose();
            new StartScreen();


        });

        btnExitGame.addActionListener(e -> System.exit(0));
        setVisible(true);
    }
}
