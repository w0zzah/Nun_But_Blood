package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InquisitorTest {

    @Test
    void getStatBonus() {
        Inquisitor inquisitor = new Inquisitor(
                "Test Inquisitor",
                "inquisitor.png",
                15,
                5,
                20,
                18);
        assertEquals(20, inquisitor.getStatBonus());
    }

    @Test
    void getStatType() {
        Inquisitor inquisitor = new Inquisitor(
                "Test Inquisitor",
                "inquisitor.png",
                15,
                5,
                20,
                18);
        assertEquals("DEX", inquisitor.getStatType());
    }

    @Test
    void calcUpKeep() {
        int result = Oracle.calcUpKeep(
                15,
                5,
                20);
        int expected = 15 + (15 + 5 + 20) / 5;

        assertEquals(expected, result);
    }
}