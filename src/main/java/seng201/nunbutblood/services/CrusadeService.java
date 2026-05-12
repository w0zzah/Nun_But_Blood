package seng201.nunbutblood.services;

import com.sun.jdi.request.StepRequest;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.Demon;
import java.util.List;
import java.util.Random;

public class CrusadeService {

    private static final int TARGET_STEPS = 5;
    private static final int STAMINA_COST = 20;
    private static final int BACKTRACK_STAMINA_PENALTY = 30;

    private int currentStep;
    private int difficultyModifier; // For random events
    private final Random random;
    private final StringBuilder log;

    public CrusadeService() {
        this.currentStep = 0;
        this.difficultyModifier = 0;
        this.random = new Random();
        this.log = new StringBuilder();
    }

    public StepResult performStep(Saints saint, Demon demon, List<Saints> entireParty) {
        // 1. Check Stamina Requirement
        if (saint.getCurrentStamina() < STAMINA_COST) {
            return new StepResult(currentStep, 0, 0, false, "Apostle too exhausted to proceed!");
        }

        // Deduct initial cost
        saint.consumeStamina(STAMINA_COST);

        // 2. Trigger Random Event (e.g., 15% chance)
        handleRandomEvent(demon, entireParty);

        // 3. Roll Mechanics
        int roll = random.nextInt(20) + 1;
        String eventNarrative = "";
        int total = 0;

        if (roll == 20) {
            currentStep = Math.min(TARGET_STEPS, currentStep + 2);
            eventNarrative = "⚡ CRITICAL SUCCESS! Advanced 2 steps!";
        } else if (roll == 1) {
            if (currentStep > 0) {
                currentStep--;
                eventNarrative = "💀 CRITICAL FAILURE! Slipped back one step.";
            } else {
                saint.consumeStamina(BACKTRACK_STAMINA_PENALTY);
                eventNarrative = "💀 CRITICAL FAILURE! Cannot go back further; lost 30 extra Stamina!";
            }
        } else {
            // Formula: roll + (saint stat) - 10 / 2
            // Note: Integer division in Java naturally floors the result
            total = roll + (saint.getStatBonus() - 10) / 2;

            int effectiveDifficulty = demon.getDifficultyRating() + difficultyModifier;

            if (total >= effectiveDifficulty) {
                currentStep++;
                eventNarrative = String.format("✅ Success! (Roll %d vs %d Diff). Advanced 1 step.", total, effectiveDifficulty);
            } else {
                eventNarrative = String.format("❌ Failure (Roll %d vs %d Diff). No progress made.", total, effectiveDifficulty);
            }
        }

        // reset temporary difficulty modifiers
        difficultyModifier = 0;

        String finalLog = "Step " + currentStep + ": " + eventNarrative;
        log.append(finalLog).append("\n");


        return new StepResult(currentStep, roll, total, currentStep >= TARGET_STEPS, finalLog);
    }

    private void handleRandomEvent(Demon demon, List<Saints> party) {
        double chance = random.nextDouble();

        if (chance < 0.10) {
            for (Saints saint : party) saint.consumeStamina(10);
            log.append("THE DEMON CURSES YOUR TEAM: -10 stamina");
        }

        else if (chance < 0.20) {
            difficultyModifier = 2;
            log.append(("THE DEMON SCREAMS IN RAGE: +2 Difficulty"));
        }
    }

    public  boolean checkVictory() {
        return currentStep >= TARGET_STEPS;
    }

    public boolean checkGameOver(List<Saints> party) {
        return party.stream().allMatch((saint -> saint.getCurrentStamina() < STAMINA_COST));
    }

    public int getCurrentStep() { return currentStep; }

    public String getLog() {return log.toString(); }

    public int dropLoot(Demon currentDemon, int size) {
        return 5;
    }

    public record StepResult(int step, int roll, int total, boolean isVictory, String narrative) {}

}
