package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SaintRosterTest {

    @Test
    void pickRandom() {
        Random random = new Random(1);

        List<SaintRoster.Entry> picked = SaintRoster.pickRandom(3, random);

        assertEquals(3, picked.size());
        assertTrue(SaintRoster.getAllSaints().containsAll(picked));
    }

    @Test
    void getAllSaints() {
        List<SaintRoster.Entry> allSaints = SaintRoster.getAllSaints();

        assertFalse(allSaints.isEmpty());
        assertEquals(18, allSaints.size());
    }

    @Test
    void create() {
        SaintRoster.Entry exeEntry =
                new SaintRoster.Entry("Test Exe", "test.png", "EXE", 10, 5, 3, 20);

        SaintRoster.Entry oraEntry =
                new SaintRoster.Entry("Test Ora", "test.png", "ORA", 5, 10, 3, 20);

        SaintRoster.Entry inqEntry =
                new SaintRoster.Entry("Test Inq", "test.png", "INQ", 5, 3, 10, 20);

        assertInstanceOf(Executioner.class, SaintRoster.create(exeEntry));
        assertInstanceOf(Oracle.class, SaintRoster.create(oraEntry));
        assertInstanceOf(Inquisitor.class, SaintRoster.create(inqEntry));
    }
}