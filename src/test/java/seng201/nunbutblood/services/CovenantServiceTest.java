package seng201.nunbutblood.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CovenantServiceTest {

    @Test
    void scoreStartsAtZero() {
        CovenantService service = new CovenantService(5);

        assertEquals(0, service.getScore());
    }

    @Test
    void totalCrusadesMatchesConstructorValue() {
        CovenantService service = new CovenantService(7);

        assertEquals(7, service.getTotalCrusades());
    }

    @Test
    void currentCrusadeStartsAtZero() {
        CovenantService service = new CovenantService(4);

        assertEquals(0, service.getCurrentCrusade());
    }

    @Test
    void changeScoreAddsPointsCorrectly() {
        CovenantService service = new CovenantService(3);

        service.changeScore(25);

        assertEquals(25, service.getScore());
    }

    @Test
    void advanceCrusadeIncreasesCurrentCrusade() {
        CovenantService service = new CovenantService(3);

        service.advanceCrusade();

        assertEquals(1, service.getCurrentCrusade());
    }

    @Test
    void gameIsNotCompleteAtStart() {
        CovenantService service = new CovenantService(2);

        assertFalse(service.isGameComplete());
    }
}