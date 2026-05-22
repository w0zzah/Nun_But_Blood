package seng201.nunbutblood.services;

import seng201.nunbutblood.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service class responsible for handling post-crusade processing.
 * Triggers random sequence events, stat mutations, ability acquisitions,
 * and handles progressive retirement tracking for Saints based on game difficulty.
 */
public class PostCrusadeService {

    /** Base probability of a random event after a crusade **/
    private static final double BASE_EVENT_CHANCE = 0.40;

    /** Difficulty increase for probability **/
    private static final double HARD_BONUS = 0.20;

    /** Skill's that can be learned after battle. */
    private static final String[] SKILLS = {
            "Holy Blessing", "Devout Survivor"
    };

    /** Random number generator utilized for evaluation of event checks. */
    private final Random random;

    /** Flag indicating whether the game session is configured to Hard difficulty. */
    private final boolean hardMode;

    /**
     * Instantiates a new Post crusade service with configured difficulty mechanics.
     * Evaluates whether hard mode bonus percentages should apply to event processing checks.
     *
     * @param difficulty the chosen game difficulty string (e.g., "Normal" or "Hard")
     */
    public PostCrusadeService(String difficulty) {
        this.random = new Random();
        this.hardMode = "Hard".equals(difficulty);

    }

    /**
     * Evaluates and updates state for unpredictable occurrences targeting active Saints after a crusade.
     * Processes potential stat swings, passive skill allocations, and voluntary retirements.
     *
     * @param covenant the player's active Covenant structure containing the full roster
     * @return a list of user-facing event description strings summarizing what transpired
     */
    public List<String> triggerEvents(Covenant covenant) {

        List<String> events = new ArrayList<>();
        List<Saint> activeParty = new ArrayList<>(covenant.getActiveParty());

        if (activeParty.isEmpty()) return events;
        double chance = BASE_EVENT_CHANCE + (hardMode ? HARD_BONUS : 0);

        /**
         * Change a random stat of the Saint
         */
        if (random.nextDouble() < chance) {
            // Pick random saint
            Saint target = activeParty.get(random.nextInt(covenant.getActiveParty().size()));

            // set the change to either +3 or -2
            int change = random.nextBoolean() ? 3 : -2;

            StatType[] stats = StatType.values();
            StatType randomStat = stats[random.nextInt(stats.length)];

            switch (randomStat) {
                case STRENGTH   -> target.changeStrength(change);
                case WISDOM     -> target.changeWisdom(change);
                case DEXTERITY  -> target.changeDexterity(change);
                case SANITY     -> target.changeMaxSanity(change);
                case PERCEPTION -> target.changePerception(change);
            }

            String direction = change > 0 ? "gained" : "lost";
            String type = change > 0 ? "divine" : "cursed";

            events.add(String.format("%s %s %d %s from a %s event",
                    target.getName(), direction, Math.abs(change), randomStat.key, type));
        }


        if (random.nextDouble() < chance * 0.5) {
            Saint target = activeParty.get(random.nextInt(covenant.getActiveParty().size()));
            if (target.getSpecialAbility().isEmpty()) {
                String skill = SKILLS[random.nextInt(SKILLS.length)];
                switch (skill) {
                    case "Holy Blessing" -> covenant.addFaith(300);
                    case "Devout Survivor" -> covenant.addItem(ItemCatalog.BLOOD_VIAL.create());
                }
                events.add(String.format("%s has Channeled the ability, %s", target, skill));
            }
        }

        if (covenant.getActiveParty().size() > 1) {
            for (Saint saint : activeParty) {
                double retireChance = 0.02 + (saint.getCrusadeCount() * 0.03);
                if (random.nextDouble() < retireChance) {
                    covenant.retireSaint(saint);
                    events.add(String.format("%s has retired, a humble end for our hero...", saint.getName()));
                    break;
                }
            }
        }

        return events;
    }


}