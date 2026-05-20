package seng201.nunbutblood.models;

/**
 * Oracle — Extend's Apostle's, Primary stat is Strength
 * <p>
 * When Constructor is called :
 *
 * @strength has a bonus of +5 clamped at 20 for balance, Math.min(20, strength + 5)
 * @calcHireCost set's the hire cost, (strength + 5, wisdom, dexterity)
 * @calcUpKeep set's the upkeep cost, (strength + 5, wisdom, dexterity) Constructor to be called in market area for further random stat's
 */
public class Oracle extends Saints {

    /**
     * Instantiates a new Oracle.
     *
     * @param name       the name
     * @param portraitID the portrait id
     * @param strength   the strength
     * @param wisdom     the wisdom
     * @param dexterity  the dexterity
     * @param stamina    the stamina
     */
    public Oracle(String name, String portraitID, int strength, int wisdom, int dexterity, int stamina){
        super(name, portraitID,
                strength,
                wisdom,
                dexterity,
                stamina,
                20,
                Saints.calcHireCost(strength, wisdom, dexterity),
                calcUpKeep(strength, wisdom, dexterity));
    }


    @Override
    public int getStatBonus() {
        return getWisdom();
    }

    @Override
    public String getStatType() {
        return "WIS";
    }

    /**
     * Sum
     *
     * @param strength  the strength
     * @param wisdom    the wisdom
     * @param dexterity the dexterity
     * @return the int
     */
    static int calcUpKeep(int strength, int wisdom, int dexterity) {
        return 15 + (strength + wisdom + dexterity) / 5;
    }
}