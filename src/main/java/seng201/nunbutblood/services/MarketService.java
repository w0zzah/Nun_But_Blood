package seng201.nunbutblood.services;
/*

 */
import seng201.nunbutblood.models.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Market service manage's Item sold in the matkey
 */
public class MarketService {

    /** List of available saints */
    private List<Saint> availableSaints;

    /** List of available items */
    private final List<Item> availableItems;

    private final Random random;

    /**
     * Contructs new Marketservice and performs the initial refresh
     *
     * @param random the random
     */
    public MarketService(Random random) {
        this.random = new Random();
        this.availableSaints = new ArrayList<>();
        this.availableItems = new ArrayList<>();
        refresh(1);
    }


    /**
     * Refresh the market stock. Called initially and after every crusade.
     * Longer game time leads to stronger Saint
     *
     * @param crusadeNumber the crusade number
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

        List<ItemCatalog> allCatalogItems = new ArrayList<>(Arrays.asList(ItemCatalog.values()));
        Collections.shuffle(allCatalogItems, random);
        int itemsToDraw = Math.min(3, allCatalogItems.size());
        for (int i = 0; i < itemsToDraw; i++) {
            availableItems.add(allCatalogItems.get(i).create());
        }
    }


    // BUY AND SELL FOR SAINTS AND ITEMS


    /**
     * Hire a Saint from the shop
     *
     * @param saint    The saint to be hired
     * @param covenant The Covenant purchasing the saint
     * @return the boolean
     */
    public boolean buySaint(Saint saint, Covenant covenant) {
        if (availableSaints.contains(saint) && covenant.spendFaith(saint.getCost())) {
            availableSaints.remove(saint);
            covenant.addSaint(saint);
            return true;
        }
        return false;
    }


    /**
     * Sells a Saint back to the market for half of their original purchase price
     *
     * @param saint    The Saint to be sold
     * @param covenant The Covenant sell the saint
     * @return the boolean
     */
    public boolean sellSaint(Saint saint, Covenant covenant) {
        if (!covenant.retireSaint(saint)) return false;
        covenant.addFaith(saint.getCost() / 2);
        return true;
    }

    /**
     * Purchase an item for the covenant
     *
     * @param item     Item the covenant is attempting to buy
     * @param covenant Covenant attempting to buy items
     * @return the boolean
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
     *
     * @param item     Item the covenant is attempting to sell
     * @param covenant Covenant attempting to sell the item
     * @return the boolean
     */
    public boolean sellItem(Item item, Covenant covenant) {
        if (!covenant.removeItem(item)) return false;
        covenant.addFaith(item.getCost() / 2);
        return true;
    }



    // getters

    /**
     * Gets available saints.
     *
     * @return List of available saints for sale
     */
    public List<Saint> getAvailableSaints() {
        return availableSaints;
    }

    /**
     * Gets available items.
     *
     * @return List of available items for sale
     */
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
    private Saint generateRandomSaint(int crusadeNumber) {

        Rarity targetRarity = rollRarity(crusadeNumber);

        // Filter the roster to best fit the current crusade number
        List<SaintRoster.Entry> candidates = SaintRoster.getAllSaints().stream()
                .filter(saint -> SaintRoster.create(saint).getRarity() == targetRarity)
                .collect(Collectors.toList());


        // If the filter is unable to produce any saints for the shop, then make a new list anyway
        if (candidates.isEmpty()) {
            candidates = new ArrayList<>(SaintRoster.getAllSaints());
        }

        // Select a random Saint based on candidates
        SaintRoster.Entry entry = candidates.get(random.nextInt(candidates.size()));
        return SaintRoster.create(entry);
    }

    /**
     * Rolls the rarity based on crusade count
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
}
