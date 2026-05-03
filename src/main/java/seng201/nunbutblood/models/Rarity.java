package seng201.nunbutblood.models;

/**
 * Represents the rarity tier of an Apostle based on sum of stat's
 * Strength, Wisdom, and Dexterity stats. <br><br>
 *
 * Rarity thresholds (statTotal = STR + WIS + DEX):<br><br>
 *
 *   COMMON     — statTotal between 0 and 30<br>
 *   RARE       — statTotal between 31 and 40<br>
 *   EPIC       — statTotal between 41 and 50<br>
 *   LEGENDARY  — statTotal between 51 and 60
 */
public enum Rarity {

    /** stat total 1–30 (Green) */
    COMMON("Common", "#4CBB17", "#317a0f"),

    /** stat total 31–40 (Blue) */
    RARE("Rare", "#079bdb", "#0047AB"),

    /** stat total 41–50 (Purple) */
    EPIC("Epic", "#9A5BD4", "#5305a6"),

    /** stat total 51–60 (Pink) */
    LEGENDARY("Legendary", "#ff00a2", "#e85ff5");

    /** Rarity String */
    private final String label;
    /** Border and text Colour */
    private final String colour;
    /** Background fill hex for the rarity badge TODO: Set as picture with enough time  */
    private final String badgeBackground;

    /**
     * Constructor for Rarity.
     * Label serve's as name
     * colour change's font and outline
     * badgeBackground change's the background
     *
     * @param label
     * @param colour
     * @param badgeBackground
     */
    Rarity(String label, String colour, String badgeBackground) {
        this.label           = label;
        this.colour          = colour;
        this.badgeBackground = badgeBackground;
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

    // GETTERS (Label, Colour, Background)

    /** @return display rarity label, (Common, Rare, Epic, Legendary) */
    public String getLabel() {
        return label;
    }

    /** @return primary hex colour string, (Green, Blue, Purple, Pink) */
    public String getColour() {
        return colour;
    }

    /** @return badge background hex string, (Green, Blue, Purple, Pink) */
    public String getBadgeBackground() {
        return badgeBackground;
    }

}
