package seng201.nunbutblood.models;

/**
 * Oracle — Extends Saint, specializing in mystical capabilities.
 * Primary scaling attribute is Wisdom (WIS).
 */
public class Oracle extends Saint {

    /**
     * Instantiates a new Oracle.
     *
     * @param name       the unique display name
     * @param portraitID asset reference path for UI layout rendering
     * @param strength   raw physical attribute value
     * @param wisdom     raw spiritual attribute value
     * @param dexterity  raw reflex attribute value
     * @param sanity     maximum reserve pool for mechanical tracking
     */
    public Oracle(String name, String portraitID, int strength, int wisdom, int dexterity, int sanity){
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
     * @return current active wisdom pool modifications
     */
    @Override
    public int getStatBonus() {
        return getWisdom();
    }

    /**
     * Returns system lookup handles referencing localized layout attributes.
     *
     * @return static signature shorthand "WIS"
     */
    @Override
    public String getStatType() {
        return "WIS";
    }

}