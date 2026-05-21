package seng201.nunbutblood.models;

/**
 * Inquisitor — Extends Saint, specializing in high-dexterity maneuvers.
 * Primary scaling attribute is Dexterity (DEX).
 */
public class Inquisitor extends Saint {

    /**
     * Instantiates a new Inquisitor with balanced physical and mechanical tracking properties.
     *
     * @param name       the unique display name
     * @param portraitID asset reference path for UI layout rendering
     * @param strength   raw physical attribute value
     * @param wisdom     raw spiritual attribute value
     * @param dexterity  raw reflex attribute value
     * @param sanity     maximum reserve pool for mechanical tracking
     */
    public Inquisitor(String name, String portraitID, int strength, int wisdom, int dexterity, int sanity){
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
     * @return current active dexterity pool modifications
     */
    @Override
    public int getStatBonus() {
        return getDexterity();
    }

    /**
     * Returns system lookup handles referencing localized layout attributes.
     *
     * @return static signature shorthand "DEX"
     */
    @Override
    public String getStatType() {
        return "DEX";
    }

}