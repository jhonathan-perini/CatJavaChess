package gui;



import com.chess.engine.player.Player;

import javax.swing.*;
import java.awt.*;


import static gui.ChessBoard.gameFrame;

public class GameOverScreen extends JFrame {


    public GameOverScreen(final Player player) {


        setSize(400, 421);
        getContentPane().setBackground(new Color(153, 51, 255));
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        JButton btnNewGame = new JButton("");
        btnNewGame.setBounds(16, 314, 100, 62);
        getContentPane().add(btnNewGame);

        JButton btnExitGame = new JButton("");
        btnExitGame.setBounds(283, 314, 100, 62);
        getContentPane().add(btnExitGame);

        JLabel lblNewGame = new JLabel("New game");
        lblNewGame.setBounds(26, 338, 71, 16);
        getContentPane().add(lblNewGame);

        JLabel lblExitGame = new JLabel("Exit game");
        lblExitGame.setBounds(307, 338, 71, 16);
        getContentPane().add(lblExitGame);


        btnNewGame.setOpaque(true);
        btnNewGame.setContentAreaFilled(false);
        btnNewGame.setBorderPainted(false);
        btnNewGame.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        btnNewGame.addActionListener(e -> {
            dispose();
            gameFrame.dispose();
            new StartScreen();


        });

        btnExitGame.setOpaque(true);
        btnExitGame.setContentAreaFilled(false);
        btnExitGame.setBorderPainted(false);
        btnExitGame.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        btnExitGame.addActionListener(e -> System.exit(0));



        JLabel lblCheckMateBckg = new JLabel("New label");
        if(player.getAlliance().isWhite()){
            lblCheckMateBckg.setIcon(new ImageIcon("art/CM_W.png"));
        } else {
            lblCheckMateBckg.setIcon(new ImageIcon("art/CM_B.png"));
        }

        lblCheckMateBckg.setBounds(0, 0, 400, 400);
        getContentPane().add(lblCheckMateBckg);

    setVisible(true);
    }

}


