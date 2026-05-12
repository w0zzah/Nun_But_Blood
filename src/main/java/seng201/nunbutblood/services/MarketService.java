package seng201.nunbutblood.services;
/*

 */
import seng201.nunbutblood.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Market service manage's Item sold in the matkey
 */
public class MarketService {

    /** List of available saints */
    private List<Saints> availableSaints;

    /** List of available items */
    private List<Item> availableItems;

    private final Random random;

    /** Every Item possible to be stocked in the market */
    private static final List<Item> ITEM_POOL = buildItemPool();

    /**
     * Contructs new Marketservice and performs the initial refresh
     *
     */

    public MarketService() {
        this.random = new Random();
        this.availableSaints = new ArrayList<>();
        this.availableItems = new ArrayList<>();
        refresh(1);
    }


    /**
     * Refresh the market stock. Called initially and after every crusade.
     * Longer game time leads to stronger Saints
     */

    public void refresh(int crusadeNumber) {

        // Clear both list's
        availableSaints.clear();
        availableItems.clear();

        // Generate a list of random saints based on crusade's completed
        int saintCount = 3 + random.nextInt(3);
        for (int i = 0; i < saintCount; i++) {
            availableSaints.add(generateRandomSaint(crusadeNumber));
        }

        // Generate a list of random items from the ITEM_POOL
        List<Item> newItems = new ArrayList<>(ITEM_POOL);
        Collections.shuffle(newItems, random);
        int itemCount = Math.min(3 + random.nextInt(3), newItems.size());
        availableItems.addAll(newItems.subList(0, itemCount));

    }


    // BUY AND SELL FOR SAINTS AND ITEMS


    /**
     * Hire a Saint from the shop
     *
     * @param saint The saint to be hired
     * @param covenant The Covenant purchasing the saint
     */
    public boolean buySaint(Saints saint, Covenant covenant) {
        if (!availableSaints.contains(saint)) return false;
        if (!covenant.spendFaith(saint.getCost())) return false;
        availableSaints.remove(saint);
        covenant.addSaints(saint);
        return true;
    }

    /**
     * Sells a Saint back to the market for half of their original purchase price
     *
     * @param saint The Saint to be sold
     * @param covenant The Covenant sell the saint
     */
    public boolean sellSaint(Saints saint, Covenant covenant) {
        if (!covenant.retireSaint(saint)) return false;
        covenant.addFaith(saint.getCost() / 2);
        return true;
    }

    /**
     * Purchase an item for the covenant
     *
     * @param item Item the covenant is attempting to buy
     * @param covenant Covenant attempting to buy items
     */
    public boolean buyItem(Item item, Covenant covenant) {
        if (!availableItems.contains(item)) return false;
        if (!covenant.spendFaith(item.getCost())) return false;
        availableItems.remove(item);
        covenant.addItem(item);
        return true;
    }

    /**
     * Sells the back to the market from covenant
     * @param item Item the covenant is attempting to sell
     * @param covenant Covenant attempting to sell the item
     */

    public boolean sellItem(Item item, Covenant covenant) {
        if (!covenant.removeItem(item)) return false;
        covenant.addFaith(item.getCost() / 2);
        return true;
    }



    // getters
    /** @return List of available saints for sale*/
    public List<Saints> getAvailableSaints() {
        return availableSaints;
    }
    /** @return List of available items for sale */
    public List<Item> getAvailableItems() {
        return availableItems;
    }


    /**
     * Generate's a random Saint drawn from ApostleRoster
     *
     * Rarity scales with the crusade number, increasing the chance of
     * stronger saints based on the crusade count
     * @param crusadeNumber current crusade number
     * @return a new Saint from the roster
     */
    private Saints generateRandomSaint(int crusadeNumber) {
        Rarity targetRarity = rollRarity(crusadeNumber);

        // Filter the roster to best fit the current crusade number
        List<SaintsRoster.Entry> candidates = SaintsRoster.getAllSaints().stream()
                .filter(w -> SaintsRoster.create(w).getRarity() == targetRarity)
                .collect(Collectors.toList());


        // If the filter is unable to produce any saints for the shop, then make a new list anyway
        if (candidates.isEmpty()) {
            candidates = new ArrayList<>(SaintsRoster.getAllSaints());
        }

        // Select a random Saint based on candidates
        SaintsRoster.Entry entry = candidates.get(random.nextInt(candidates.size()));
        return SaintsRoster.create(entry);
    }

    /**
     * Rolls the rarity based on crusade count
     *
     *
     */
    private Rarity rollRarity(int crusadeNumber) {
        int roll = random.nextInt(100);
        if (crusadeNumber <= 2) {
            if (roll < 60) return Rarity.COMMON;
            if (roll < 80) return Rarity.RARE;
            return Rarity.EPIC;
        }
        else if (crusadeNumber <= 4) {
            if (roll < 50) return Rarity.COMMON;
            if (roll < 70) return Rarity.RARE;
            if (roll < 85) return Rarity.EPIC;
            return Rarity.LEGENDARY;
        }
        else if (crusadeNumber <= 6) {
            if (roll < 40) return Rarity.COMMON;
            if (roll < 65) return Rarity.RARE;
            if (roll < 80) return Rarity.EPIC;
            return Rarity.LEGENDARY;
        }
        else if (crusadeNumber <= 10) {
            if (roll < 30) return Rarity.COMMON;
            if (roll < 50) return Rarity.RARE;
            if (roll < 70) return Rarity.EPIC;
            return Rarity.LEGENDARY;
        }
        else {
            if (roll < 30) return Rarity.RARE;
            if (roll < 60) return Rarity.EPIC;
            return Rarity.LEGENDARY;
        }
    }
    /**
     * Builds the static pool of all possible items the Market can stock.
     *
     * @return list of all item definitions
     */
    private static List<Item> buildItemPool() {
        List<Item> pool = new ArrayList<>();
        // Consumables
        pool.add(new Item("Holy Salve", Item.ItemType.UPGRADE, "stamina", 20, 60,
                "A vial of blessed oil. Restores 20 Stamina."));
        pool.add(new Item("Perception Powder", Item.ItemType.UPGRADE, "perception", 5, 80,
                "Fine dust that sharpens the senses. +5 Perception."));
        pool.add(new Item("War Unction", Item.ItemType.UPGRADE, "strength", 5, 70,
                "An anointing that hardens the arm. +5 to strength for one round"));
        // Gear
        pool.add(new Item("Iron Rosary", Item.ItemType.EQUIPMENT, "perception", 8, 120,
                "A heavy iron rosary, focus for the devout. +8 Perception while equipped."));
        pool.add(new Item("Blessed Gauntlets", Item.ItemType.EQUIPMENT, "strength", 3, 130,
                "Gauntlets etched with psalms. +3 to strength while equipped."));
        pool.add(new Item("Pilgrim's Boots", Item.ItemType.EQUIPMENT, "stamina", 30, 110,
                "Boots worn thin by a thousand miles. +30 Stamina while equipped."));
        // Skill Tomes
        pool.add(new Item("Tome of Discernment", Item.ItemType.SKILL_TOME, "NONE", 0, 150,
                "Grants the Apostle the ability to sense a Demon's weakness before rolling."));
        pool.add(new Item("Codex of Fortitude", Item.ItemType.SKILL_TOME, "NONE", 0, 140,
                "Teaches the Apostle to endure — they no longer lose Stamina on cursed checks."));
        return pool;
    }
}
