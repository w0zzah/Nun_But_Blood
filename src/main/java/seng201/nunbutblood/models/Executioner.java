package seng201.nunbutblood.models;

/**
 * Executioner — Extend's Apostle's, Primary stat is Strength
 * <p>
 * When Constructor is called :
 *
 * @strength has a bonus of +5 clamped at 20 for balance, Math.min(20, strength + 5)
 * @calcHireCost set's the hire cost, (strength + 5, wisdom, dexterity)
 * @calcUpKeep set's the upkeep cost, (strength + 5, wisdom, dexterity) Constructor to be called in market area for further random stat's
 */
public class Executioner extends Saint {

    /**
     * Instantiates a new Executioner.
     *
     * @param name       the name
     * @param portraitID the portrait id
     * @param strength   the strength
     * @param wisdom     the wisdom
     * @param dexterity  the dexterity
     * @param sanity    the sanity
     */
    public Executioner(String name, String portraitID, int strength, int wisdom, int dexterity, int sanity){
        super(name, portraitID,
                strength,
                wisdom,
                dexterity,
                sanity,
                20,
                Saint.calcHireCost(strength, wisdom, dexterity),
                calcUpKeep(strength, wisdom, dexterity));
    }

    @Override
    public int getStatBonus() {
        return getStrength();
    }

    @Override
    public String getStatType() {
        return "STR";
    }

}


