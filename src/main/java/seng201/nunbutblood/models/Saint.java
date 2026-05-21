package seng201.nunbutblood.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Base abstract class for all hero units (Apostles/Saints) in the Coven.
 * Handles primary attributes, sanity tracking, financial costs, and temporary power modifications.
 */
public abstract class Saint implements Purchasable {

    private String name;
    private int maxSanity;
    private int currentSanity;
    private int strength;
    private int wisdom;
    private int dexterity;

    /** Chance of acquiring an item after combat. */
    private int perception;

    /** Required faith capital to recruit this unit. */
    private int hireCost;

    /** Faith cost (or dividend, if negative) paid per expedition. */
    private int upKeep;

    private int primaryStat;
    private int crusadeCount;
    private String specialAbility = "";
    private String portraitKey;
    private Rarity rarity;
    private final List<Item> activePotions = new ArrayList<>();

    /**
     * Constructs a new Saint with clamped base attributes and sets initial combat metadata.
     *
     * @param name        Unique display name
     * @param portraitKey Reference key matching assets in resources/images/Saints/
     * @param strength    Physical damage and physical check stat (0-20)
     * @param wisdom      Occult affinity and mystical check stat (0-20)
     * @param dexterity   Finesse and agility check stat (0-20)
     * @param sanity      Maximum and starting sanity pool
     * @param perception  Chance coefficient for post-crusade looting
     * @param hireCost    Faith cost up front to hire from the pool
     * @param upKeep      Faith expense required per active crusade mission
     */
    public Saint(String name, String portraitKey, int strength, int wisdom, int dexterity,
                 int sanity, int perception, int hireCost, int upKeep) {
        this.portraitKey = portraitKey;
        this.name        = name;
        this.strength    = Math.clamp(strength, 0, 20);
        this.wisdom      = Math.clamp(wisdom, 0, 20);
        this.dexterity   = Math.clamp(dexterity, 0, 20);
        this.maxSanity      = sanity;
        this.currentSanity  = sanity;
        this.perception      = perception;
        this.hireCost = hireCost;
        this.upKeep = upKeep;
        this.rarity = Rarity.fromStatTotal(this.strength + this.wisdom + this.dexterity);
        this.crusadeCount = 0;
    }

    /**
     * Determines default upkeep value based on absolute attributes.
     *
     * @param strength  base physical power
     * @param wisdom    base mental focus
     * @param dexterity base motor reflex
     * @return runtime upkeep rate
     */
    protected static int calcUpKeep(int strength, int wisdom, int dexterity) {
        return 15 + (strength + wisdom + dexterity) / 5;
    }

    // ----- GETTERS (PURCHASABLE) ----------------------------------------------------------------

    /** @return Hiring Cost */
    @Override
    public int getCost() { return hireCost;}

    @Override
    public String getDescription() {
        return String.format(
                "[%s] %s | %s | STR %d  WIS %d  DEX %d | Sanity %d/%d | Pay %d/round",
                rarity.getLabel(), name, getStatType(),
                strength, wisdom, dexterity,
                currentSanity, maxSanity,
                upKeep
        );
    }

    // ----- GETTERS (PURCHASABLE) END ----------------------------------------------------------------


    // ----- GETTERS (APOSTLE'S CLASS) ----------------------------------------------------------------

    /**
     * Gets name.
     *
     * @return display name
     */
    public String getName() { return name; }

    /**
     * Gets max sanity.
     *
     * @return maximum sanity
     */
    public int getMaxSanity() { return maxSanity; }

    /**
     * Gets current sanity.
     *
     * @return current sanity
     */
    public int getCurrentSanity() { return currentSanity; }

    /**
     * Gets strength.
     *
     * @return Strength
     */
    public int getStrength() { return strength;}

    /**
     * Gets wisdom.
     *
     * @return Wisdom
     */
    public int getWisdom() { return wisdom;}

    /**
     * Gets dexterity.
     *
     * @return Dexterity
     */
    public int getDexterity() { return dexterity;}

    /**
     * Gets perception.
     *
     * @return perception stat
     */
    public int getPerception() { return perception; }

    /**
     * Gets stat total.
     *
     * @return Returns the combined total of strength, wisdom and dexterity
     */
    public int getStatTotal() {
        return strength + wisdom + dexterity;
    }

    /**
     * Gets up keep.
     *
     * @return daily pay per expedition
     */
    public int getUpKeep() { return upKeep; }

