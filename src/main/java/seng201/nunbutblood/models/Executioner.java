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
public class Executioner extends Apostles {

    public Executioner(String name) {
        this(name, getPortraitID(name), 8, 5, 5);
    }

    public Executioner(String name, String portraitID, int strength, int wisdom, int dexterity){
        super(name, portraitID,
                Math.min(20, strength + 5),
                wisdom,
                dexterity,
                60,
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


    static String getPortraitID(String name) {
        return name.toLowerCase().replace(" ", "-");
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


