package seng201.nunbutblood.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CovenantTest {

    @Test
    void getName() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertEquals("Blood Covenant", covenant.getName());
    }

    @Test
    void getTotalCrusades() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertEquals(5, covenant.getTotalCrusades());
    }

    @Test
    void getCurrentCrusade() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertEquals(0, covenant.getCurrentCrusade());
    }

    @Test
    void getDifficulty() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertEquals("Normal", covenant.getDifficulty());
    }

    @Test
    void addFaith() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        int before = covenant.getFaith();
        covenant.addFaith(50);

        assertEquals(before + 50, covenant.getFaith());
    }

    @Test
    void spendFaith() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        covenant.addFaith(100);
        int before = covenant.getFaith();

        boolean result = covenant.spendFaith(50);

        assertTrue(result);
        assertEquals(before - 50, covenant.getFaith());
    }

    @Test
    void spendFaithFailsWhenTooExpensive() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        int before = covenant.getFaith();

        boolean result = covenant.spendFaith(before + 9999);

        assertFalse(result);
        assertEquals(before, covenant.getFaith());
    }

    @Test
    void addItem() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");
        Item item = ItemCatalog.HOLY_WATER.create();

        covenant.addItem(item);

        assertTrue(covenant.getInventory().contains(item));
    }

    @Test
    void removeItem() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");
        Item item = ItemCatalog.HOLY_WATER.create();

        covenant.addItem(item);
        boolean removed = covenant.removeItem(item);

        assertTrue(removed);
        assertFalse(covenant.getInventory().contains(item));
    }

    @Test
    void getInventoryIsUnmodifiable() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertThrows(UnsupportedOperationException.class, () -> {
            covenant.getInventory().add(ItemCatalog.HOLY_WATER.create());
        });
    }

    @Test
    void postCrusadeVictoryAdvancesCrusade() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        int beforeCrusade = covenant.getCurrentCrusade();

        covenant.postCrusade(100, 50, true);

        assertEquals(beforeCrusade + 1, covenant.getCurrentCrusade());
        assertTrue(covenant.getScore() >= 50);
    }

    @Test
    void postCrusadeDefeatDoesNotAdvanceCrusade() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        int beforeCrusade = covenant.getCurrentCrusade();

        covenant.postCrusade(0, 0, false);

        assertEquals(beforeCrusade, covenant.getCurrentCrusade());
    }

    @Test
    void getInventoryStartsEmpty() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertTrue(covenant.getInventory().isEmpty());
    }

    @Test
    void getDemonServiceIsNotNull() {
        Covenant covenant = new Covenant("Blood Covenant", 5, "Normal");

        assertNotNull(covenant.getDemonService());
    }
}