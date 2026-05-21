package seng201.nunbutblood.models;

import java.util.Random;

/**
 * The enum Stat type.
 */
public enum StatType {
    /**
     * Strength stat type.
     */
    STRENGTH("strength", "STR"),
    /**
     * Wisdom stat type.
     */
    WISDOM("wisdom", "WIS"),
    /**
     * Dexterity stat type.
     */
    DEXTERITY("dexterity", "DEX"),
    /**
     * Sanity stat type.
     */
    SANITY("sanity", "STA"),
    /**
     * Perception stat type.
     */
    PERCEPTION("perception", "PER");

    /**
     * The Key.
     */
    public final String key;    // used in applyEffect / getAttributeValue
    /**
     * The Label.
     */
    public final String label;  // used in UI display
    
    StatType(String key, String label) {
        this.key = key;
        this.label = label;
    }

    private static final Random RANDOM = new Random();

    /**
     * Picks a random StatType from the provided choices.
     *
     * @param stats the stats
     * @return the random of
     */
    public static StatType getRandomOf(StatType... stats) {
        if (stats == null || stats.length == 0) {
            throw new IllegalArgumentException("Must provide at least one StatType option.");
        }
        return stats[RANDOM.nextInt(stats.length)];
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }
}
