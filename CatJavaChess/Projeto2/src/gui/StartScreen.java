package gui;



import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

import static gui.ChessBoard.*;


public class StartScreen extends JFrame {


    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 338);



    public StartScreen() {

        final BlinkButton startButton = new BlinkButton("Press here to start");
        startButton.setBounds(150,280,300,20);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setEnabled(true);
        startButton.setForeground(Color.WHITE);
        startButton.setBlink(Color.BLACK);
        startButton.start();
        startButton.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        add(startButton, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(OUTER_FRAME_DIMENSION);
        assignBackground();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        startButton.addActionListener(e -> {
            dispose();
            playSound("sounds/cat.wav");
             JTextField txtPlayerOne;
             JTextField txtPlayerTwo;

            JFrame playerScreen = new JFrame();
            playerScreen.setSize(600, 338);
            playerScreen.setLocationRelativeTo(null);
            playerScreen.getContentPane().setBackground(new Color(153, 51, 255));
            playerScreen.getContentPane().setLayout(null);
            playerScreen.setResizable(false);
            txtPlayerOne = new JTextField();
            txtPlayerOne.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
            txtPlayerOne.setBounds(234, 68, 297, 41);
            playerScreen.getContentPane().add(txtPlayerOne);
            txtPlayerOne.setColumns(10);

            txtPlayerTwo = new JTextField();
            txtPlayerTwo.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
            txtPlayerTwo.setBounds(234, 148, 297, 41);
            playerScreen.getContentPane().add(txtPlayerTwo);
            txtPlayerTwo.setColumns(10);

            JLabel lblPlayerOne = new JLabel("Player 1");
            lblPlayerOne.setFont(new Font("Lucida Grande", Font.BOLD, 28));
            lblPlayerOne.setForeground(Color.WHITE);
            lblPlayerOne.setBounds(104, 64, 130, 45);
            playerScreen.getContentPane().add(lblPlayerOne);

            JLabel lblPlayerTwo = new JLabel("Player 2");
            lblPlayerTwo.setFont(new Font("Lucida Grande", Font.BOLD, 28));
            lblPlayerTwo.setForeground(Color.WHITE);
            lblPlayerTwo.setBounds(104, 140, 130, 52);
            playerScreen.getContentPane().add(lblPlayerTwo);

            JLabel lblChooseTheNames = new JLabel("Choose the names");
            lblChooseTheNames.setForeground(Color.WHITE);
            lblChooseTheNames.setFont(new Font("Lucida Grande", Font.BOLD, 28));
            lblChooseTheNames.setBounds(170, 6, 310, 45);
            playerScreen.getContentPane().add(lblChooseTheNames);

            JButton btnPlay = new JButton("");
            btnPlay.setBounds(191, 253, 179, 57);
            btnPlay.setOpaque(false);
            btnPlay.setContentAreaFilled(false);
            btnPlay.setBorderPainted(false);
            btnPlay.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
            playerScreen.getContentPane().add(btnPlay);

            JLabel lblPlay = new JLabel("Play");
            lblPlay.setFont(new Font("Lucida Grande", Font.BOLD, 20));
            lblPlay.setBounds(258, 270, 50, 34);
            playerScreen.getContentPane().add(lblPlay);

            JLabel lblWhiteCat = new JLabel();
            lblWhiteCat.setIcon(new ImageIcon("art/catW.png"));
            lblWhiteCat.setBounds(35, 68, 57, 41);
            playerScreen.getContentPane().add(lblWhiteCat);

            JLabel lblBlackCat = new JLabel();
            lblBlackCat.setIcon(new ImageIcon("art/catB.png"));
            lblBlackCat.setBounds(35, 145, 62, 44);
            playerScreen.getContentPane().add(lblBlackCat);

            JLabel lblPlayButton = new JLabel();
            lblPlayButton.setIcon(new ImageIcon("art/catButton.png"));
            lblPlayButton.setBounds(179, 223, 219, 87);
            playerScreen.getContentPane().add(lblPlayButton);
            playerScreen.setVisible(true);

    btnPlay.addActionListener(e1 -> {
    String[] x = {"W - "+txtPlayerOne.getText(), "B - "+txtPlayerTwo.getText()};

    ChessBoard chessBoard = null;
    try {
        chessBoard = new ChessBoard(x);
    } catch (UnsupportedAudioFileException | IOException ex) {
        ex.printStackTrace();
    }

    INSTANCE = chessBoard;
    startButton.stop();
    playerScreen.dispose();

});


}
        );

    }


    private void assignBackground(){
        try {
            final BufferedImage image = ImageIO.read(new File("bk/bk.png"));
            Image scaled = image.getScaledInstance(600,338,Image.SCALE_SMOOTH);
            add(new JLabel(new ImageIcon(scaled)));
        } catch (IOException e){
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


    public static class BlinkButton extends JButton {
        @Serial
        private static final long serialVersionUID = 0x525350504700000EL;

        private final Timer blinkTimer;
        private boolean blinked;
        private Color blinkColour = Color.black;
        private Color foreground;

        public BlinkButton(String text) {
            this();
            this.setText(text);
        }

        public BlinkButton() {
            super();
            blinkTimer = new Timer(400, e -> {
                blinked = !blinked;
                repaint();
            });
            blinkTimer.setRepeats(true);
            foreground = getForeground();
        }


        public void setForeground(Color foreground) {
            this.foreground = foreground;
            if (!blinked)
                super.setForeground(foreground);
        }


        public void setBlink(Color blink) {
            this.blinkColour = blink;
        }

        public void paintComponent(Graphics g) {

            if (blinked) {
                super.setForeground(foreground);
            } else {
                super.setForeground(blinkColour);
            }
            super.paintComponent(g);
        }

        public void setEnabled(boolean enable) {
            super.setEnabled(enable);
            if (enable)
                start();
            else
                stop();
        }


        public void start() {
            blinkTimer.start();
        }

        public void stop() {
            super.setForeground(foreground);
            blinked = false;
            blinkTimer.stop();
        }


    }

}


