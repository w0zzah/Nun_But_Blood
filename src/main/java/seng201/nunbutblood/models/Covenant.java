package seng201.nunbutblood.models;

import seng201.nunbutblood.services.BankService;
import seng201.nunbutblood.services.CovenantService;
import seng201.nunbutblood.services.SaintService;
import seng201.nunbutblood.services.DemonService;

import java.util.List;

/**
 * Represents the player's Covenant.
 * Stores information about saints, faith, score, crusades, and inventory.
 * Acts as the structural hub linking various sub-domain managers together.
 */
public class Covenant {

    /** Name of the covenant. */
    private final String name;

    /** Handles saint roster management. */
    private final SaintService roster;

    /** Handles faith and economy logic. */
    private final BankService bank;

    /** Handles crusade progression and score tracking. */
    private final CovenantService covenantService;

    /** Stores all owned inventory items. */
    private final List<Item> rawInventory;

    /** Configured gameplay balance difficulty setting modifier string. */
    private final String difficulty;

    /** Handles generation and management of current crusade target encounter options. */
    private final DemonService demonService;

    /**
     * Creates a new covenant.
     *
     * @param name          Name of the covenant
     * @param totalCrusades Total number of crusades in the game
     * @param difficulty    Difficulty setting used for starting faith
     */
    public Covenant(String name, int totalCrusades, String difficulty) {
        this.name = name;
        this.roster = new SaintService();
        this.bank = new BankService(difficulty);
        this.covenantService = new CovenantService(totalCrusades);
        this.rawInventory = new java.util.ArrayList<>();
        this.difficulty = difficulty;
        this.demonService = new DemonService();
        demonService.generateNewOptions(covenantService.getCurrentCrusade(), difficulty);
    }

    /**
     * Handles all end-of-crusade updates.
     * Rewards faith, applies upkeep, updates score,
     * advances crusade count, and refreshes saint sanity.
     *
     * @param reward      Faith earned from the crusade
     * @param scoreReward Score earned from the crusade
     * @param isVictory   the is victory
     */
    public void postCrusade(int reward, int scoreReward, boolean isVictory) {
        bank.addFaith(reward);
        int upkeepCost = roster.calculateUpKeep();
        bank.payUpKeep(upkeepCost);
        covenantService.changeScore(scoreReward);
        roster.incrementCrusadeCounts();

        for(Saint saint : roster.getOwnedSaints()) {
            saint.clearConsumable();
        }

        if (isVictory) {
            covenantService.advanceCrusade();
            roster.refreshSanity();
        }
        demonService.generateNewOptions(covenantService.getCurrentCrusade(), difficulty);

    }

    /**
     * Checks whether the player has lost the game.
     * Player loses if they have no saints and no faith.
     *
     * @return True if game is lost
     */
    public boolean hasLost() {
        boolean outOfSanitySaints = roster.allSaintsExhausted();
        boolean cannotAffordNewSaint = bank.getFaith() <= 200;

        return outOfSanitySaints && cannotAffordNewSaint;
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
        return bank.getFaith();
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
     * Gets the demon service containing the current crusade's demon options.
     *
     * @return The active DemonService instance
     */
    public DemonService getDemonService() {
        return this.demonService;
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
    public List<Saint> getActiveParty() {
        return roster.getActiveParty();
    }

    /**
     * Gets all saints currently in reserves.
     *
     * @return Reserve saint list
     */
    public List<Saint> getReserves() {
        return roster.getReserves();
    }

    /**
     * Gets every saint owned by the player.
     *
     * @return List of all owned saints
     */
    public List<Saint> getOwnedSaints() {
        return roster.getOwnedSaints();
    }

    /**
     * Gets the selected difficulty as a string
     *
     * @return Difficulty String
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Adds a saint to the covenant roster.
     *
     * @param saint Saint to add
     */
    public void addSaint(Saint saint) {
        roster.addSaint(saint);
    }

    /**
     * Moves a saint into the active party.
     *
     * @param saint Saint to activate
     * @return True if activation succeeds
     */
    public boolean activateSaint(Saint saint) {
        return roster.activateSaint(saint);
    }

    /**
     * Moves a saint out of the active party.
     *
     * @param saint Saint to bench
     * @return True if benching succeeds
     */
    public boolean benchSaint(Saint saint) {
        return roster.benchSaint(saint);
    }

    /**
     * Permanently removes a saint from the roster.
     *
     * @param saint Saint to retire
     * @return True if retirement succeeds
     */
    public boolean retireSaint(Saint saint) {
        return roster.retireSaint(saint);
    }

    /**
     * Attempts to spend faith.
     *
     * @param amount Amount of faith to spend
     * @return True if payment succeeds
     */
    public boolean spendFaith(int amount) {
        return bank.spendFaith(amount);
    }

    /**
     * Adds faith to the covenant bank.
     *
     * @param amount Amount of faith to add
     */
    public void addFaith(int amount) {
        bank.addFaith(amount);
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
     * Add item.
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