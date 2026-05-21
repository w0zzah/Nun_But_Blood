package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.Saint;
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
    // expedition length
    private final int totalSteps = 3;
    private final StringBuilder logHistory = new StringBuilder("Click 'ROLL' to roll the die. Roll higher 3 times and you win!\n");
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
     *
     * @param saint the saint
     * @param demon the demon
     * @param party the party
     * @param stat  the stat
     * @return A StepResult containing all data needed for the animation and UI update.
     */
    public StepResult performStep(Saint saint, Demon demon, List<Saint> party, StatType stat) {
        //  Calculate the Roll
        int roll = random.nextInt(20) + 1;
        int bonus = saint.getAttributeValue(stat);
        int total = roll + bonus;
        boolean isSuccess = total >= demon.getDifficultyRating();

        String type = switch(roll) {
            case 20 -> "critical-success";
            case 1 -> "critical-fail";
            default -> isSuccess ? "success" : "fail";
        };

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
                (roll + bonus), roll, bonus, saint.getName(), stat, type.toUpperCase());
        addToLog(logEntry);


        return new StepResult(roll, type, narrative, logEntry, roll < 10);
    }

    /**
     * Check victory boolean.
     *
     * @return the boolean
     */
    public boolean checkVictory() {
        return currentStep >= totalSteps;
    }

    /**
     * Check game over boolean.
     *
     * @param party the party
     * @return the boolean
     */
    public boolean checkGameOver(List<Saint> party) {
        // Game is over if all party members are below the required stamina to act
        return party.stream().allMatch(s -> s.getCurrentStamina() < 5);
    }

    /**
     * Demon reward int.
     *
     * @param demon the demon
     * @return the int
     */
    public int demonReward(Demon demon) {
        // Simple loot logic based on difficulty
        return demon.getDifficultyRating() * 10;
    }

    /**
     * Advance step.
     */
    public void advanceStep() {
        currentStep++;
    }


    /**
     * Gets current step.
     *
     * @return the current step
     */
    public int getCurrentStep() { return currentStep; }

    /**
     * Gets total steps.
     *
     * @return the total steps
     */
    public int getTotalSteps() { return totalSteps; }


    /**
     * Gets log.
     *
     * @return the log
     */
    public String getLog() { return logHistory.toString(); }

    private void addToLog(String message) {
        logHistory.insert(0, message + "\n"); // Newest messages at the top
    }

    private final List<CrusadeEvent> activeEvents = new ArrayList<>();


    /**
     * Rolls for a mid-crusade random event. Call this from the controller
     *
     * @param activeParty the active party
     * @return the optional
     */
    public Optional<CrusadeEvent> rollMidCrusadeEvent(List<Saint> activeParty) {
        if (activeParty.isEmpty() || random.nextDouble() > 0.5) return Optional.empty();

        Saint target = activeParty.get(random.nextInt(activeParty.size()));
        boolean isBuff = random.nextBoolean();

        StatType[] stats = StatType.values();
        StatType randomStat = stats[random.nextInt(stats.length)];
        int change = isBuff ? (random.nextInt(3) + 2) : -(random.nextInt(3) + 1);
        Saint.applyStat(target, randomStat, change);
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
            Saint.applyStat(event.target(), event.stat(), -event.change());
        }
        activeEvents.clear();
    }


}
