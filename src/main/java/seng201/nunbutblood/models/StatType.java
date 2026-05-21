package seng201.nunbutblood.models;

import java.util.Random;

/**
 * Defines core attributes used by characters for crusade event checks and scaling.
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

    /**
     * Gets the system reference key.
     *
     * @return internal reference string
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the abbreviated UI string.
     *
     * @return display label shorthand
     */
    public String getLabel() {
        return label;
    }
}