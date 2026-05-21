package seng201.nunbutblood.models;

/**
 * Represents a purchasable item that can be applied to an saint.
 * Items are either consumed on use (CONSUMABLE, SKILL_TOME) or
 * persist while equipped (EQUIPMENT).
 *
 * @author pgi33
 */
public class Item implements Purchasable {

    /**
     * Defines the functional category of an item.
     * Determines how applyEffect() behaves.
     */
    public enum ItemType {
        /**
         * One-time permanent stat boost, consumed on use.
         */
        CONSUMABLE,
        /**
         * Equippable item that boosts stats while held.
         */
        EQUIPMENT
    }
    /** Display name of item. */
    private final String itemName;

    /** Functional category that determines how the item behaves. */
    private final ItemType type;

    /** The stat this item affects — one of "STR", "WIS", "DEX", "STA", "PER". */
    private final StatType statAffected;

    /** The amount added to the affected stat. */
    private final int bonusValue;

    /** Faith cost to purchase from the market. */
    private final int cost;

    /** Description shown in the market and inventory screens. */
    private final String itemDescription;

    private final String portraitKey;


    /**
     * Creates an Item with the given attributes.
     *
     * @param itemName        display name of the item
     * @param type            functional category (CONSUMABLE, EQUIPMENT)
     * @param statAffected    stat this item affects ("STR", "WIS", "DEX", "STA", "PER")
     * @param bonusValue      amount added to the affected stat
     * @param cost            faith cost to purchase
     * @param itemDescription description shown in market and inventory
     * @param portraitKey     Item Portrait
     */
    public Item(String itemName, ItemType type, StatType statAffected, int bonusValue, int cost, String itemDescription, String portraitKey){
        this.itemName = itemName;
        this.type = type;
        this.statAffected = statAffected;
        this.bonusValue = bonusValue;
        this.cost = cost;
        this.itemDescription = itemDescription;
        this.portraitKey = portraitKey;
    }

    /**
     * Applies this item's effect to the given saint.
     * CONSUMABLE and SKILL_TOME are consumed on use.
     * EQUIPMENT persists and should be tracked on the saint separately.
     *
     * @param saint the saint to apply the effect to
     */
    public void applyEffect(Saint saint) {
        Saint.applyStat(saint, this.statAffected, this.bonusValue);

        // If it's a potion, sign it up to be automatically reversed post-crusade
        if (this.type == ItemType.CONSUMABLE) {
            saint.registerConsumable(this);
        }
    }


    /**
     * Returns the display name of this item.
     *
     * @return item name
     */
    public String getPortraitKey() {return portraitKey; }

    /**
     * Get item name string.
     *
     * @return the string
     */
    public String getItemName(){return String.format("[%s] %s",
            type, itemName);
    }

    /**
     * Returns the functional type of this item.
     *
     * @return item type
     */
    public ItemType getType() { return type; }

    /**
     * Returns the stat this item affects.
     *
     * @return stat identifier string
     */
    public StatType getStatAffected(){ return statAffected; }

    /**
     * Returns the stat bonus amount this item provides.
     *
     * @return bonus value
     */
    public int getBonusValue(){ return bonusValue; }

    /**
     * Returns the faith cost to purchase this item.
     * @return cost in faith
     */
    @Override
    public int getCost() { return cost; }

    /**
     * Returns a formatted description for market and inventory display.
     * @return description string
     */
    @Override
    public String getDescription() {
        return String.format("+%d %s | Cost: %d Faith | %s",
                bonusValue, statAffected, cost, itemDescription);
    }
}