package seng201.nunbutblood.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemonServiceTest {

    @Test
    void generateNewOptionsCreatesOptions() {
        DemonService demonService = new DemonService();
        demonService.generateNewOptions(3, "Fire");

        assertNotNull(demonService.getCurrentOptions());
        assertFalse(demonService.getCurrentOptions().isEmpty());
    }

    @Test
    void generateNewOptionsCreatesThreeOptions() {
        DemonService demonService = new DemonService();
        demonService.generateNewOptions(3, "Fire");

        assertEquals(3, demonService.getCurrentOptions().size());
    }

    @Test
    void generateNewOptionsCreatesNonNullOptions() {
        DemonService demonService = new DemonService();
        demonService.generateNewOptions(3, "Fire");

        for (var demon : demonService.getCurrentOptions()) {
            assertNotNull(demon);
        }
    }

    @Test
    void getCurrentOptionsReturnsSameGeneratedList() {
        DemonService demonService = new DemonService();
        demonService.generateNewOptions(3, "Fire");
        var options = demonService.getCurrentOptions();

        assertNotNull(options);
        assertEquals(3, options.size());
        assertSame(options, demonService.getCurrentOptions());
    }
}