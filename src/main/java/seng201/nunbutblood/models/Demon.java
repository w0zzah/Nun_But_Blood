package seng201.nunbutblood.models;
/*

Last Edited 0359 02/04/26 @author cwi184 - "Defined Enemy structure"

        Demon Class ---
        TODO:
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
        return "This Demon is " + name + ". It has a difficulty rating of " + difficultyRating +
                " and a weakness to " + weakness;}

    public boolean applyCurse() {
        if (Math.random() * difficultyRating < curseChance){
            return true;
        }
        else{
            return false;
        }
    }

}
