package seng201.nunbutblood.models;

/*

        Code Summary : "Form's the base which all character's will be built."

          Hold's 13 unique attributes which hold information about

            ~ Profile, (name, portraitKey)
            ~ Stamina, (maxStamina, currentStamina)
            ~ Stat's,  (Strength, Wisdom, Dexterity)
            ~ Unique   (Perception, primaryStat, ability)
            ~ Cost     (hireCost, upKeep)

           Methods:
            ~ getCost() : PURCHASABLE INTERFACE
            ~ getDescription() : PURCHASABLE INTERFACE
            ~ getName() :
            ~ getMaxStamina() :
            ~ getCurrentStamina() :
            ~ getStrength() :
            ~ getWisdom() :
            ~ getDexterity() :
            ~ getPerception() :
            ~ getStatTotal() : (strength + wisdom + dexterity)
            ~ getUpKeep() :
            ~ getSpecialAbility() :
            ~ getPortraitKey() :
            ~ getRarity() :
            ~ getStatBonus() :
            ~ getStatType() :
            ~ refreshRarity() :
            ~ changeStrength() :
            ~ changeWisom() :
            ~ changeDexterity() :
            ~ consumeStamina() : consume after attack 2 for nat 1
            ~ restoreStamina() : upon return, restore all stamina
            ~ changeMaxStamina() :
            ~ changePerception() :
            ~ setSpecialAbility() :
            ~ incrementCrusadeCount() :
            ~ resetCrusadeCount():

*/

import java.util.Objects;

public abstract class Saints implements Purchasable {

    /*
        The Apostle class serve's as a base for our hero's.
        Apostle's have a name, main stat, stamina, perception, hiring cost and up keep cost
        The name will be randomly generated String
        The main stat will be either Strength, Wisdom or Dexterity
        Perception will be a
    */

    // Names character
    private String name;

    //  Stamina decrease's on use of attack
    private int maxStamina;
    private int currentStamina;

    // Base Strength, Wisdom, and Dexterity
    private int strength;
    private int wisdom;
    private int dexterity;

    // Chance of acquiring an Item after combat
    private int perception;

    // The required faith for purchase between 500 and 1000 faith  - >  calculation is ((each stat - 10 / 2) * 25) + 500.
    private int hireCost;

    // The required faith to take Apostle on crusade between -250 and 250 faith -> calculation is (each stat - 10 / 2) * 12
    // A negative upKeep will reward players for using weaker apostle's
    private int upKeep;

    // The type stat to be used for rolling
    private int primaryStat;

    // Keep total of how many crusade's the apostle has been through
    private int crusadeCount;

    // Abilit
    private String specialAbility = "";

    // Portrait of Crusader - May not implement, here as placeholder
    private String portraitKey;

    private Rarity rarity;
    /**
     * Constructor for Apostle's. To be generated through market which pull's on each respective class
     * for base additions. Market -> Executioner -> Apostle's. Will build this way to so itterate's over
     * game state. Longer game goes on, better stat's for Apostle's in market
     *
     * @param name           Name for Apostle
     * @param stamina        maxStamina and currentStamina for the apostle
     * @param strength       Strength for Apostle, int 0-20 which is modified in class get as (strength - 10 / 2)
     * @param wisdom         Wisdom for Apostle, int 0-20 which is modified in class get as (wisdom - 10 / 2)
     * @param dexterity      Dexterity for Apostle, int 0-20 which is modified in class get as (dexterity - 10 / 2)
     * @param perception     Perception for Apostle, serve's as the chance to find Item's
     * @param hireCost       Cost to hire the Apostle, (500 to 1000 faith) - >  calculation is ((each stat - 10 / 2) * 25) + 500.
     * @param upKeep         Cost per expedition for apostle, (-240 to 240) -> calculation is (each stat - 10 / 2) * 12
     * @param portraitKey       Portrait of the Apostle generated in market
     */
    public Saints(String name, String portraitKey, int strength, int wisdom, int dexterity,
                  int stamina, int perception, int hireCost, int upKeep ) {
        this.portraitKey = portraitKey;
        this.name        = name;
        this.strength    = Math.clamp(strength, 0, 20);
        this.wisdom      = Math.clamp(wisdom, 0, 20);
        this.dexterity   = Math.clamp(dexterity, 0, 20);
        this.maxStamina      = stamina;
        this.currentStamina  = stamina;
        this.perception      = perception;
        this.hireCost = hireCost;
        this.upKeep = upKeep;
        this.rarity = Rarity.fromStatTotal(this.strength + this.wisdom + this.dexterity);

    }

    // ----- GETTERS (PURCHASABLE) ----------------------------------------------------------------

    /** @return Hiring Cost */
    @Override
    public int getCost() { return hireCost;}

    @Override
    public String getDescription() {
        return String.format(
                "[%s] %s | %s | STR %d  WIS %d  DEX %d | Stamina %d/%d | Pay %d/round",
                rarity.getLabel(), name, getStatType(),
                strength, wisdom, dexterity,
                currentStamina, maxStamina,
                upKeep
        );
    }

