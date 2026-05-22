package seng201.nunbutblood.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrusadeServiceTest {

    @Test
    void checkVictoryStartsFalse() {
        CrusadeService service = new CrusadeService();

        assertFalse(service.checkVictory());
    }

    @Test
    void currentStepStartsAtZero() {
        CrusadeService service = new CrusadeService();

        assertEquals(0, service.getCurrentStep());
    }

    @Test
    void totalStepsShouldBeThree() {
        CrusadeService service = new CrusadeService();

        assertEquals(3, service.getTotalSteps());
    }

    @Test
    void logContainsRollInstructions() {
        CrusadeService service = new CrusadeService();

        assertTrue(service.getLog().contains("Roll higher 3 times"));
    }

    @Test
    void gameOverIsTrueWhenPartyIsEmpty() {
        CrusadeService service = new CrusadeService();

        assertTrue(service.checkGameOver(List.of()));
    }

    @Test
    void logStartsWithRollMessage() {
        CrusadeService service = new CrusadeService();

        String log = service.getLog();

        assertTrue(log.startsWith("Click 'ROLL'"));
    }
}