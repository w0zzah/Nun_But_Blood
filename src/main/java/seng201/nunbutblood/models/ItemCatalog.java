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

    CROWN_OF_ASHES("Crown of Ashes", Item.ItemType.UPGRADE,
            "wisdom", 10, 75,
            "The burden of sacrifice sharpens holy judgment.", "crown.png"),

    // --- EQUIPMENT ---

    SILVER_CRUCIFIX("Silver Crucifix", Item.ItemType.EQUIPMENT, "strength", 10, 50,
            "A heavy cross that burns demons on touch.", "chestPlate.png"),

    RELIQUARY_BLADE("Reliquary Blade", Item.ItemType.EQUIPMENT,
            "strength", 12, 80,
            "A consecrated sword sealed with saintly relics.", "reliquaryblade.png"),

    PENITENTS_CHAIN("Penitent's Chain", Item.ItemType.EQUIPMENT,
            "stamina", 20, 90,
            "Heavy chains carried in devotion strengthen resolve.", "chain.png"),

    // --- SKILL TOMES ---

    SCRIPTURE_OF_FLAMES("Scripture of Flames", Item.ItemType.SKILL_TOME,
            "none", 0, 120,
            "Holy Inferno", "flametome.png"),

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