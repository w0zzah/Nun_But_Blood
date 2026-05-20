package seng201.nunbutblood.models;

import java.util.Random;

public enum StatType {
    STRENGTH("strength", "STR"),
    WISDOM("wisdom", "WIS"),
    DEXTERITY("dexterity", "DEX"),
    STAMINA("stamina", "STA"),
    PERCEPTION("perception", "PER");

    public final String key;    // used in applyEffect / getAttributeValue
    public final String label;  // used in UI display
    
    StatType(String key, String label) {
        this.key = key;
        this.label = label;
    }

    private static final Random RANDOM = new Random();

    /**
     * Picks a random StatType from the provided choices.
     */
    public static StatType getRandomOf(StatType... stats) {
        if (stats == null || stats.length == 0) {
            throw new IllegalArgumentException("Must provide at least one StatType option.");
        }
        return stats[RANDOM.nextInt(stats.length)];
    }
}
