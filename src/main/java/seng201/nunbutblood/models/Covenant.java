package seng201.nunbutblood.models;

import seng201.nunbutblood.services.BankService;
import seng201.nunbutblood.services.CovenantService;
import seng201.nunbutblood.services.SaintsService;

import java.util.List;

/**
 * Represents the player's Covenant.
 * Stores information about saints, faith, score, crusades, and inventory.
 */
public class Covenant {

    /** Name of the covenant. */
    private final String name;

    /** Handles saint roster management. */
    private final SaintsService roster;

    /** Handles faith and economy logic. */
    private final BankService wallet;

    /** Handles crusade progression and score tracking. */
    private final CovenantService covenantService;

    /** Stores all owned inventory items. */
    private final java.util.ArrayList<Item> rawInventory;

    /**
     * Creates a new covenant.
     *
     * @param name Name of the covenant
     * @param totalCrusades Total number of crusades in the game
     * @param difficulty Difficulty setting used for starting faith
     */
    public Covenant(String name, int totalCrusades, String difficulty) {
        this.name = name;
        this.roster = new SaintsService();
        this.wallet = new BankService(difficulty);
        this.covenantService = new CovenantService(totalCrusades);
        this.rawInventory = new java.util.ArrayList<>();
    }

    /**
     * Handles all end-of-crusade updates.
     * Rewards faith, applies upkeep, updates score,
     * advances crusade count, and refreshes saint stamina.
     *
     * @param reward Faith earned from the crusade
     * @param scoreReward Score earned from the crusade
     */
    public void postCrusade(int reward, int scoreReward) {

        wallet.addFaith(reward);

        int upkeepCost = roster.calculateUpKeep();
        wallet.payUpKeep(upkeepCost);

        covenantService.changeScore(scoreReward);
        covenantService.advanceCrusade();

        roster.incrementCrusadeCounts();
        roster.refreshStamina();
    }

    /**
     * Checks whether the player has lost the game.
     * Player loses if they have no saints and no faith.
     *
     * @return True if game is lost
     */
    public boolean hasLost() {
        return roster.isEmpty() && wallet.getFaith() <= 0;
    }

    /**
     * Gets the covenant name.
     *
     * @return Covenant name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current amount of faith.
     *
     * @return Current faith total
     */
    public int getFaith() {
        return wallet.getFaith();
    }

    /**
     * Gets the total number of crusades in the game.
     *
     * @return Total crusades
     */
    public int getTotalCrusades() {
        return covenantService.getTotalCrusades();
    }

    /**
     * Gets the current crusade number.
     *
     * @return Current crusade
     */
    public int getCurrentCrusade() {
        return covenantService.getCurrentCrusade();
    }

    /**
     * Gets the total amount of faith earned during the game.
     *
     * @return Total faith earned
     */
    public int getTotalFaithEarned() {
        return wallet.getTotalFaithEarned();
    }

    /**
     * Gets the current covenant score.
     *
     * @return Current score
     */
    public int getScore() {
        return covenantService.getScore();
    }

    /**
     * Checks whether all crusades are complete.
     *
     * @return True if game is complete
     */
    public boolean isGameComplete() {
        return covenantService.isGameComplete();
    }

    /**
     * Gets all saints currently in the active party.
     *
     * @return Active party list
     */
    public List<Saints> getActiveParty() {
        return roster.getActiveParty();
    }

    /**
     * Gets all saints currently in reserves.
     *
     * @return Reserve saint list
     */
    public List<Saints> getReserves() {
        return roster.getReserves();
    }

    /**
     * Gets every saint owned by the player.
     *
     * @return List of all owned saints
     */
    public List<Saints> getOwnedSaints() {
        return roster.getOwnedSaints();
    }

    /**
     * Adds a saint to the covenant roster.
     *
     * @param saints Saint to add
     */
    public void addSaints(Saints saints) {
        roster.addSaints(saints);
    }

    /**
     * Moves a saint into the active party.
     *
     * @param apostle Saint to activate
     * @return True if activation succeeds
     */
    public boolean activateApostle(Saints apostle) {
        return roster.activateSaint(apostle);
    }

    /**
     * Moves a saint out of the active party.
     *
     * @param apostle Saint to bench
     * @return True if benching succeeds
     */
    public boolean benchApostle(Saints apostle) {
        return roster.benchSaint(apostle);
    }

    /**
     * Permanently removes a saint from the roster.
     *
     * @param apostle Saint to retire
     * @return True if retirement succeeds
     */
    public boolean retireSaint(Saints apostle) {
        return roster.retireSaint(apostle);
    }

    /**
     * Attempts to spend faith.
     *
     * @param amount Amount of faith to spend
     * @return True if payment succeeds
     */
    public boolean spendFaith(int amount) {
        return wallet.spendFaith(amount);
    }

    /**
     * Adds faith to the covenant wallet.
     *
     * @param amount Amount of faith to add
     */
    public void addFaith(int amount) {
        wallet.addFaith(amount);
    }

    /**
     * Gets a read-only version of the inventory.
     *
     * @return Inventory item list
     */
    public List<Item> getInventory() {
        return java.util.Collections.unmodifiableList(rawInventory);
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item Item to add
     */
    public void addItem(Item item) {
        rawInventory.add(item);
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item Item to remove
     * @return True if item was removed
     */
    public boolean removeItem(Item item) {
        return rawInventory.remove(item);
    }
}