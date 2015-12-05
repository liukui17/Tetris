package gui;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
	Clip clip;
	AudioInputStream audioIn;
	
	public MusicPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File("bgm.wav");
		audioIn = AudioSystem.getAudioInputStream(soundFile);
		 
        // Get a sound clip
        clip = AudioSystem.getClip();
	}
	
	/*
	 * Starts the music. Loops indefinitely
	 */
	public void start() throws LineUnavailableException, IOException {
        clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
