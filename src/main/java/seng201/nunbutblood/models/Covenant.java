package seng201.nunbutblood.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the player's covenant — the central hub for game state.
 * Manages the active party, reserves, inventory, faith currency,
 * crusade progression, and win/loss conditions.
 *
 * @author pgi33
 */
public class Covenant {

    /**
     * The covenant's name, set once at game setup.
     */
    private final String name;

    /**
     * Current faith balance, used as currency.
     */
    private int faith;

    /**
     * Total number of crusades chosen at game setup (5–15).
     */
    private final int totalCrusades;

    /**
     * Number of crusades completed so far.
     */
    private int currentCrusade;

    /**
     * Running total of all faith ever received, unaffected by spending.
     */
    private int totalFaithEarned;

    /**
     * Current score accumulated through crusades.
     */
    private int score;

    /**
     * Active party of apostles going on the next crusade. Max 5.
     */
    private final ArrayList<Saints> activeParty;

    /**
     * Reserve apostles not used in the upcoming crusade. Max 5.
     */
    private final ArrayList<Saints> reserves;

    /**
     * Items and equipment held by the covenant.
     */
    private final ArrayList<Item> inventory;


    /**
     * Creates a new Covenant at game start.
     *
     * @param name          player-chosen covenant name (3–15 chars)
     * @param totalCrusades number of crusades chosen (5–15)
     */
    public Covenant(String name, int totalCrusades, String difficulty) {
        this.name = name;
        this.totalCrusades = totalCrusades;
        this.currentCrusade = 1;
        this.activeParty = new ArrayList<>();
        this.reserves = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.totalFaithEarned = 0;
        this.score = 0;
        this.faith = difficulty.equals("Hard") ? 100 : 300;
    }


    /**
     * @return the covenant's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return current faith balance
     */
    public int getFaith() {
        return faith;
    }

    /**
     * @return total number of crusades chosen at game setup
     */
    public int getTotalCrusades() {
        return totalCrusades;
    }

    /**
     * @return number of crusades completed so far
     */
    public int getCurrentCrusade() {
        return currentCrusade;
    }

    /**
     * @return number of crusades still to complete
     */
    public int getRemainingCrusades() {
        return totalCrusades - currentCrusade;
    }

    /**
     * @return total faith ever received, not affected by spending
     */
    public int getTotalFaithEarned() {
        return totalFaithEarned;
    }

    /**
     * @return current score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return unmodifiable view of the active party
     */
    public List<Saints> getActiveParty() {
        return Collections.unmodifiableList(activeParty);
    }

    /**
     * @return unmodifiable view of the reserves
     */
    public List<Saints> getReserves() {
        return Collections.unmodifiableList(reserves);
    }

    /**
     * @return unmodifiable view of the inventory
     */
    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }


    /**
     * Adds a newly hired apostle to the reserves.
     *
     * @param saint the apostle to add
     * @return true if added, false if reserves are full (max 5)
     */
    public boolean addSaint(Saints saint) {
        if (reserves.size() >= 5) return false;
        reserves.add(saint);
        return true;
    }

    /**
     * Moves an apostle from reserves to the active party.
     *
     * @param apostle the apostle to activate
     * @return true if successful, false if party is full or apostle not in reserves
     */
    public boolean activateApostle(Saints apostle) {
        if (activeParty.size() >= 5) return false;
        if (!reserves.contains(apostle)) return false;
        reserves.remove(apostle);
        activeParty.add(apostle);
        return true;
    }

    /**
     * Moves an apostle from the active party to reserves.
     * The active party must always retain at least one member.
     *
     * @param apostle the apostle to bench
     * @return true if successful, false if party would be empty or reserves are full
     */
    public boolean benchApostle(Saints apostle) {
        if (activeParty.size() <= 1) return false;
        if (reserves.size() >= 5) return false;
        if (!activeParty.contains(apostle)) return false;
        activeParty.remove(apostle);
        reserves.add(apostle);
        return true;
    }

    /**
     * Permanently removes an apostle from the guild.
     * Cannot retire the last remaining apostle across both party and reserves.
     *
     * @param apostle the apostle to retire
     * @return true if retired, false if they are the last apostle or not found
     */
    public boolean retireSaint(Saints apostle) {
        int total = activeParty.size() + reserves.size();
        if (total <= 1) return false;
        if (activeParty.remove(apostle)) return true;
        if (reserves.remove(apostle)) return true;
        return false;
    }

    /**
     * Adds faith to the balance and running total.
     * Negative amounts are ignored.
     *
     * @param amount positive amount of faith to add
     * @return new faith balance
     */
    public int addFaith(int amount) {
        if (amount < 0) return faith;
        faith += amount;
        totalFaithEarned += amount;
        return faith;
    }

    /**
     * Deducts faith from the balance if funds are sufficient.
     *
     * @param amount positive amount to spend
     * @return true if successful, false if insufficient funds
     */
    public boolean spendFaith(int amount) {
        if (amount > faith) return false;
        faith -= amount;
        return true;
    }

    /**
     * Deducts upkeep pay for every apostle in the active party.
     * Called at the start of each crusade. Balance may go negative.
     *
     * @return total faith paid out
     */
    public int payActiveParty() {
        int total = activeParty.stream()
                .mapToInt(Saints::getUpKeep)
                .sum();
        faith -= total;
        return total;
    }

    public void refreshStamina() {
        activeParty.forEach(Saints::restoreStamina);
    }



    /**
     * Adds an item to the covenant's inventory.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        inventory.add(item);
    }

    /**
     * Removes an item from the covenant's inventory.
     *
     * @param item the item to remove
     * @return true if the item was found and removed, false otherwise
     */
    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    /**
     * Adds points to the covenant's score.
     *
     * @param points points to add
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Advances to the next crusade and increments each active party
     * member's crusade count.
     */
    public void advanceCrusade() {
        currentCrusade++;
        activeParty.forEach(Saints::incrementCrusadeCount);

    }

    public void postCrusade(int reward, int score) {
        this.addFaith(reward);
        this.payActiveParty();
        this.addScore(score);
        this.advanceCrusade();
        this.refreshStamina();
    }

    /**
     * Checks whether all crusades have been completed.
     *
     * @return true if the game is won
     */
    public boolean isGameComplete() {
        return currentCrusade >= totalCrusades;
    }

    /**
     * Checks whether the player has lost.
     * Loss occurs when there are no apostles and faith is zero or negative.
     *
     * @return true if the player has lost
     */
    public boolean hasLost() {
        boolean noApostles = activeParty.isEmpty() && reserves.isEmpty();
        return noApostles && faith <= 0;
    }

    public void addSaints(Saints saints) {
        if (activeParty.size() < 5) {
            activeParty.add(saints);
            return;
        }
        if (reserves.size() >= 5) {
            return;
        }
        reserves.add(saints);
    }

    public List<Saints> getOwnedSaints() {
        List<Saints> allSaints = new ArrayList<>(getActiveParty());
        allSaints.addAll(getReserves());
        return allSaints;
    }
}