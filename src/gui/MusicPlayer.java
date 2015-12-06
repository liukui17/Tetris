package gui;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicPlayer {
	private Clip clip;
	private AudioInputStream audioIn;

	private static final String BGM = "bgm.wav";

	public MusicPlayer() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL url = classLoader.getResource(BGM);
			File soundFile = new File(url.getPath());
			audioIn = AudioSystem.getAudioInputStream(soundFile);

			// Get a sound clip
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Starts the music. Loops indefinitely
	 */
	public void start() {
		try {
			if (!clip.isActive()) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Stops the music
	 */
	public void stop() {
		clip.stop();
	}

	/*
	 * Adjusts the volume of the clip
	 * Positive float to increase, negative float to decrease
	 */
	public void adjustVolume(Float change) {
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

		float newValue = volume.getValue() + change;
		
		if (newValue < volume.getMaximum() && newValue > volume.getMinimum()) {
			volume.setValue(newValue);
		}
	}
}
