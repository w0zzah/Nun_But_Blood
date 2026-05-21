package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Executioner;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MidCrusadeEventsTest {

    private MidCrusadeEvents events;

    @BeforeEach
    void setUp() {
        events = new MidCrusadeEvents();
    }

    @Test
    void getRandomEventReturnsEvent() {
        MidCrusadeEvents.Event event = events.getRandomEvent();

        assertNotNull(event);
        assertNotNull(event.description());
        assertNotNull(event.choiceA());
        assertNotNull(event.choiceB());
    }

    @Test
    void getRandomEventChoicesHaveStats() {
        MidCrusadeEvents.Event event = events.getRandomEvent();

        assertNotNull(event.choiceA().stat());
        assertNotNull(event.choiceB().stat());
    }

    @Test
    void applyChoiceWithEmptyParty() {
        List<Saint> party = new ArrayList<>();

        MidCrusadeEvents.Choice choice =
                new MidCrusadeEvents.Choice("Test choice", StatType.STRENGTH, 2);

        String result = events.applyChoice(party, choice);

        assertEquals("No saints were affected.", result);
    }

    @Test
    void applyChoiceWithPartyReturnsMessage() {
        List<Saint> party = new ArrayList<>();
        Saint saint = new Executioner("Test Saint", "test.png", 10, 5, 5, 20);
        party.add(saint);

        MidCrusadeEvents.Choice choice =
                new MidCrusadeEvents.Choice("Gain strength", StatType.STRENGTH, 2);

        String result = events.applyChoice(party, choice);

        assertTrue(result.contains("Test Saint"));
        assertTrue(result.contains("changed by +2"));
    }

    @Test
    void choiceStoresValuesCorrectly() {
        MidCrusadeEvents.Choice choice =
                new MidCrusadeEvents.Choice("Gain wisdom", StatType.WISDOM, 3);

        assertEquals("Gain wisdom", choice.text());
        assertEquals(StatType.WISDOM, choice.stat());
        assertEquals(3, choice.amount());
    }

    @Test
    void eventStoresValuesCorrectly() {
        MidCrusadeEvents.Choice choiceA =
                new MidCrusadeEvents.Choice("A", StatType.STRENGTH, 1);

        MidCrusadeEvents.Choice choiceB =
                new MidCrusadeEvents.Choice("B", StatType.WISDOM, -1);

        MidCrusadeEvents.Event event =
                new MidCrusadeEvents.Event("Test event", choiceA, choiceB);

        assertEquals("Test event", event.description());
        assertEquals(choiceA, event.choiceA());
        assertEquals(choiceB, event.choiceB());
    }
}