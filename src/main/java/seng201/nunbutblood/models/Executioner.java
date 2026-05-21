package seng201.nunbutblood.models;

/**
 * Executioner — Extends Saint, specializing in heavy offensive capabilities.
 * Primary scaling attribute is Strength (STR).
 */
public class Executioner extends Saint {

    /**
     * Instantiates a new Executioner with offensive scaling parameters.
     *
     * @param name       the unique display name
     * @param portraitID asset reference path for UI layout rendering
     * @param strength   raw physical attribute value
     * @param wisdom     raw spiritual attribute value
     * @param dexterity  raw reflex attribute value
     * @param sanity     maximum reserve pool for mechanical tracking
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

    /**
     * Extracts operational combat values matching internal class archetype specifications.
     *
     * @return current active strength pool modifications
     */
    @Override
    public int getStatBonus() {
        return getStrength();
    }

    /**
     * Returns system lookup handles referencing localized layout attributes.
     *
     * @return static signature shorthand "STR"
     */
    @Override
    public String getStatType() {
        return "STR";
    }

}