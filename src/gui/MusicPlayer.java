package gui;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicPlayer {
	private static final String BGM = "bgm.wav";
	private static final String CRICKETS = "crickets.wav";
	private static final float DEFAULT_VOLUME = 1.0f;
	
	private Clip clip;
	private AudioInputStream audioIn;
	private float volume;

	public MusicPlayer() {
		volume = DEFAULT_VOLUME;
		
		try {
			// Opens the default clip: BGM
			openClip(BGM);
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
		FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

		float newValue = volumeControl.getValue() + change;

		if (newValue < volumeControl.getMaximum() && newValue > volumeControl.getMinimum()) {
			volumeControl.setValue(newValue);
		}
		
		volume = newValue;
	}

	public boolean isPlaying() {
		return clip.isActive() || clip.isRunning();
	}
	
	/*
	 * Plays crickets
	 * If something is already playing, stops it first
	 */
	public void playCrickets() {
		if (isPlaying()) {
			stop();
		}
		
		openClip(CRICKETS);
		start();
	}
	
	/*
	 * Plays BGM
	 * If something is already playing, stops it first
	 */
	public void playBGM() {
		if (isPlaying()) {
			stop();
		}
		
		openClip(BGM);
		start();
	}
	
	/*
	 * Closes the clip
	 */
	public void close() {
		clip.close();
	}
	
	/*
	 * Tries to open the clip with the given name
	 */
	private void openClip(String name) {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL url = classLoader.getResource(name);
			audioIn = AudioSystem.getAudioInputStream(url);
			
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			
			FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(volume);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