    /**
     * Gets special ability.
     *
     * @return special ability name, or empty string if none
     */
    public String getSpecialAbility() { return specialAbility; }


    /**
     * Gets portrait key.
     *
     * @return the name of the portrait in resources/images/Saints/...
     */
    public String getPortraitKey() { return portraitKey; }

    /**
     * Gets rarity.
     *
     * @return Saint rarity
     */
    public Rarity getRarity() { return rarity; }

    /**
     * Gets crusade count.
     *
     * @return the crusade count
     */
    public int getCrusadeCount() {return  crusadeCount; }

    @Override
    public String toString() {
        return this.getName();
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
     *
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
     * @param amount stat change
     */
    public void changeStrength(int amount) {
        this.strength = Math.clamp(this.strength + amount, 0, 20); // Math.clamp(Value, min, max)
        refreshRarity();
    }

    /**
     * change the wisdom of Apostle (Max 20)
     * Refresh rarity to adjust for stat change
     *
     * @param amount stat change
     */
    public void changeWisdom(int amount) {
        this.wisdom = Math.clamp(this.wisdom + amount, 0, 20);
        refreshRarity();
    }

    /**
     * change the dexterity of Apostle (Max 20)
     * Refresh rarity to adjust for stat change
     *
     * @param amount stat change
     */
    public void changeDexterity(int amount) {
        this.dexterity = Math.clamp(this.dexterity + amount, 0, 20);
        refreshRarity();
    }

    /**
     * Consume's Sanity. If it's below 0, return 0
     *
     * @param amount the amount
     */
    public void consumeSanity(int amount) {
        this.currentSanity = Math.max(0, this.currentSanity - amount); // Math.max return's the higher of two value's, not aloowing current sanity to go below 0
    }

    /**
     * Restores sanity to maximum, called after crusade.
     *
     */
    public void restoreSanity() {
        this.currentSanity = this.maxSanity;
    }

    /**
     * Permanently changes maximum sanity, through Item's
     *
     * @param amount sanity change
     */
    public void changeMaxSanity(int amount) {
        this.maxSanity += amount;
        this.currentSanity = Math.min(this.currentSanity + amount, this.maxSanity);
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

    /**
     * Get attribute value int.
     *
     * @param stat the stat
     * @return primary combat stat value
     */
    public int getAttributeValue(StatType stat){
        if (stat == StatType.STRENGTH) {
            return (this.strength);
        } else if (stat == StatType.WISDOM) {
            return (this.wisdom);
        } else return (this.dexterity);

    }

    /**
     * Gets main stat.
     *
     * @return the main stat
     */
    public String getMainStat() {
        if (strength >= wisdom && strength >= dexterity) {
            return "strength";
        } else if (wisdom >= strength && wisdom >= dexterity) {
            return "wisdom";
        } else {
            return "dexterity";
        }
    }

    /**
     * Calc hire cost int.
     *
     * @param strength  the strength
     * @param wisdom    the wisdom
     * @param dexterity the dexterity
     * @return the int
     */
    static int calcHireCost(int strength, int wisdom, int dexterity) {
        return 500 + ((strength - 10) + (wisdom - 10) + (dexterity - 10)) * 25;
    }

    /**
     * Mutates a specific target attribute on a given unit.
     *
     * @param saint  Saint to change the stat of
     * @param stat   Stat to be changed
     * @param amount The amount the stat will be changed
     */
    public static void applyStat(Saint saint, StatType stat, int amount) {
        switch (stat) {
            case STRENGTH   -> saint.changeStrength(amount);
            case WISDOM     -> saint.changeWisdom(amount);
            case DEXTERITY  -> saint.changeDexterity(amount);
            case PERCEPTION -> saint.changePerception(amount);
            case SANITY    -> saint.changeMaxSanity(amount);
        }
    }

    /**
     * Places a consumable modifier item into local state to track for teardown.
     *
     * @param potion active temporary modifier item
     */
    public void registerConsumable(Item potion) {
        this.activePotions.add(potion);
    }

    /**
     * Iterates through all tracked active potions and subtracts their bonus values before purging cache.
     */
    public void clearConsumable() {
        for (Item potion : activePotions) {
            Saint.applyStat(this, potion.getStatAffected(), -potion.getBonusValue());
        }

        this.activePotions.clear();
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