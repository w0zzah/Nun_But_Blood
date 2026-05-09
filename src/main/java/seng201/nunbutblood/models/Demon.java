package seng201.nunbutblood.models;

/**
 * Represents a demon enemy encountered during a crusade.
 * Each demon has a stat weakness that apostles can exploit for a bonus roll,
 * and a curse chance that can negatively affect the party after combat.
 *
 * @author pgi33
 */
public class Demon {

    /** Display name of the demon. */
    private final String name;

    /** Combat difficulty, used to scale encounter frequency and rewards. */
    private final int difficultyRating;

    /** Stat type this demon is weak to — one of "STR", "WIS", or "DEX". */
    private final String weakness;

    /** Faith awarded to the covenant upon defeating this demon. */
    private final int faithReward;

    /** Probability of the curse triggering after combat, between 0.0 and 1.0. */
    private final double curseChance;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Creates a Demon with the given combat attributes.
     *
     * @param name             display name of the demon
     * @param difficultyRating combat difficulty (higher = harder)
     * @param weakness         stat type this demon is weak to ("STR", "WIS", "DEX")
     * @param faithReward      faith awarded on defeat
     * @param curseChance      probability of curse triggering (0.0 to 1.0)
     */
    public Demon(String name, int difficultyRating, String weakness, int faithReward, double curseChance){
        this.name = name;
        this.difficultyRating = difficultyRating;
        this.weakness = weakness;
        this.faithReward = faithReward;
        this.curseChance = curseChance;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /**
     * Returns the demon's display name.
     *
     * @return name of the demon
     */
    public String getName() { return name; }

    /**
     * Returns the difficulty rating of this demon.
     *
     * @return difficulty rating
     */
    public int getDifficultyRating() { return difficultyRating; }

    /**
     * Returns the stat type this demon is weak to.
     *
     * @return weakness stat type ("STR", "WIS", or "DEX")
     */
    public String getWeakness() { return weakness; }

    /**
     * Returns the faith reward for defeating this demon.
     *
     * @return faith reward amount
     */
    public int getFaithReward() { return faithReward; }

    /**
     * Returns the curse chance for this demon.
     *
     * @return curse probability between 0.0 and 1.0
     */
    public double getCurseChance() { return curseChance; }

    /**
     * Returns a formatted summary of this demon's attributes
     * for display in the GUI before a crusade.
     *
     * @return formatted description string
     */
    public String getDescription() {
        return String.format(
                "%s | Difficulty %d | Weak to %s | Reward: %d Faith | Curse chance: %.0f%%",
                name, difficultyRating, weakness, faithReward, curseChance * 100
        );
    }

    /**
     * Rolls against the curse chance to determine if this demon curses the party.
     * Called after combat resolves, regardless of win or loss.
     *
     * @return true if the curse triggers, false otherwise
     */
    public boolean applyCurse() {
        return Math.random() < curseChance;
    }
}