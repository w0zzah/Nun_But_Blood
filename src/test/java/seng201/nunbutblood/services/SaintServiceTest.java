package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Executioner;
import seng201.nunbutblood.models.Oracle;
import seng201.nunbutblood.models.Inquisitor;
import seng201.nunbutblood.models.Saint;

import static org.junit.jupiter.api.Assertions.*;

class SaintServiceTest {

    private SaintService saintService;

    private Saint saint1;
    private Saint saint2;
    private Saint saint3;
    private Saint saint4;
    private Saint saint5;
    private Saint saint6;

    @BeforeEach
    void setUp() {
        saintService = new SaintService();

        saint1 = new Executioner("Saint 1", "s1.png", 10, 5, 5, 20);
        saint2 = new Oracle("Saint 2", "s2.png", 5, 10, 5, 20);
        saint3 = new Inquisitor("Saint 3", "s3.png", 5, 5, 10, 20);
        saint4 = new Executioner("Saint 4", "s4.png", 10, 5, 5, 20);
        saint5 = new Oracle("Saint 5", "s5.png", 5, 10, 5, 20);
        saint6 = new Inquisitor("Saint 6", "s6.png", 5, 5, 10, 20);
    }

    @Test
    void testMaxSaints() {
        assertTrue(saintService.addSaint(saint1));
        assertTrue(saintService.addSaint(saint2));
        assertTrue(saintService.addSaint(saint3));
        assertTrue(saintService.addSaint(saint4));
        assertTrue(saintService.addSaint(saint5));
        assertTrue(saintService.addSaint(saint6));

        assertEquals(5, saintService.getActiveParty().size());
        assertEquals(1, saintService.getReserves().size());
    }

    @Test
    void getReservesStartsEmpty() {
        assertTrue(saintService.getReserves().isEmpty());
    }

    @Test
    void addSaintAddsToActivePartyFirst() {
        assertTrue(saintService.addSaint(saint1));

        assertTrue(saintService.getActiveParty().contains(saint1));
        assertEquals(1, saintService.getActiveParty().size());
        assertEquals(0, saintService.getReserves().size());
    }

    @Test
    void addSaintGoesToReservesWhenActivePartyFull() {
        saintService.addSaint(saint1);
        saintService.addSaint(saint2);
        saintService.addSaint(saint3);
        saintService.addSaint(saint4);
        saintService.addSaint(saint5);

        assertTrue(saintService.addSaint(saint6));

        assertEquals(5, saintService.getActiveParty().size());
        assertEquals(1, saintService.getReserves().size());
        assertTrue(saintService.getReserves().contains(saint6));
    }

    @Test
    void activateSaintMovesSaintFromReservesToActiveParty() {
        saintService.addSaint(saint1);
        saintService.addSaint(saint2);
        saintService.addSaint(saint3);
        saintService.addSaint(saint4);
        saintService.addSaint(saint5);
        saintService.addSaint(saint6);

        saintService.benchSaint(saint1);

        assertTrue(saintService.activateSaint(saint6));

        assertTrue(saintService.getActiveParty().contains(saint6));
        assertFalse(saintService.getReserves().contains(saint6));
    }

    @Test
    void activateSaintFailsIfSaintNotInReserves() {
        saintService.addSaint(saint1);

        assertFalse(saintService.activateSaint(saint1));
    }

    @Test
    void benchSaintMovesSaintToReserves() {
        saintService.addSaint(saint1);

        assertTrue(saintService.benchSaint(saint1));

        assertFalse(saintService.getActiveParty().contains(saint1));
        assertTrue(saintService.getReserves().contains(saint1));
    }

    @Test
    void benchSaintFailsIfSaintNotActive() {
        assertFalse(saintService.benchSaint(saint1));
    }

    @Test
    void retireSaintRemovesSaint() {
        saintService.addSaint(saint1);
        saintService.addSaint(saint2);

        assertTrue(saintService.retireSaint(saint1));

        assertFalse(saintService.getOwnedSaints().contains(saint1));
    }

    @Test
    void retireSaintFailsIfOnlyOneSaintLeft() {
        saintService.addSaint(saint1);

        assertFalse(saintService.retireSaint(saint1));
        assertTrue(saintService.getOwnedSaints().contains(saint1));
    }

    @Test
    void calculateUpKeep() {
        saintService.addSaint(saint1);
        saintService.addSaint(saint2);

        int expected = saint1.getUpKeep() + saint2.getUpKeep();

        assertEquals(expected, saintService.calculateUpKeep());
    }

    @Test
    void refreshStamina() {
        saintService.addSaint(saint1);

        saint1.consumeStamina(10);
        saintService.refreshStamina();

        assertEquals(saint1.getMaxStamina(), saint1.getCurrentStamina());
    }

    @Test
    void incrementCrusadeCounts() {
        saintService.addSaint(saint1);

        int before = saint1.getCrusadeCount();

        saintService.incrementCrusadeCounts();

        assertEquals(before + 1, saint1.getCrusadeCount());
    }

    @Test
    void isEmpty() {
        assertTrue(saintService.isEmpty());

        saintService.addSaint(saint1);

        assertFalse(saintService.isEmpty());
    }

    @Test
    void getOwnedSaints() {
        saintService.addSaint(saint1);
        saintService.addSaint(saint2);

        assertEquals(2, saintService.getOwnedSaints().size());
        assertTrue(saintService.getOwnedSaints().contains(saint1));
        assertTrue(saintService.getOwnedSaints().contains(saint2));
    }

    @Test
    void allSaintsExhaustedReturnsTrueWhenNoSaints() {
        assertTrue(saintService.allSaintsExhausted());
    }
}