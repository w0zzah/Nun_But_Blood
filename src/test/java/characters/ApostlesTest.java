package characters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.nunbutblood.models.Executioner;
import seng201.nunbutblood.models.Oracle;
import seng201.nunbutblood.models.Inquisitor;
import seng201.nunbutblood.models.Rarity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Apostles abstract class, subclasses, and the Rarity system.
 */
class ApostlesTest {
    private Executioner executioner;
    private Oracle oracle;
    private Inquisitor inquisitor;

    @BeforeEach
    void setUp() {
        executioner = new Executioner("Test Warrior");
    }

    // ── Class bonus (+5 applied at construction) ──────────────────────────────

    @Test
    void executioner_strength_includesClassBonus() {
        // Default constructor: baseStr=8, class bonus +5 → expected 13
        assertEquals(13, executioner.getStrength());
    }


    @Test
    void classBonus_cappedAt20() {
        // Base 16 + 5 = 21, must be clamped to 20
        Executioner strong = new Executioner("Capped", "capped", 16, 5, 5);
        assertEquals(20, strong.getStrength());
    }

    // ── Stat type routing ─────────────────────────────────────────────────────

    @Test
    void executioner_statType_isSTR() {
        assertEquals("STR", executioner.getStatType());
    }

    @Test
    void executioner_statBonus_returnsStrength() {
        assertEquals(executioner.getStrength(), executioner.getStatBonus());
    }


    // ── Rarity computation ────────────────────────────────────────────────────

    @Test
    void rarity_fromStatTotal_common() {
        assertEquals(Rarity.COMMON, Rarity.fromStatTotal(15));
        assertEquals(Rarity.COMMON, Rarity.fromStatTotal(1));
        assertEquals(Rarity.COMMON, Rarity.fromStatTotal(29));
    }

    @Test
    void rarity_fromStatTotal_rare() {
        assertEquals(Rarity.RARE, Rarity.fromStatTotal(30));
        assertEquals(Rarity.RARE, Rarity.fromStatTotal(39));
    }

    @Test
    void rarity_fromStatTotal_epic() {
        assertEquals(Rarity.EPIC, Rarity.fromStatTotal(40));
        assertEquals(Rarity.EPIC, Rarity.fromStatTotal(49));
    }

    @Test
    void rarity_fromStatTotal_legendary() {
        assertEquals(Rarity.LEGENDARY, Rarity.fromStatTotal(51));
        assertEquals(Rarity.LEGENDARY, Rarity.fromStatTotal(60));
    }

    @Test
    void rarity_clampedAt60() {
        assertEquals(Rarity.LEGENDARY, Rarity.fromStatTotal(999));
    }

    @Test
    void rarity_clampedAtZero() {
        assertEquals(Rarity.COMMON, Rarity.fromStatTotal(-5));
    }

    @Test
    void executioner_defaultConstructor_isCommon() {
        // STR 13 + WIS 5 + DEX 5 = 23 → COMMON
        assertEquals(Rarity.COMMON, executioner.getRarity());
    }

    @Test
    void fullConstructor_epicStats_isEpic() {
        // STR 18+5=20, WIS 10, DEX 9 → total 39 → RARE (18+5=20... 20+10+9=39)
        // Use values that give total 42 for EPIC: base STR=15→20, WIS=12, DEX=10
        Executioner epic = new Executioner("Epic One", "epic-one", 15, 12, 10);
        assertEquals(Rarity.EPIC, epic.getRarity());
    }

    @Test
    void fullConstructor_legendaryStats_isLegendary() {
        // STR 15+5=20, WIS 16, DEX 16 → total 52 → LEGENDARY
        Executioner legend = new Executioner("Legend", "legend", 15, 16, 16);
        assertEquals(Rarity.LEGENDARY, legend.getRarity());
    }

    // ── Rarity refreshes when stats change ────────────────────────────────────

    @Test
    void rarity_updatesAfterStatIncrease() {
        // Start COMMON (total 23), increase STR by 10 → total 33 → RARE
        assertEquals(Rarity.COMMON, executioner.getRarity());
        executioner.increaseStrength(10);
        assertEquals(Rarity.RARE, executioner.getRarity());
    }


    @Test
    void statTotal_sumOfAllThree() {
        int expected = executioner.getStrength() + executioner.getWisdom() + executioner.getDexterity();
        assertEquals(expected, executioner.getStatTotal());
    }

    // ── Stat cap enforcement ──────────────────────────────────────────────────

    @Test
    void increaseStrength_cappedAt20() {
        executioner.increaseStrength(999);
        assertEquals(20, executioner.getStrength());
    }


    // ── Purchasable ───────────────────────────────────────────────────────────

    @Test
    void getDescription_containsRarityLabel() {
        assertTrue(executioner.getDescription().contains("Common"));
    }

    @Test
    void getDescription_containsAllThreeStatValues() {
        String desc = executioner.getDescription();
        assertTrue(desc.contains("STR") && desc.contains("WIS") && desc.contains("DEX"));
    }
}
