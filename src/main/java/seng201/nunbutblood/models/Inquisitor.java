package seng201.nunbutblood.models;

/**
 * Inquisitor — Extend's Apostle's, Primary stat is Strength
 *
 * When Constructor is called :
 * @strength has a bonus of +5 clamped at 20 for balance, Math.min(20, strength + 5)
 * @calcHireCost set's the hire cost, (strength + 5, wisdom, dexterity)
 * @calcUpKeep set's the upkeep cost, (strength + 5, wisdom, dexterity)
 *
 * Constructor to be called in market area for further random stat's
 */
public class Inquisitor extends Saints {

    public Inquisitor(String name, String portraitID, int strength, int wisdom, int dexterity, int stamina){
        super(name, portraitID,
                strength,
                wisdom,
                dexterity,
                stamina,
                20,
                Saints.calcHireCost(strength, wisdom, dexterity),
                calcUpKeep(strength, wisdom, dexterity));
    }

    // APOSTLES CLASS


    // PURCHASABLE INTERFACE

    @Override
    public int getStatBonus() {
        return getDexterity();
    }

    @Override
    public String getStatType() {
        return "DEX";
    }


    /**
     * Sum
     * @param strength
     * @param wisdom
     * @param dexterity
     * @return
     */
    static int calcUpKeep(int strength, int wisdom, int dexterity) {
        return 15 + (strength + wisdom + dexterity) / 5;
    }
}
