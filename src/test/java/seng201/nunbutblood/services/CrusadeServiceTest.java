package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrusadeServiceTest {

    private static class TestSaint extends Saint {
        private int sanity = 50;

        /**
         * Constructs a new Saint with clamped base attributes and sets initial combat metadata.
         *
         * @param name        Unique display name
         * @param portraitKey Reference key matching assets in resources/images/Saints/
         * @param strength    Physical damage and physical check stat (0-20)
         * @param wisdom      Occult affinity and mystical check stat (0-20)
         * @param dexterity   Finesse and agility check stat (0-20)
         * @param sanity      Maximum and starting sanity pool
         * @param perception  Chance coefficient for post-crusade looting
         * @param hireCost    Faith cost up front to hire from the pool
         * @param upKeep      Faith expense required per active crusade mission
         */
        public TestSaint(String name, String portraitKey, int strength, int wisdom, int dexterity, int sanity, int perception, int hireCost, int upKeep) {
            super(name, portraitKey, strength, wisdom, dexterity, sanity, perception, hireCost, upKeep);
        }

        @Override
        public String getName() { return "TestSaint"; }

        @Override
        public int getAttributeValue(StatType stat) {
            return 5; // Static +5 modifier bonus for math evaluation
        }

        @Override
        public void consumeSanity(int amount) {
            this.sanity -= amount;
        }

        @Override
        public int getCurrentSanity() {
            return this.sanity;
        }

        @Override
        public int getStatBonus() {
            return 0;
        }

        @Override
        public String getStatType() {
            return "";
        }
    }

    private static class TestDemon extends Demon {
        /**
         * Creates a Demon with the given combat attributes.
         *
         * @param name             display name of the demon
         * @param portraitKey      the portrait key
         * @param difficultyRating combat difficulty (higher = harder)
         * @param weakness         stat type this demon is weak to ("STR", "WIS", "DEX")
         * @param faithReward      faith awarded on defeat
         */
        public TestDemon(String name, String portraitKey, int difficultyRating, StatType weakness, int faithReward) {
            super(name, portraitKey, difficultyRating, weakness, faithReward);
        }

        @Override
        public int getDifficultyRating() {
            return 15; // Requires a roll + bonus total >= 15 to pass
        }
    }

    private CrusadeService service;
    private TestSaint mainSaint;
    private List<Saint> party;
    private TestDemon enemyDemon;

    @BeforeEach
    void setUp() {
        service = new CrusadeService();
        mainSaint = new TestSaint("mockSaint", "1.png", 10, 10, 10, 20, 10, 100, 10);
        enemyDemon = new TestDemon("mockDemon", "firehaunt.gif", 10, StatType.STRENGTH, 200);

        party = new ArrayList<>();
        party.add(mainSaint);
    }

    /**
     * Coverage: Covers basic getters, initial state constants, and the log configuration.
     */
    @Test
    void testInitialStateAndGetters() {
        assertEquals(0, service.getCurrentStep());
        assertEquals(3, service.getTotalSteps());
        assertNotNull(service.getLog());
        assertTrue(service.getLog().contains("Click 'ROLL' to roll the die."));
        assertFalse(service.checkVictory());
    }

    /**
     * Coverage: Exhaustively iterates performStep until every switch case, ternary path,
     * step result string outcome, and demon event flag branch is hit.
     */
    @Test
    void testPerformStepExhaustivePaths() {
        boolean hitCriticalSuccess = false;
        boolean hitCriticalFail = false;
        boolean hitStandardSuccess = false;
        boolean hitStandardFail = false;
        boolean hitDemonEventTrue = false;
        boolean hitDemonEventFalse = false;

        // 3000 steps completely covers a 1-to-20 distribution sample hundreds of times over
        for (int i = 0; i < 3000; i++) {
            // Keep refreshing sanity so the saint doesn't break domain logic constraints
            mainSaint.sanity = 100;

            CrusadeService.StepResult result = service.performStep(mainSaint, enemyDemon, party, StatType.STRENGTH);

            assertNotNull(result);
            assertNotNull(result.logMessage());

            // Capture narrative states (covers line 50-61 switch blocks)
            switch (result.narrative()) {
                case "CRITICAL SUCCESS" -> hitCriticalSuccess = true;
                case "CRITICAL FAIL" -> hitCriticalFail = true;
                case "SUCCESS" -> hitStandardSuccess = true;
                case "FAIL" -> hitStandardFail = true;
            }

            // Capture demon event branches (covers condition: roll < 10)
            if (result.hasDemonEvent()) {
                hitDemonEventTrue = true;
            } else {
                hitDemonEventFalse = true;
            }
        }

        // Validate that all lines and structural branches inside performStep were hit
        assertTrue(hitCriticalSuccess, "Critical Success (roll 20) path was never reached.");
        assertTrue(hitCriticalFail, "Critical Fail (roll 1) path was never reached.");
        assertTrue(hitStandardSuccess, "Standard Success path was never reached.");
        assertTrue(hitStandardFail, "Standard Fail path was never reached.");
        assertTrue(hitDemonEventTrue, "Demon event true path (roll < 10) was never reached.");
        assertTrue(hitDemonEventFalse, "Demon event false path (roll >= 10) was never reached.");

        // Verifies strings are actively being shifted to the top of logHistory sequence
        assertTrue(service.getLog().startsWith("[Roll :"));
    }

    /**
     * Coverage: Targets checkVictory boundary calculations (currentStep >= 3).
     */
    @Test
    void testCheckVictoryConditions() {
        assertFalse(service.checkVictory());

        // Artificially trigger steps until the loop rolls high enough to cross totalSteps bound
        for (int i = 0; i < 100; i++) {
            service.performStep(mainSaint, enemyDemon, party, StatType.STRENGTH);
            if (service.checkVictory()) {
                break;
            }
        }
        assertTrue(service.checkVictory(), "Service failed to log victory status when currentStep >= totalSteps");
    }

    /**
     * Coverage: Targets checkGameOver evaluation logic branches.
     */
    @Test
    void testCheckGameOverConditions() {
        // Step 1: Active living squad should not trigger game over state
        mainSaint.sanity = 20;
        assertFalse(service.checkGameOver(party));

        // Step 2: Drop sanity below 5. Should match state filter and return true
        mainSaint.sanity = 4;
        assertTrue(service.checkGameOver(party));

        // Step 3: Verify multi-member evaluation handling (One alive, one dead)
        TestSaint secondSaint = new TestSaint("mockSaint2", "1.png", 10, 10, 10, 20, 10, 100, 10);
        secondSaint.sanity = 15;
        party.add(secondSaint);

        // Should return false because secondSaint still has sanity >= 5
        assertFalse(service.checkGameOver(party));
    }
}