    // ----- GETTERS (PURCHASABLE) END ----------------------------------------------------------------


    // ----- GETTERS (APOSTLE'S CLASS) ----------------------------------------------------------------
    /** @return display name */
    public String getName() { return name; }

    /** @return maximum stamina */
    public int getMaxStamina() { return maxStamina; }

    /** @return current stamina */
    public int getCurrentStamina() { return currentStamina; }

    /** @return Strength */
    public int getStrength() { return strength;}

    /** @return Wisdom */
    public int getWisdom() { return wisdom;}

    /** @return Dexterity */
    public int getDexterity() { return dexterity;}

    /** @return perception stat */
    public int getPerception() { return perception; }

    public int getStatTotal() {
        return strength + wisdom + dexterity;
    }

    /** @return daily pay per expedition */
    public int getUpKeep() { return upKeep; }

    /** @return special ability name, or empty string if none */
    public String getSpecialAbility() { return specialAbility; }


    /** @return number of consecutive crusades without rest */
    public String getPortraitKey() { return portraitKey; }

    /** @return Saints rarity*/
    public Rarity getRarity() { return rarity; }

    public int getCrusadeCount() {return  crusadeCount; }

    @Override
    public String toString() {
        return this.getName(); // Assuming you have a getName() method
    }



    // ----- GETTERS (ABSTRACT)----------------------------------------------------------------


    /**
     * Returns the subclass's primary stat value to add to a dice roll.
     * This value already includes the +5 class bonus applied at construction.
     *
     * @return the primary stat bonus for crusade dice checks
     */
    public abstract int getStatBonus();

    /**
     * Returns the short stat-type identifier, ('STR', 'WIS', 'DEX')
     * @return stat type string
     */
    public abstract String getStatType();


    // ----- GETTERS (ABSTRACT) END -----------------------------------------------------------



    // ----- METHODS (STAT CHANGES) --------------------------------------------------------------------------

    private void refreshRarity() {
        this.rarity = Rarity.fromStatTotal(strength + wisdom + dexterity);
    }

    /**
     * change the strength of Apostle (Max 20)
     * Refresh rarity to adjust for stat change
     *
     *  @param amount stat change
     */
    public void changeStrength(int amount) {
        this.strength = Math.clamp(this.strength + amount, 0, 20); // Math.clamp(Value, min, max)
        refreshRarity();
    }

    /**
     * change the wisdom of Apostle (Max 20)
     * Refresh rarity to adjust for stat change
     *
     *  @param amount stat change
     */
    public void changeWisdom(int amount) {
        this.wisdom = Math.clamp(this.wisdom + amount, 0, 20);
        refreshRarity();
    }

    /**
     * change the dexterity of Apostle (Max 20)
     * Refresh rarity to adjust for stat change
     *
     *  @param amount stat change
     */
    public void changeDexterity(int amount) {
        this.dexterity = Math.clamp(this.dexterity + amount, 0, 20);
        refreshRarity();
    }

    /**
     * Consume's Stamina. If it's below 0, return 0
     *
     * @param amount
     */
    public void consumeStamina(int amount) {
        this.currentStamina = Math.max(0, this.currentStamina - amount); // Math.max return's the higher of two value's, not aloowing current stamina to go below 0
    }

    /**
     * Restores stamina to maximum, called after crusade.
     *
     * */
    public void restoreStamina() {
        this.currentStamina = this.maxStamina;
    }

    /**
     * Permanently changes maximum stamina, through Item's
     *
     * @param amount stamina change
     */
    public void changeMaxStamina(int amount) {
        this.maxStamina += amount;
        this.currentStamina = Math.min(this.currentStamina + amount, this.maxStamina);
    }

    /**
     * Permanently changes perception, through Item's or after crusade's
     *
     * @param amount amount to add
     */
    public void changePerception(int amount) {
        this.perception += amount;
    }

    /**
     * Give's A unique ability to Apostle.
     *
     * @param abilityName name of the ability
     */
    public void setSpecialAbility(String abilityName) {
        this.specialAbility = abilityName;
    }

    /** @return primary combat stat value */
    public int getAttributeValue(String attributeName){
        if (Objects.equals(attributeName, "strength")) {
            return (this.strength / 2);

        } else if (Objects.equals(attributeName, "wisdom")) {
            return (this.wisdom / 2);
        }
        else return (this.dexterity / 2);
    }

    public String getMainStat() {
        if (strength >= wisdom && strength >= dexterity) {
            return "strength";
        } else if (wisdom >= strength && wisdom >= dexterity) {
            return "wisdom";
        } else {
            return "dexterity";
        }
    }


    // ----- METHODS (STAT CHANGES) END --------------------------------------------------------------------------

    // ----- CRUSADE COUNTER -----------------------------------------------------------------------------------

    /**
     * Add's 1 to crusade counter after every crusade.
     */
    public void incrementCrusadeCount() { this.crusadeCount++; }

    /**
     * Reset crusade's called on hire
     */
    public void resetCrusadeCount() { this.crusadeCount = 0; }

    // ----- METHODS END ---------------------------------------------------------------------------------------

}

