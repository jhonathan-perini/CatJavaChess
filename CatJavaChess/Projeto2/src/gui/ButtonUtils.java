package gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class ButtonUtils {

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
