package seng201.nunbutblood.services;


/**
 * BankService handles the faith of the Coven.
 * <br>
 * FUNCTIONS:
 * <p>
 * {@link #getFaith()} -> Get the CovensFaith <br>
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
     * Adds faith to the player's wallet.
     * Also updates total faith earned.
     *
     * @param faithIncrease Amount of faith to add
     */
    public void addFaith(int faithIncrease) {
        if (faithIncrease >= 0) {
            faith += faithIncrease;
            totalFaithEarned += faithIncrease;
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