package seng201.nunbutblood.models;

/**
 * A central registry of all available items in the game.
 * Use this enum to easily spawn predefined items.
 */
public enum ItemCatalog {

// --- CONSUMABLES ---

    /**
     * The Blood vial.
     */
    BLOOD_VIAL("Blood Vial", Item.ItemType.CONSUMABLE, StatType.STRENGTH, 4, 150,
            "Thick, vital fluid that causes a burst of unholy power.", "bloodVial.png"),

    /**
     * The Calcified heart.
     */
    CALCIFIED_HEART("Calcified Heart", Item.ItemType.CONSUMABLE, StatType.SANITY, 8, 250,
            "A petrified heart that temporarily numbs terror.", "calcifiedHeart.png"),

    /**
     * The Eye candy.
     */
    EYE_CANDY("Eye Candy", Item.ItemType.CONSUMABLE, StatType.PERCEPTION, 5, 200,
            "A vibrant sweet that allows the consumer to spot hidden blessings.", "eyeCandy.png"),

    /**
     * The Flambe stew.
     */
    FLAMBE_STEW("Flambé Stew", Item.ItemType.CONSUMABLE, StatType.DEXTERITY, 4, 150,
            "A searing broth that sets the nerves on fire, quickening movements.", "flambeStew.png"),

    /**
     * The Shriek juice.
     */
    SHRIEK_JUICE("Shriek Juice", Item.ItemType.CONSUMABLE, StatType.WISDOM, 4, 180,
            "An agonizing brew that forces the mind to perceive cosmic echoes.", "shriekJuice.png"),

// --- EQUIPMENT ---

    /**
     * The Circlet.
     */
    CIRCLET("Sacred Circlet", Item.ItemType.EQUIPMENT, StatType.WISDOM, 3, 250,
            "A woven headband that focuses divine thoughts permanently.", "circlet.png"),

    /**
     * The Prayer tome.
     */
    PRAYER_TOME("Prayer Tome", Item.ItemType.EQUIPMENT, StatType.SANITY, 12, 600,
            "A well-worn scripture providing permanent spiritual resilience.", "prayerTome.png"),

    /**
     * The Skull candle.
     */
    SKULL_CANDLE("Skull Candle", Item.ItemType.EQUIPMENT, StatType.PERCEPTION, 3, 300,
            "Its eerie flickering flame uncovers secret artifacts in the dark.", "skullCandle.png"),

    /**
     * The Tome.
     */
    TOME("Ancient Tome", Item.ItemType.EQUIPMENT, StatType.WISDOM, 5, 500,
            "Heavy text detailing lost defensive incantations.", "tome.png"),

    /**
     * The VooDool.
     */
    VOO_DOOL("Voodoo Doll", Item.ItemType.EQUIPMENT, StatType.STRENGTH, 3, 350,
            "A cursed doll whose needles redirect physical pain back outward.", "vooDool.png"),
    ;


    // Enum fields matching the Item constructor
    private final String itemName;
    private final Item.ItemType type;
    private final StatType statAffected;
    private final int bonusValue;
    private final int cost;
    private final String itemDescription;
    private final String portraitKey;

    /**
     * Internal constructor setting static properties for cataloged assets.
     *
     * @param itemName        Display name of the commodity
     * @param type            Classification grouping (CONSUMABLE vs EQUIPMENT)
     * @param statAffected    Core attribute modified by the asset
     * @param bonusValue      Numerical value applied during modification
     * @param cost            Faith resource point cost to buy
     * @param itemDescription Flavor and layout reference string
     * @param portraitKey     Resource asset string for UI rendering
     */
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