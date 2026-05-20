package seng201.nunbutblood.services;
import seng201.nunbutblood.services.SaintsService;

/**
 * The type Covenant service.
 */
public class CovenantService {

    private int score;
    private int currentCrusade;
    private final int totalCrusades;
    private SaintsService saintsService;

    /**
     * Instantiates a new Covenant service.
     *
     * @param totalCrusades the total crusades
     */
    public CovenantService(int totalCrusades) {
        this.score = 0;
        this.currentCrusade = 0;
        this.totalCrusades = totalCrusades;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets total crusades.
     *
     * @return the total crusades
     */
    public int getTotalCrusades() {
        return totalCrusades;
    }

    /**
     * Gets current crusade.
     *
     * @return the current crusade
     */
    public int getCurrentCrusade() {
        return currentCrusade;
    }

    /**
     * Change score.
     *
     * @param points the points
     */
// Can go negative
    public void changeScore(int points) {
        score += points;
    }

    /**
     * Advance crusade.
     */
    public void advanceCrusade() {
        if (currentCrusade < totalCrusades) {
            currentCrusade += 1;
        } else { throw new IllegalStateException("currentCrusades cannot be greater than totalCrusades"); }
    }

    /**
     * Is game complete boolean.
     *
     * @return the boolean
     */
    public boolean isGameComplete() {
        return currentCrusade >= totalCrusades;
    }

}
