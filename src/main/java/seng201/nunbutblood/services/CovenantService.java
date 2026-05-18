package seng201.nunbutblood.services;
import seng201.nunbutblood.models.Covenant;

public class CovenantService {

    private int score;
    private int currentCrusade;
    private final int totalCrusades;


    public CovenantService(int totalCrusades) {
        this.score = 0;
        this.currentCrusade = 0;
        this.totalCrusades = totalCrusades;
    }

    public int getScore() {
        return score;
    }

    public int getTotalCrusades() {
        return totalCrusades;
    }

    public int getCurrentCrusade() {
        return currentCrusade;
    }

    // Can go negative
    public void changeScore(int points) {
        score += points;
    }

    public void advanceCrusade() {
        if (currentCrusade < totalCrusades) {
            currentCrusade += 1;
        } else { throw new IllegalStateException("currentCrusades cannot be greater than totalCrusades"); }
    }

    public boolean isGameComplete() {
        return currentCrusade >= totalCrusades;
    }

}
