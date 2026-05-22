package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Executioner;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MidCrusadeEventsTest {

    private MidCrusadeEvents midCrusadeEvents;
    private List<Saint> testParty;
    private Saint saintA;
    private Saint saintB;

    @BeforeEach
    void setUp() {

        midCrusadeEvents = new MidCrusadeEvents();
        saintA = new Executioner("saintA", "portrait.png", 10, 10, 10, 20);
        saintB = new Executioner("saintB", "portrait.png", 10, 10, 10, 20);

        testParty = new ArrayList<>();
        testParty.add(saintA);
        testParty.add(saintB);
    }

    /**
     * Coverage for getRandomEvent(), runs through event 1000 times
     */
    @Test
    void testAllRandomEventsBruteForce() {
        Set<String> uniqueDescriptions = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            MidCrusadeEvents.Event event = midCrusadeEvents.getRandomEvent(testParty);

            assertNotNull(event);
            assertNotNull(event.description());
            assertNotNull(event.choiceA());
            assertNotNull(event.choiceB());

            uniqueDescriptions.add(event.description());
        }

        // Verify that all 15 unique event descriptions were encountered
        assertEquals(15, uniqueDescriptions.size(),
                "The random loop failed to hit all 15 event variations. Try increasing the iteration loop bound.");
    }

    /**
     * Coverage for applyChoice() when amount is positive.
     * Ensures the "+" branch of the ternary operator runs.
     */
    @Test
    void testApplyChoicePositiveAmount() {
        MidCrusadeEvents.Choice positiveChoice = new MidCrusadeEvents.Choice(
                "Positive outcome description",
                StatType.WISDOM,
                3,
                saintA
        );

        String result = midCrusadeEvents.applyChoice(testParty, positiveChoice);

        // Verifies the exact string format with the '+' sign
        assertEquals("saintA's WIS changed by +3.", result);
    }

    /**
     * Coverage for applyChoice() when amount is negative.
     * Ensures the default branch of the ternary operator runs.
     */
    @Test
    void testApplyChoiceNegativeAmount() {
        MidCrusadeEvents.Choice negativeChoice = new MidCrusadeEvents.Choice(
                "Negative outcome description",
                StatType.SANITY,
                -2,
                saintB
        );

        String result = midCrusadeEvents.applyChoice(testParty, negativeChoice);

        // Verifies string format for negatives (retains standard '-' symbol)
        assertEquals("saintB's STA changed by -2.", result);
    }
}