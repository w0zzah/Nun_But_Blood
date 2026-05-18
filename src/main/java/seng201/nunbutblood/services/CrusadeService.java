package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The brain of the Crusade. Handles the math, stamina costs, and
 * progression logic, returning data for the UI to display.
 */
public class CrusadeService {

    private int currentStep = 0;
    private final int totalSteps = 5;
    private final StringBuilder logHistory = new StringBuilder("Click 'Exorcise' to roll the die. Roll higher 5 times and you win!\n");
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
        int roll = 20;
        int bonus = saint.getAttributeValue(demon.getWeakness());
        int total = roll + bonus;
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


        String logEntry = String.format("[Roll : %d] (%d+%d) %s used %s. Result: %s",
                (roll + bonus), roll, bonus, saint.getName(), demon.getWeakness(), type.toUpperCase());
        addToLog(logEntry);


        return new StepResult(roll, type, narrative, logEntry, roll < 10);
    }

    public boolean checkVictory() {
        return currentStep >= totalSteps;
    }

    public boolean checkGameOver(List<Saints> party) {
        // Game is over if all party members are below the required stamina to act
        return party.stream().allMatch(s -> s.getCurrentStamina() < 5);
    }

    public int demonReward(Demon demon) {
        // Simple loot logic based on difficulty
        return demon.getDifficultyRating() * 10;
    }


    public int getCurrentStep() { return currentStep; }

    public String getLog() { return logHistory.toString(); }

    private void addToLog(String message) {
        logHistory.insert(0, message + "\n"); // Newest messages at the top
    }

    private final List<CrusadeEvent> activeEvents = new ArrayList<>();


    /**
     * Rolls for a mid-crusade random event. Call this from the controller
     */
    public Optional<CrusadeEvent> rollMidCrusadeEvent(List<Saints> party) {
        if (party.isEmpty() || random.nextDouble() > .9) return Optional.empty();

        Saints target = party.get(random.nextInt(party.size()));
        boolean isBuff = random.nextBoolean();

        StatType[] stats = StatType.values();
        StatType randomStat = stats[random.nextInt(stats.length)];
        int change = isBuff ? (random.nextInt(3) + 2) : -(random.nextInt(3) + 1);
        applyStat(target, randomStat, change);
        String description = String.format("%s's %s changes by %d", target.getName(), randomStat.label, change);

        addToLog(description);

        CrusadeEvent event = new CrusadeEvent(target, randomStat, change, description);
        activeEvents.add(event);
        return Optional.of(event);
    }

    /**
     * reverse changes
     */
    public void revertEvents() {
        for (CrusadeEvent event : activeEvents) {
            applyStat(event.target(), event.stat(), -event.change());
        }
        activeEvents.clear();
    }


    private void applyStat(Saints saint, StatType stat, int amount) {
        switch (stat) {
            case STRENGTH   -> saint.changeStrength(amount);
            case WISDOM     -> saint.changeWisdom(amount);
            case DEXTERITY  -> saint.changeDexterity(amount);
            case PERCEPTION -> saint.changePerception(amount);
            case STAMINA    -> saint.changeMaxStamina(amount);
        }
    }

}
