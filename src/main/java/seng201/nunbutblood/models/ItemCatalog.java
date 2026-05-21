package seng201.nunbutblood.models;

/**
 * A central registry of all available items in the game.
 * Use this enum to easily spawn predefined items.
 */
public enum ItemCatalog {

    // --- UPGRADES ---

    /**
     * The Holy water.
     */
    HOLY_WATER("Holy Water", Item.ItemType.UPGRADE, StatType.WISDOM, 5, 200,
            "Cleanses the mind and grants divine insight.", "circlet.png"),

    /**
     * The Martyrs blood.
     */
    MARTYRS_BLOOD("Martyr's Blood", Item.ItemType.UPGRADE, StatType.STAMINA, 10, 400,
            "Bolsters the spirit for the long crusade.", "bloodvial.png"),

    /**
     * The Crown of ashes.
     */
    CROWN_OF_ASHES("Crown of Ashes", Item.ItemType.UPGRADE,
            StatType.DEXTERITY, 7, 300,
            "The burden of sacrifice sharpens holy judgment.", "crown.png"),

    // --- EQUIPMENT ---

    /**
     * The Silver crucifix.
     */
    SILVER_CRUCIFIX("Silver Crucifix", Item.ItemType.EQUIPMENT, StatType.STAMINA, 5, 200,
            "A heavy cross that burns demons on touch.", "chestPlate.png"),

    /**
     * The Reliquary blade.
     */
    RELIQUARY_BLADE("Reliquary Blade", Item.ItemType.EQUIPMENT,
            StatType.STRENGTH, 5, 200,
            "A consecrated sword sealed with saintly relics.", "reliquaryblade.png"),

    /**
     * The Penitents chain.
     */
    PENITENTS_CHAIN("Penitent's Chain", Item.ItemType.EQUIPMENT,
            StatType.STAMINA, 15, 800,
            "Heavy chains carried in devotion strengthen resolve.", "chain.png"),

    // --- SKILL TOMES ---

    /**
     * The Scripture of flames.
     */
    SCRIPTURE_OF_FLAMES("Scripture of Flames", Item.ItemType.SKILL_TOME,
            StatType.WISDOM, 5, 200,
            "Holy Inferno", "flametome.png"),

    /**
     * The Tome of smiting.
     */
    TOME_OF_SMITING("Tome of Smiting", Item.ItemType.SKILL_TOME, StatType.STRENGTH, 5, 200,
            "Divine Smite", "tome.png");


    // Enum fields matching the Item constructor
    private final String itemName;
    private final Item.ItemType type;
    private final StatType statAffected;
    private final int bonusValue;
    private final int cost;
    private final String itemDescription;
    private final String portraitKey;

    ItemCatalog(String itemName, Item.ItemType type, StatType statAffected, int bonusValue,
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
     *
     * @return A new Item object based on these predefined stats.
     */
    public Item create() {
        return new Item(itemName, type, statAffected, bonusValue, cost, itemDescription, portraitKey);
    }
}