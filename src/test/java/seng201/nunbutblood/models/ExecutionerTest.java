package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionerTest {

    @Test
    void getStatBonus() {
        Executioner executioner = new Executioner(
                "Test Executioner",
                "executioner.png",
                20,
                10,
                0,
                20);
        assertEquals(20, executioner.getStatBonus());
    }

    @Test
    void getStatType() {
        Executioner executioner = new Executioner(
                "Test Executioner",
                "executioner.png",
                20,
                10,
                0,
                20);
        assertEquals("STR", executioner.getStatType());
    }

    @Test
    void calcUpKeep() {
        int result = Executioner.calcUpKeep(
                20,
                10,
                0);
        int expected = 15 + (20 + 10 + 0) / 5;

        assertEquals(expected, result);
    }
}