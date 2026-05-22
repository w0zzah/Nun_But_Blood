package seng201.nunbutblood.services;

import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrusadeServiceTest {

    private Saint createTestSaint(String name, int sanity) {
        return new Saint(
                name,
                "saint.png",
                10,
                8,
                6,
                sanity,
                5,
                200,
                10
        ) {
            @Override
            public int getStatBonus() {
                return 5;
            }

            @Override
            public String getStatType() {
                return "STR";
            }
        };
    }

    private Demon createTestDemon(String name, int difficulty) {
        return new Demon(
                name,
                "demon.png",
                difficulty,
                StatType.STRENGTH,
                100
        );
    }

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
    void logStartsWithRollMessage() {
        CrusadeService service = new CrusadeService();

        assertTrue(service.getLog().startsWith("Click 'ROLL'"));
    }

    @Test
    void gameOverIsTrueWhenPartyIsEmpty() {
        CrusadeService service = new CrusadeService();

        assertTrue(service.checkGameOver(List.of()));
    }

    @Test
    void gameOverIsTrueWhenAllSaintsHaveLowSanity() {
        CrusadeService service = new CrusadeService();

        Saint saint1 = createTestSaint("Gabriel", 4);
        Saint saint2 = createTestSaint("Michael", 2);

        assertTrue(service.checkGameOver(List.of(saint1, saint2)));
    }

    @Test
    void gameOverIsFalseWhenOneSaintHasEnoughSanity() {
        CrusadeService service = new CrusadeService();

        Saint saint1 = createTestSaint("Gabriel", 4);
        Saint saint2 = createTestSaint("Michael", 10);

        assertFalse(service.checkGameOver(List.of(saint1, saint2)));
    }

    @Test
    void performStepReturnsValidResult() {
        CrusadeService service = new CrusadeService();

        Saint saint = createTestSaint("Lucius", 100);
        Demon demon = createTestDemon("Beelzebub", 10);

        CrusadeService.StepResult result =
                service.performStep(
                        saint,
                        demon,
                        List.of(saint),
                        StatType.STRENGTH
                );

        assertNotNull(result);
        assertTrue(result.roll() >= 1 && result.roll() <= 20);
        assertNotNull(result.type());
        assertNotNull(result.narrative());
        assertNotNull(result.logMessage());
    }

    @Test
    void performStepConsumesSanity() {
        CrusadeService service = new CrusadeService();

        Saint saint = createTestSaint("Raphael", 100);
        Demon demon = createTestDemon("Azrael", 8);

        int sanityBefore = saint.getCurrentSanity();

        service.performStep(
                saint,
                demon,
                List.of(saint),
                StatType.STRENGTH
        );

        assertEquals(sanityBefore - 5, saint.getCurrentSanity());
    }

    @Test
    void performStepAddsToLog() {
        CrusadeService service = new CrusadeService();

        Saint saint = createTestSaint("Uriel", 100);
        Demon demon = createTestDemon("Mammon", 12);

        service.performStep(
                saint,
                demon,
                List.of(saint),
                StatType.STRENGTH
        );

        assertTrue(service.getLog().contains("Uriel"));
        assertTrue(service.getLog().contains("Result:"));
    }
}