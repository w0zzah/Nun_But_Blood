package seng201.nunbutblood.services;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;


/**
 * <h1>Audio Service: * </h1>
 *
 * <p>Creates an Enum where all audio clips are found for shorter sound effects such as dice roll.</p>
 * <p>(can be disabled with setting enabled to false)</p>
 */
public class AudioService {


    /**
     * The enum Clip.
     */
    public enum Clip {
        /**
         * Critical success clip.
         */
        CRITICAL_SUCCESS,
        /**
         * Success clip.
         */
        SUCCESS,
        /**
         * Fail clip.
         */
        FAIL,
        /**
         * Critical fail clip.
         */
        CRITICAL_FAIL,
        /**
         * Rolling clip.
         */
        ROLLING
    }

    public enum Track {
        CRUSADE,
        HUB
    }

    // Create an EnumMap to call each audio clip of o(1)
    private final Map<Clip, AudioClip> clips = new EnumMap<>(Clip.class);
    private boolean enabled = true; // To disable audio


    private MediaPlayer currentMusicPlayer;
    private Track currentTrack;
    private double musicVolume = .4;


    /**
     * Instantiates a new Audio service.
     */
    public AudioService() {
        // Load assets into memory to prevent pulling each time
        loadDiceRollSound(Clip.CRITICAL_SUCCESS, "/audio/critical_success.wav");
        loadDiceRollSound(Clip.SUCCESS,          "/audio/success.wav");
        loadDiceRollSound(Clip.FAIL,             "/audio/fail.wav");
        loadDiceRollSound(Clip.CRITICAL_FAIL,    "/audio/critical_fail.wav");
        loadDiceRollSound(Clip.ROLLING,          "/audio/rolling.wav");
    }

    /**
     * Play.
     *
     * @param clip the clip
     */
    public void play(Clip clip) {
        // check if audio is enabled or if the clip exists
        if (!enabled || !clips.containsKey(clip)) return;

        // Stop's overlap of audio
        clips.get(clip).stop();
        clips.get(clip).play(volumeFor(clip));
    }

    /**
     * Stop.
     *
     * @param clip the clip
     */
    public void stop(Clip clip) {
        if (clips.containsKey(clip)) clips.get(clip).stop();
    }

    /**
     * Finds and caches the audio asset from resources if it exists.
     *
     * @param key the designated clip identifier
     * @param path target path to the resource file
     */
    private void load(Clip key, String path) {

    }
    public void playMusic(Track track) {
        if (!enabled) return;
        if (currentTrack == track && currentMusicPlayer != null) return;

        stopMusic();

        String path = switch (track) {
            case HUB -> "/audio/hub.mp3";
            case CRUSADE -> "/audio/crusade.mp3";
        };

        URL url = getClass().getResource(path);

        if (url == null) return;

        Media media = new Media(url.toExternalForm());
        currentMusicPlayer = new MediaPlayer(media);
        currentTrack = track;

        currentMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentMusicPlayer.setVolume(musicVolume);
        currentMusicPlayer.play();
    }

    public void stopMusic() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer.dispose(); // Free all resources associated with player. check docs
            currentMusicPlayer = null;
            currentTrack = null;
        }
    }
    /**
     * Finds and caches the audio asset from resources if it exists.
     *
     * @param key the designated clip identifier
     * @param path target path to the resource file
     */
    private void loadDiceRollSound(Clip key, String path) {
        URL url = getClass().getResource(path);
        // Check the audio exists, if so assign the EnumMap key to audio wav
        if (url != null) {
            clips.put(key, new AudioClip(url.toExternalForm()));
        }
    }

    /**
     * Controls mixing levels for specific sound effects.
     *
     * @param clip the clip being played
     * @return double value between 0.0 and 1.0 representing relative gain
     */
    private double volumeFor(Clip clip) {
        // Balance the audio, may add setting for fx music etc.
        return switch(clip) {
            default -> 0.7;
            case ROLLING -> 0.5;
            case CRITICAL_FAIL, CRITICAL_SUCCESS -> 1;
        };
    }
 }
