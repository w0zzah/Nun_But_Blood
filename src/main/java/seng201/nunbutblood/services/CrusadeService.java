package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Covenant;
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

        // 2. Determine Outcome
        // Logic: Success if Roll + Saint Attribute >= Demon Difficulty
        // We assume 'demon.getWeakness()' returns a string matching a Saint's attribute
        int bonus = saint.getAttributeValue(demon.getWeakness());
        boolean isSuccess = (roll + bonus) >= demon.getDifficultyRating();

        // 3. Define the "Type" (This string maps directly to CSS: .dice-crit, .dice-success, etc.)
        String type = "fail";
        if (roll == 20) type = "crit";
        else if (isSuccess) type = "success";

        // 4. Handle Progression and Costs
        // Every roll costs stamina, regardless of success
        saint.consumeStamina(20);

        String narrative;
        if (type.equals("crit")) {
            currentStep += 2; // Crits move you forward faster
            narrative = "DIVINE INTERVENTION! " + saint.getName() + " strikes a shattering blow!";
        } else if (isSuccess) {
            currentStep += 1;
            narrative = saint.getName() + " forces the demon back!";
        } else {
            narrative = "The demon resists! The party falters...";
        }

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
        return party.stream().allMatch(s -> s.getCurrentStamina() < 20);
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