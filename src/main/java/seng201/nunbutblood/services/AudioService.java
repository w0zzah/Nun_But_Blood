package seng201.nunbutblood.services;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;


/**
 * Audio Service: *
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

    // Create an EnumMap to call each audio clip of o(1)
    private final Map<Clip, AudioClip> clips = new EnumMap<>(Clip.class);
    private boolean enabled = true; // To disable audio

    /**
     * Instantiates a new Audio service.
     */
    public AudioService() {
        // Load assets into memory to prevent pulling each time
        load(Clip.CRITICAL_SUCCESS, "/audio/critical_success.wav");
        load(Clip.SUCCESS,          "/audio/success.wav");
        load(Clip.FAIL,             "/audio/fail.wav");
        load(Clip.CRITICAL_FAIL,    "/audio/critical_fail.wav");
        load(Clip.ROLLING,          "/audio/rolling.wav");
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