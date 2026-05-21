package seng201.nunbutblood.models;

import java.util.Random;

/**
 * Represents the rarity tier of an Apostle based on sum of stat's
 * Strength, Wisdom, and Dexterity stats. <br><br>
 * <p>
 * Rarity thresholds (statTotal = STR + WIS + DEX):<br><br>
 * <p>
 * COMMON     — statTotal between 0 and 30<br>
 * RARE       — statTotal between 31 and 40<br>
 * EPIC       — statTotal between 41 and 50<br>
 * LEGENDARY  — statTotal between 51 and 60
 */
public enum Rarity {

    /**
     * stat total 1–30 (Green)
     */
    COMMON("Common", "#4CBB17"),

    /**
     * stat total 31–40 (Blue)
     */
    RARE("Rare", "#079bdb"),

    /**
     * stat total 41–50 (Purple)
     */
    EPIC("Epic", "#9A5BD4"),

    /**
     * stat total 51–60 (Pink)
     */
    LEGENDARY("Legendary", "#ff00a2");

    /** Rarity String */
    private final String label;
    /** Border and text Colour */
    private final String colour;
    /** Background fill hex for the rarity badge*/

    /**
     * Constructor for Rarity.
     * Label serve's as name
     * colour change's font and outline
     * badgeBackground change's the background
     *
     * @param label  Text tag for the tier
     * @param colour Target UI hex color code
     */
    Rarity(String label, String colour) {
        this.label           = label;
        this.colour          = colour;
    }

    /**
     * Check's the total is between 0-60 and clamps
     * Calculate's Rarity tier from statTotal
     *
     * @param statTotal sum of strength + wisdom + dexterity
     * @return the corresponding Rarity tier
     */
    public static Rarity fromStatTotal(int statTotal) {
        int clamped = Math.clamp(statTotal, 0, 60); // Math.clamp(value, min, max)
        if (clamped > 50) return LEGENDARY;
        if (clamped >= 40) return EPIC;
        if (clamped >= 30) return RARE;
        return COMMON;
    }

    /**
     * Calculates a drop tier outcome rolling against dynamically adjusted weight matrices
     * based on campaign progression milestones.
     *
     * @param random        Shared processing weight seed
     * @param crusadeNumber Current sequential campaign counter
     * @return Evaluated rarity tier assignment
     */
    public static Rarity rollFromCrusade(Random random, int crusadeNumber) {

        int roll = random.nextInt(100);

        int commonChance;
        int rareChance;
        int epicChance;
        // Increase chance
        switch (crusadeNumber) {
            case 0 -> {
                commonChance = 100; // Common
                rareChance = 0;     // Rare
                epicChance = 0;     // Epic
                // LegendaryChance is whatever 100 - (common + rare + epic)
            }
            // Legendary Chance = 0
            case 1, 2 -> {
                commonChance = 60;
                rareChance = 35;
                epicChance = 5;
            }
            // Legendary Chance = 5
            case 3, 4 -> {
                commonChance = 50;
                rareChance = 30;
                epicChance = 15;
            }

            // Legendary Chance = 10
            case 5, 6 -> {
                commonChance = 40;
                rareChance = 30;
                epicChance = 20;
            }

            // Legendary Chance = 20
            case 7, 8, 9, 10 -> {
                commonChance = 30;
                rareChance = 25;
                epicChance = 25;
            }

            // Legendary Chance = 30
            default -> {
                commonChance = 0;
                rareChance = 40;
                epicChance = 30;
            }
        }

        if (roll < commonChance) return COMMON;
        roll -= commonChance;

        if (roll < rareChance) return RARE;
        roll -= rareChance;

        if (roll < epicChance) return EPIC;

        return LEGENDARY;
    }

    /**
     * Gets label.
     *
     * @return display rarity label, (Common, Rare, Epic, Legendary)
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets colour.
     *
     * @return primary hex colour string, (Green, Blue, Purple, Pink)
     */
    public String getColour() {
        return colour;
    }
}