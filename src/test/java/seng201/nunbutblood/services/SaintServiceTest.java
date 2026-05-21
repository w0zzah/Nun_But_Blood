package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaintServiceTest {

    private SaintService saintService;

    @BeforeEach
    void setUp() {
        this.saintService = new SaintService();

    }
    @Test
    void testMaxSaints() {
        int reserves = saintService.getReserves().size();
        int active = saintService.getActiveParty().size();

    }

    @Test
    void getReserves() {
    }

    @Test
    void addSaint() {
    }

    @Test
    void activateSaint() {
    }

    @Test
    void benchSaint() {
    }

    @Test
    void retireSaint() {
    }

    @Test
    void calculateUpKeep() {
    }

    @Test
    void refreshStamina() {
    }

    @Test
    void incrementCrusadeCounts() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void getOwnedSaints() {
    }
}