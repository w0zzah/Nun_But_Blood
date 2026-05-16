package seng201.nunbutblood.models;
/**
 * A central registry of all available items in the game.
 * Use this enum to easily spawn predefined items.
 */
public enum ItemCatalog {

    // --- UPGRADES ---
    HOLY_WATER("Holy Water", Item.ItemType.UPGRADE, "wisdom", 5, 20,
            "Cleanses the mind and grants divine insight.", "circlet.png"),
    MARTYRS_BLOOD("Martyr's Blood", Item.ItemType.UPGRADE, "stamina", 15, 30,
            "Bolsters the spirit for the long crusade.", "bloodvial.png"),

    // --- EQUIPMENT ---
    SILVER_CRUCIFIX("Silver Crucifix", Item.ItemType.EQUIPMENT, "strength", 10, 50,
            "A heavy cross that burns demons on touch.", "chestPlate.png"),

    // --- SKILL TOMES ---
    TOME_OF_SMITING("Tome of Smiting", Item.ItemType.SKILL_TOME, "none", 0, 100,
            "Divine Smite", "tome.png");

    // Enum fields matching the Item constructor
    private final String itemName;
    private final Item.ItemType type;
    private final String statAffected;
    private final int bonusValue;
    private final int cost;
    private final String itemDescription;
    private final String portraitKey;

    ItemCatalog(String itemName, Item.ItemType type, String statAffected, int bonusValue,
                int cost, String itemDescription, String portraitKey) {
        this.itemName = itemName;
        this.type = type;
        this.statAffected = statAffected;
        this.bonusValue = bonusValue;
        this.cost = cost;
        this.itemDescription = itemDescription;
        this.portraitKey = portraitKey;
    }

    /**
     * Spawns a fresh instance of this item.
     * @return A new Item object based on these predefined stats.
     */
    public Item create() {
        return new Item(itemName, type, statAffected, bonusValue, cost, itemDescription, portraitKey);
    }
}