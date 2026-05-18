package seng201.nunbutblood.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Executioner;
import seng201.nunbutblood.models.Saints;

import static org.junit.jupiter.api.Assertions.*;

class SaintsServiceTest {

    private SaintsService saintsService;

    @BeforeEach
    void setUp() {
        this.saintsService = new SaintsService();

    }
    @Test
    void testMaxSaints() {
        int reserves = saintsService.getReserves().size();
        int active = saintsService.getActiveParty().size();

    }

    @Test
    void getReserves() {
    }

    @Test
    void addSaints() {
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