package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.Saints;

import java.util.List;
import java.util.Random;

/**
 * The brain of the Crusade. Handles the math, stamina costs, and
 * progression logic, returning data for the UI to display.
 */
public class CrusadeService {

    private int currentStep = 0;
    private final int totalSteps = 5;
    private final StringBuilder logHistory = new StringBuilder("Crusade Initialized. Steel your soul.\n");
    private final Random random = new Random();

    /**
     * Data carrier for the Controller and Animation.
     */
    public record StepResult(
            int roll,
            String type,
            String narrative,
            String logMessage,
            boolean hasDemonEvent
    ) {}

    /**
     * Executes one "turn" of the crusade.
     * @return A StepResult containing all data needed for the animation and UI update.
     */
    public StepResult performStep(Saints saint, Demon demon, List<Saints> party) {
        // 1. Calculate the Roll
        int roll = random.nextInt(20) + 1;
        int bonus = saint.getAttributeValue(demon.getWeakness());
        boolean isSuccess = (roll + (bonus / 2)) >= demon.getDifficultyRating();

        String type = switch(roll) {
            case 20 -> "critical-success";
            case 1 -> "critical-fail";
            default -> isSuccess ? "success" : "fail";
        };

        // 4. Handle Progression and Costs
        // Every roll costs stamina, regardless of success
        saint.consumeStamina(5);

        String narrative;
        if (type.equals("critical-success")) {
            currentStep += 2;
            narrative = "CRITICAL SUCCESS";
        } else if (isSuccess) {
            currentStep += 1;
            narrative = "SUCCESS";
        } else if (type.equals("critical-fail")){
            currentStep -= 1;
            narrative = "CRITICAL FAIL";
        } else { narrative = "FAIL"; }

        // 5. Update the Log
        String logEntry = String.format("[Roll %d] %s used %s. Result: %s",
                roll, saint.getName(), demon.getWeakness(), type.toUpperCase());
        addToLog(logEntry);

        // 6. Return Result (Setting hasDemonEvent to true on low rolls for spice)
        return new StepResult(roll, type, narrative, logEntry, roll < 3);
    }

    public boolean checkVictory() {
        return currentStep >= totalSteps;
    }

    public boolean checkGameOver(List<Saints> party) {
        // Game is over if all party members are below the required stamina to act
        return party.stream().allMatch(s -> s.getCurrentStamina() < 5);
    }

    public int dropLoot(Demon demon) {
        // Simple loot logic based on difficulty
        return demon.getDifficultyRating() * 10;
    }

    public int getCurrentStep() { return currentStep; }

    public String getLog() { return logHistory.toString(); }

    private void addToLog(String message) {
        logHistory.insert(0, message + "\n"); // Newest messages at the top
    }
}