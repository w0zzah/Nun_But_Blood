package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import seng201.nunbutblood.models.Executioner;
import seng201.nunbutblood.models.Oracle;
import seng201.nunbutblood.models.Inquisitor;
import seng201.nunbutblood.models.Rarity;

import static org.junit.jupiter.api.Assertions.*;

class SaintsTest {

    private Executioner executioner;
    private Oracle oracle;
    private Inquisitor inquisitor;

    @BeforeEach
    void setUp() {
        executioner = new Executioner("Saint Germain",  "Germain.png",  20,  10,  0,  20);
    }

    @Test
    void getCost() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void getName() {
    }

    @Test
    void getMaxStamina() {
    }

    @Test
    void getCurrentStamina() {
    }

    @Test
    void getStrength() {
    }

    @Test
    void getWisdom() {
    }

    @Test
    void getDexterity() {
    }

    @Test
    void getPerception() {
    }

    @Test
    void getStatTotal() {
    }

    @Test
    void getUpKeep() {
    }

    @Test
    void getSpecialAbility() {
    }

    @Test
    void getPortraitKey() {
    }

    @Test
    void getRarity() {
    }

    @Test
    void getCrusadeCount() {
    }

    @Test
    void testToString() {
    }

    @Test
    void getStatBonus() {
    }

    @Test
    void getStatType() {
    }

    @Test
    void changeStrength() {
    }

    @Test
    void changeWisdom() {
    }

    @Test
    void changeDexterity() {
    }

    @Test
    void consumeStamina() {
    }

    @Test
    void restoreStamina() {
    }

    @Test
    void changeMaxStamina() {
    }

    @Test
    void changePerception() {
    }

    @Test
    void setSpecialAbility() {
    }

    @Test
    void getAttributeValue() {
        assertEquals(20, executioner.getAttributeValue("strength"));
        assertEquals(10, executioner.getAttributeValue("wisdom"));
        assertEquals(0, executioner.getAttributeValue("dexterity"));
    }

    @Test
    void incrementCrusadeCount() {
    }

    @Test
    void resetCrusadeCount() {
    }
}