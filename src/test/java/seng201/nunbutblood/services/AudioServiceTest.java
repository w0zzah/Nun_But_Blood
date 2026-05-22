package seng201.nunbutblood.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioServiceTest {

    @Test
    void audioServiceCanBeCreated() {
        AudioService audioService = new AudioService();

        assertNotNull(audioService);
    }

    @Test
    void playDoesNotCrash() {
        AudioService audioService = new AudioService();

        assertDoesNotThrow(() ->
                audioService.play(AudioService.Clip.SUCCESS)
        );
    }

    @Test
    void stopDoesNotCrash() {
        AudioService audioService = new AudioService();

        assertDoesNotThrow(() ->
                audioService.stop(AudioService.Clip.SUCCESS)
        );
    }
}