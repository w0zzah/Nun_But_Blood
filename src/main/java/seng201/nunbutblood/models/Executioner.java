package seng201.nunbutblood.models;

/**
 * Executioner — Extend's Apostle's, Primary stat is Strength
 *
 * When Constructor is called :
 * @strength has a bonus of +5 clamped at 20 for balance, Math.min(20, strength + 5)
 * @calcHireCost set's the hire cost, (strength + 5, wisdom, dexterity)
 * @calcUpKeep set's the upkeep cost, (strength + 5, wisdom, dexterity)
 *
 * Constructor to be called in market area for further random stat's
 */
public class Executioner extends Saints {

    public Executioner(String name, String portraitID, int strength, int wisdom, int dexterity, int stamina){
        super(name, portraitID,
                Math.min(20, strength + 5),
                wisdom,
                dexterity,
                stamina,
                20,
                calcHireCost(strength + 5, wisdom, dexterity),
                calcUpKeep(strength + 5, wisdom, dexterity));
    }

    // APOSTLES CLASS


    // PURCHASABLE INTERFACE

    @Override
    public int getStatBonus() {
        return getStrength();
    }

    @Override
    public String getStatType() {
        return "STR";
    }

    /**
     *
     * @param strength
     * @param wisdom
     * @param dexterity
     * @return
     */
    static int calcHireCost(int strength, int wisdom, int dexterity) {
        return 80 + (strength + wisdom + dexterity) * 3;
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


