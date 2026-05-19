package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SaintsRosterTest {

    @Test
    void pickRandom() {
        Random random = new Random(1);

        List<SaintsRoster.Entry> picked = SaintsRoster.pickRandom(3, random);

        assertEquals(3, picked.size());
        assertTrue(SaintsRoster.getAllSaints().containsAll(picked));
    }

    @Test
    void getAllSaints() {
        List<SaintsRoster.Entry> allSaints = SaintsRoster.getAllSaints();

        assertFalse(allSaints.isEmpty());
        assertEquals(17, allSaints.size());
    }

    @Test
    void create() {
        SaintsRoster.Entry exeEntry =
                new SaintsRoster.Entry("Test Exe", "test.png", "EXE", 10, 5, 3, 20);

        SaintsRoster.Entry oraEntry =
                new SaintsRoster.Entry("Test Ora", "test.png", "ORA", 5, 10, 3, 20);

        SaintsRoster.Entry inqEntry =
                new SaintsRoster.Entry("Test Inq", "test.png", "INQ", 5, 3, 10, 20);

        assertInstanceOf(Executioner.class, SaintsRoster.create(exeEntry));
        assertInstanceOf(Oracle.class, SaintsRoster.create(oraEntry));
        assertInstanceOf(Inquisitor.class, SaintsRoster.create(inqEntry));
    }
}