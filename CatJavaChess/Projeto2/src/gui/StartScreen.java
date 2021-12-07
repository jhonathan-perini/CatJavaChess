package gui;



import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartScreen extends JFrame {


    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 338);



    public StartScreen() {

        final JButton startButton = new JButton("Press here to start");
        startButton.setBounds(150,280,300,20);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setForeground(Color.WHITE);
        add(startButton, BorderLayout.CENTER);


        setSize(OUTER_FRAME_DIMENSION);
        assignBackground();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Table table = new Table();
                dispose();
            }
        });

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
}


