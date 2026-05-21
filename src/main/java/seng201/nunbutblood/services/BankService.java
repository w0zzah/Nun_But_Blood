package seng201.nunbutblood.services;

import seng201.nunbutblood.models.*;


/**
 * <h1>BankService handles the faith of the Coven.
 * <br>
 * FUNCTIONS: </h1>
 * <p>
 * {@link #getFaith()} -> Get the CovensFaith <br>
 * {@link #getTotalFaithEarned()} -> Get the total faith earned over the game <br>
 * {@link #addFaith(int)} -> Adds faith to the Covenant <br>
 * {@link #spendFaith(int)} -> Spends faith if the Covenant has enough <br>
 * {@link #payUpKeep(int)} -> Pays all Saint who are on the activeParty <br>
 * </p>
 */


public class BankService {

    /** Current amount of faith owned by the player. */
    private int faith;

    /** Total amount of faith earned throughout the game. */
    private int totalFaithEarned;

    /**
     * Creates a new bank service.
     * Starting faith depends on selected difficulty.
     *
     * @param difficulty Selected game difficulty
     */
    public BankService(String difficulty) {
        this.faith = difficulty.equals("Hard") ? 100 : 300;
        this.totalFaithEarned = 0;
    }

    /**
     * Gets the current amount of faith.
     *
     * @return Current faith total
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Gets the total amount of faith earned during the game.
     *
     * @return Total faith earned
     */
    public int getTotalFaithEarned() {
        return totalFaithEarned;
    }

    /**
     * Adds faith to the player's wallet.
     * Also updates total faith earned.
     *
     * @param faithIncrease Amount of faith to add
     * @return Updated faith total
     * @throws IllegalArgumentException If value is negative
     */
    public void addFaith(int faithIncrease) {
        if (faithIncrease >= 0) {
            faith += faithIncrease;
            totalFaithEarned += faithIncrease;
        } else {
            throw new IllegalArgumentException("Value of faith to add is negative");
        }
    }

    /**
     * Attempts to spend faith.
     * Removes faith if the player can afford the cost.
     *
     * @param cost Amount of faith to spend
     * @return True if purchase succeeds
     */
    public boolean spendFaith(int cost) {
        if (cost > 0 && faith >= cost) {
            faith -= cost;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes faith for upkeep costs.
     *
     * @param amount Upkeep amount to pay
     */
    public void payUpKeep(int amount) {
        faith -= amount;
    }
}