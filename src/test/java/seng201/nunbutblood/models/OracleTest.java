package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OracleTest {

    @Test
    void getStatBonus() {
        Oracle oracle = new Oracle(
                "Test Oracle",
                "oracle.png",
                5,
                15,
                3,
                20);
        assertEquals(15, oracle.getStatBonus());
    }

    @Test
    void getStatType() {
        Oracle oracle = new Oracle(
                "Test Oracle",
                "oracle.png",
                5,
                15,
                3,
                20);
        assertEquals("WIS", oracle.getStatType());
    }

    @Test
    void calcUpKeep() {
        int result = Oracle.calcUpKeep(
                10,
                15,
                5);
        int expected = 15 + (10 + 15 + 5) / 5;

        assertEquals(expected, result);
    }
}