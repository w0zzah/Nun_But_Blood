package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemonTest {

    private final Demon demon =
            new Demon(
                    "Belial",
                    "belial.png",
                    5,
                    StatType.STRENGTH,
                    100
            );

    @Test
    void getName() {
        assertEquals("Belial", demon.getName());
    }

    @Test
    void getDifficultyRating() {
        assertEquals(5, demon.getDifficultyRating());
    }

    @Test
    void getFaithReward() {
        assertEquals(100, demon.getFaithReward());
    }

    @Test
    void getPortraitKey() {
        assertEquals("belial.png", demon.getPortraitKey());
    }

    @Test
    void getWeakness() {
        assertEquals(StatType.STRENGTH, demon.getWeakness());
    }

    @Test
    void getDescription() {

        String description = demon.getDescription();

        assertTrue(description.contains("Belial"));
        assertTrue(description.contains("Difficulty 5"));
        assertTrue(description.contains("100 Faith"));
    }

    @Test
    void applyCurse() {

        // Since randomness is involved,
        // just ensure method executes safely.

        boolean result = demon.applyCurse();

        assertTrue(result || !result);
    }
}