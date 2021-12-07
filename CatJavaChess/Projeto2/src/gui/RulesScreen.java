package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RulesScreen extends JFrame {


   public RulesScreen(){

       setSize(525, 510);
       setTitle("Game rules");
       getContentPane().setBackground(Color.WHITE);
       getContentPane().setLayout(null);
       JLabel lblNewLabel_2 = new JLabel("");
       lblNewLabel_2.setIcon(new ImageIcon("art/Spin.gif"));
       lblNewLabel_2.setBounds(5, 376, 50, 50);
       getContentPane().add(lblNewLabel_2);
       JLabel lblNewLabel_2_1 = new JLabel("");
       lblNewLabel_2_1.setIcon(new ImageIcon("art/Spin.gif"));
       lblNewLabel_2_1.setBounds(455, 376, 50, 50);
       getContentPane().add(lblNewLabel_2_1);
       JButton btnLink = new JButton("");
       btnLink.setBounds(66, 382, 364, 47);
       btnLink.setOpaque(true);
       btnLink.setContentAreaFilled(false);
       btnLink.setBorderPainted(false);
       getContentPane().add(btnLink);
       JLabel lblText = new JLabel("Click here to read all the rules");
      btnLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      btnLink.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               try {

                   Desktop.getDesktop().browse(new URI("https://www.chesshouse.com/pages/chess-rules"));

               } catch (IOException | URISyntaxException e1) {
                   e1.printStackTrace();
               }           }

           @Override
           public void mouseEntered(MouseEvent e) {
               // the mouse has entered the label
           }

           @Override
           public void mouseExited(MouseEvent e) {
               // the mouse has exited the label
           }
       });
       lblText.setForeground(new Color(147, 112, 219));
       lblText.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
       lblText.setBounds(100, 380, 378, 65);
       getContentPane().add(lblText);
       JLabel lblImg = new JLabel("");
       lblImg.setIcon(new ImageIcon("art/rules3.png"));
       lblImg.setBounds(0, 0, 525, 373);
       getContentPane().add(lblImg);
       JButton okButton = new JButton("Ok");
       okButton.setBounds(220,440,80,40);
       okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       getContentPane().add(okButton);
       okButton.addActionListener(e ->{
           dispose();
       });


   }



}
