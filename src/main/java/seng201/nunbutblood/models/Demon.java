package seng201.nunbutblood.models;
/*

Last Edited 0359 02/04/26 @author cwi184 - "Defined Enemy structure"

        Demon Class ---
     //   TODO:
          Attributes:
            - name: String
            - dificultyRating: int
            - attributeWeakness: String
            - faithReward: int
            - curseChance: double
          Methods:
            + getName(): String
            + getDIfficultyRating: int
            + getAttributeWeakness: String
            + getFaithReward: int
            + getCurseChance: double
            + getDescription(): String
            + applyCurse(): boolean
*/
public class Demon {

    private String name;
    private int difficultyRating;
    private String weakness;
    private int faithReward;
    private double curseChance;

    public Demon(String name, int difficultyRating, String weakness, int faithReward, double curseChance){
        this.name = name;
        this.difficultyRating = difficultyRating;
        this.weakness = weakness;
        this.faithReward = faithReward;
        this.curseChance = curseChance;
    }
    public String getName() { return name; }

    public int getDifficultyRating() { return difficultyRating; }

    public String getWeakness() { return weakness; }

    public int getFaithReward() { return faithReward; }

    public double getCurseChance() { return curseChance; }

    public String getDescription() {
        return String.format(
                "%s | Difficulty %d | Weak to %s | Reward: %d Faith | Curse chance: %.0f%%",
                name, difficultyRating, weakness, faithReward, curseChance * 100
        );
    }

    public boolean applyCurse() {
        return Math.random() < curseChance;
    }

}
/**
 * Creates a Demon with the given combat attributes.
 *
 * @param name             display name of the demon
 * @param difficultyRating combat difficulty, scales curse roll
 * @param weakness         stat type this demon is weak to ("STR", "WIS", "DEX")
 * @param faithReward      faith awarded on defeat
 * @param curseChance      probability of curse triggering (0.0 to 1.0)
 */