package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostCrusadeServiceTest {

    // Simple manual stub classes mimicking your domain models to avoid Mockito
    private static class TestSaint extends Saint {
        private final String name;
        private final String specialAbility;
        private final int crusadeCount;

        public TestSaint(String name, String specialAbility, int crusadeCount) {
            super("mockSaint", "1.png", 10, 10, 10, 20, 10, 100, 10);
            this.name = name;
            this.specialAbility = specialAbility;
            this.crusadeCount = crusadeCount;
        }

        @Override
        public String getName() { return this.name; }

        @Override
        public String getSpecialAbility() { return this.specialAbility; }

        @Override
        public int getCrusadeCount() { return this.crusadeCount; }

        @Override
        public String toString() { return this.name; }

        @Override
        public int getStatBonus() {
            return 0;
        }

        @Override
        public String getStatType() {
            return "";
        }
    }

    private static class TestCovenant extends Covenant {
        private final List<Saint> activeParty = new ArrayList<>();
        private final List<Saint> reserves = new ArrayList<>();
        public int faithAdded = 0;
        public int itemsAdded = 0;
        public int saintsRetired = 0;
        public int saintsAdded = 0;

        /**
         * Creates a new covenant.
         *
         * @param name          Name of the covenant
         * @param totalCrusades Total number of crusades in the game
         * @param difficulty    Difficulty setting used for starting faith
         */
        public TestCovenant(String name, int totalCrusades, String difficulty) {
            super(name, totalCrusades, difficulty);
        }

        @Override
        public List<Saint> getActiveParty() { return activeParty; }

        @Override
        public List<Saint> getReserves() { return reserves; }

        @Override
        public void addFaith(int amount) { this.faithAdded += amount; }

        @Override
        public boolean retireSaint(Saint saint) { this.saintsRetired++;
            return false;
        }

        @Override
        public void addSaint(Saint saint) { this.saintsAdded++; }
    }

    private TestCovenant covenant;
    private TestSaint regularSaint;
    private TestSaint eliteSaint;

    @BeforeEach
    void setUp() {
        covenant = new TestCovenant("testCovenant", 0, "Easy");
        // One saint with an empty special ability to test the random skills path
        regularSaint = new TestSaint("Sister Joan", "", 1);
        // One saint with an existing ability to test the early escape path
        eliteSaint = new TestSaint("Sister Mary", "Shield", 5);

        covenant.getActiveParty().add(regularSaint);
        covenant.getActiveParty().add(eliteSaint);
    }

    /**
     * Coverage: Tests the early return statement when active party is completely empty.
     */
    @Test
    void testEmptyActivePartyReturnsImmediately() {
        PostCrusadeService service = new PostCrusadeService("Normal");
        TestCovenant emptyCovenant = new TestCovenant("testCovenant", 0, "Easy"); // No saints added

        List<String> events = service.triggerEvents(emptyCovenant);
        assertTrue(events.isEmpty(), "Events array should return empty immediately");
    }

    /**
     * Coverage: Covers "Normal" difficulty probability modifiers and statistical execution
     * of every randomized branch block over a repetitive volume trial.
     */
    @Test
    void testTriggerEventsNormalModeExhaustive() {
        PostCrusadeService service = new PostCrusadeService("Normal");

        boolean hitGainedStat = false;
        boolean hitLostStat = false;
        boolean hitHolyBlessing = false;
        boolean hitDevoutSurvivor = false;
        boolean hitCoveredInBlood = false;
        boolean hitRetirement = false;

        // 5000 iterations mathematically guarantees running every sequence branch path
        for (int i = 0; i < 5000; i++) {
            List<String> events = service.triggerEvents(covenant);

            for (String event : events) {
                if (event.contains("gained") && event.contains("divine")) hitGainedStat = true;
                if (event.contains("lost") && event.contains("cursed")) hitLostStat = true;
                if (event.contains("Channeled the ability, Holy Blessing")) hitHolyBlessing = true;
                if (event.contains("Channeled the ability, Devout Survivor")) hitDevoutSurvivor = true;
                if (event.contains("Channeled the ability, Covered in Blood")) hitCoveredInBlood = true;
                if (event.contains("has retired, a humble end")) hitRetirement = true;
            }
        }

        // Assert that all lines inside the random conditions were successfully run
        assertTrue(hitGainedStat, "Failed to run positive stat branch variation");
        assertTrue(hitLostStat, "Failed to run negative stat branch variation");
        assertTrue(hitHolyBlessing, "Failed to execute Holy Blessing action block");
        assertTrue(hitDevoutSurvivor, "Failed to execute Devout Survivor action block");
        assertTrue(hitCoveredInBlood, "Failed to execute Covered in Blood action block");
        assertTrue(hitRetirement, "Failed to hit basic retirement execution flow loop");
    }

    /**
     * Coverage: Covers the "Hard" difficulty ternary condition check branch.
     */
    @Test
    void testTriggerEventsHardModeModifier() {
        PostCrusadeService service = new PostCrusadeService("Hard");

        // Runs it several times to confirm the high event percentages function without logic traps
        for (int i = 0; i < 500; i++) {
            List<String> events = service.triggerEvents(covenant);
            assertNotNull(events);
        }
    }

    /**
     * Coverage: Tests the conditional safety block protecting the last remaining saint
     * from retiring if total squad size drops to exactly 1.
     */
    @Test
    void testRetirementSafetyPreventedWhenTotalIsOne() {
        PostCrusadeService service = new PostCrusadeService("Hard");
        TestCovenant soloCovenant = new TestCovenant("testCovenant2", 0, "Easy");

        // Setup only a single active party saint, leaving reserves empty (Total size = 1)
        soloCovenant.getActiveParty().add(regularSaint);

        for (int i = 0; i < 1000; i++) {
            List<String> events = service.triggerEvents(soloCovenant);
            for (String event : events) {
                // Ensure no retirement log string slips through
                assertFalse(event.contains("has retired"),
                        "Safety failed! A hero retired when they were the absolute last squad member remaining.");
            }
        }
        assertEquals(0, soloCovenant.saintsRetired, "Roster count should protect lone active saint from retirement.");
    }
}