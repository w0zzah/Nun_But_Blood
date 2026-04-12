package seng201.nunbutblood.models;

import seng201.nunbutblood.characters.Apostles;

/*

Last Edited 0405 02/04/26 @author cwi184 - "Item Class implementation"

        Item Class --- Implements Purchasable
  //      TODO:
          Attributes:
            + ItemType: enum
            - itemName: string
            - type: ItemType
            - statAffected: String
            - bonusValue: int
            - cost: int
            - itemDescription: String
          Methods:
            + applyEffect(Apostle apostle)
            + getItemName(): ItemType
            + getStatAffected(): String
            + getBonusValue(): int
            + getCost(): int <<purchasble>>
            + getDecription: String <<purchasable>>
*/
public class Item implements Purchasable {

    public enum ItemType {
        UPGRADE,    // one-time stat boost, consumed on use
        EQUIPMENT,  // equippable, provides stat bonus while held
        SKILL_TOME  // grants a special ability
    }
    /** Display name of item. */
    private String itemName;

    /** Functional category that determines how the item behaves. */
    private ItemType type;

    /** The stat this item affects — one of "STR", "WIS", "DEX", "STA", "PER". */
    private String statAffected;

    /** The amount added to the affected stat. */
    private int bonusValue;

    /** Faith cost to purchase from the market. */
    private int cost;

    /** Description shown in the market and inventory screens. */
    private String itemDescription;

    /**
     * Creates an Item with the given attributes.
     *
     * @param itemName        display name of the item
     * @param type            functional category (UPGRADE, EQUIPMENT, SKILL_TOME)
     * @param statAffected    stat this item affects ("STR", "WIS", "DEX", "STA", "PER")
     * @param bonusValue      amount added to the affected stat
     * @param cost            faith cost to purchase
     * @param itemDescription description shown in market and inventory
     */
    public Item(String itemName, ItemType type, String statAffected, int bonusValue, int cost, String itemDescription){
        this.itemName = itemName;
        this.type = type;
        this.statAffected = statAffected;
        this.bonusValue = bonusValue;
        this.cost = cost;
        this.itemDescription = itemDescription;
    }

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Applies this item's effect to the given apostle.
     * UPGRADE and SKILL_TOME are consumed on use.
     * EQUIPMENT persists and should be tracked on the apostle separately.
     *
     * @param apostle the apostle to apply the effect to
     */
    public void applyEffect(Apostles apostle) {
        switch (type) {
            case UPGRADE, EQUIPMENT -> {
                switch (statAffected) {
                    case "STR" -> apostle.increaseStrength(bonusValue);
                    case "WIS" -> apostle.increaseWisdom(bonusValue);
                    case "DEX" -> apostle.increaseDexterity(bonusValue);
                    case "STA" -> apostle.increaseMaxStamina(bonusValue);
                    case "PER" -> apostle.increasePerception(bonusValue);
                }
            }
            case SKILL_TOME -> apostle.setSpecialAbility(itemDescription);
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Returns the display name of this item.
     * @return item name
     */
    public String getItemName(){ return itemName; }

    /**
     * Returns the functional type of this item.
     * @return item type
     */
    public ItemType getType() { return type; }

    /**
     * Returns the stat this item affects.
     * @return stat identifier string
     */
    public String getStatAffected(){ return statAffected; }

    /**
     * Returns the stat bonus amount this item provides.
     * @return bonus value
     */
    public int getBonusValue(){ return bonusValue; }

    /**
     * Returns the faith cost to purchase this item.
     *
     * @return cost in faith
     */
    @Override
    public int getCost() { return cost; }

    /**
     * Returns a formatted description for market and inventory display.
     *
     * @return description string
     */
    @Override
    public String getDescription() {
        return String.format("[%s] %s | +%d %s | Cost: %d Faith",
                type, itemName, bonusValue, statAffected, cost);
    }
}
