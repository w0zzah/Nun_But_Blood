package seng201.nunbutblood.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaintsTest {

    private Executioner executioner;
    private Oracle oracle;
    private Inquisitor inquisitor;

    @BeforeEach
    void setUp() {
        executioner = new Executioner(
                "Saint Germain",
                "Germain.png",
                20,
                10,
                0,
                20
        );

        oracle = new Oracle(
                "Saint Oracle",
                "oracle.png",
                10,
                20,
                5,
                15
        );

        inquisitor = new Inquisitor(
                "Saint Inquisitor",
                "inq.png",
                15,
                5,
                20,
                18
        );
    }

    @Test
    void getName() {
        assertEquals("Saint Germain", executioner.getName());
    }

    @Test
    void getMaxStamina() {
        assertTrue(executioner.getMaxStamina() > 0);
    }

    @Test
    void getCurrentStamina() {
        assertEquals(
                executioner.getMaxStamina(),
                executioner.getCurrentStamina()
        );
    }

    @Test
    void getStrength() {
        assertEquals(20, executioner.getStrength());
    }

    @Test
    void getWisdom() {
        assertEquals(10, executioner.getWisdom());
    }

    @Test
    void getDexterity() {
        assertEquals(0, executioner.getDexterity());
    }

    @Test
    void getPerception() {
        assertEquals(20, executioner.getPerception());
    }

    @Test
    void getStatTotal() {
        int expected =
                executioner.getStrength()
                        + executioner.getWisdom()
                        + executioner.getDexterity();

        assertEquals(expected, executioner.getStatTotal());
    }

    @Test
    void getPortraitKey() {
        assertEquals("Germain.png", executioner.getPortraitKey());
    }

    @Test
    void getStatBonus() {
        assertEquals(
                executioner.getStrength(),
                executioner.getStatBonus()
        );
    }

    @Test
    void getStatType() {
        assertEquals("STR", executioner.getStatType());
        assertEquals("WIS", oracle.getStatType());
        assertEquals("DEX", inquisitor.getStatType());
    }

    @Test
    void changeStrength() {
        executioner.changeStrength(5);

        assertEquals(20, executioner.getStrength());
    }

    @Test
    void changeWisdom() {
        executioner.changeWisdom(3);

        assertEquals(13, executioner.getWisdom());
    }

    @Test
    void changeDexterity() {
        executioner.changeDexterity(7);

        assertEquals(7, executioner.getDexterity());
    }

    @Test
    void consumeStamina() {
        int before = executioner.getCurrentStamina();

        executioner.consumeStamina(5);

        assertEquals(before - 5, executioner.getCurrentStamina());
    }

    @Test
    void restoreStamina() {
        executioner.consumeStamina(10);

        executioner.restoreStamina();

        assertEquals(
                executioner.getMaxStamina(),
                executioner.getCurrentStamina()
        );
    }

    @Test
    void changeMaxStamina() {
        int before = executioner.getMaxStamina();

        executioner.changeMaxStamina(5);

        assertEquals(before + 5, executioner.getMaxStamina());
    }

    @Test
    void changePerception() {
        executioner.changePerception(5);

        assertEquals(25, executioner.getPerception());
    }

    @Test
    void setSpecialAbility() {
        executioner.setSpecialAbility("Holy Flame");

        assertEquals(
                "Holy Flame",
                executioner.getSpecialAbility()
        );
    }

    @Test
    void getAttributeValue() {
        assertEquals(20, executioner.getAttributeValue(StatType.STRENGTH));
        assertEquals(10, executioner.getAttributeValue(StatType.WISDOM));
        assertEquals(0, executioner.getAttributeValue(StatType.DEXTERITY));
    }

    @Test
    void incrementCrusadeCount() {
        int before = executioner.getCrusadeCount();

        executioner.incrementCrusadeCount();

        assertEquals(before + 1, executioner.getCrusadeCount());
    }

    @Test
    void resetCrusadeCount() {
        executioner.incrementCrusadeCount();
        executioner.incrementCrusadeCount();

        executioner.resetCrusadeCount();

        assertEquals(0, executioner.getCrusadeCount());
    }

    @Test
    void testToString() {
        assertNotNull(executioner.toString());
        assertFalse(executioner.toString().isEmpty());
    }

    @Test
    void getRarity() {
        assertNotNull(executioner.getRarity());
    }

    @Test
    void getCost() {
        assertTrue(executioner.getCost() >= 0);
    }

    @Test
    void getDescription() {
        assertNotNull(executioner.getDescription());
    }

    @Test
    void getUpKeep() {
        assertTrue(executioner.getUpKeep() >= 0);
    }

    @Test
    void getSpecialAbility() {
        executioner.setSpecialAbility("Smite");

        assertEquals("Smite", executioner.getSpecialAbility());
    }
}