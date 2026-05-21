package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The type Post crusade service.
 */
public class PostCrusadeService {

    /** Base probability of a random event after a crusade **/
    private static final double BASE_EVENT_CHANCE = 0.40;
    
    /** Difficulty increase for probability **/
    private static final double HARD_BONUS = 0.20;
    
    /** Skill's that can be learned after battle. */
    private static final String[] SKILLS = {
            "Holy Blessing", "Devout Survivor", "Covered in Blood"
    };
    
    
    private final Random random;
    
    private final boolean hardMode;

    /**
     * Instantiates a new Post crusade service.
     *
     * @param difficulty the difficulty
     * @param score      the score
     * @param reward     the reward
     */
    public PostCrusadeService(String difficulty) {
        this.random = new Random();
        this.hardMode = "Hard".equals(difficulty);

    }

    /**
     * Creates random events after the crusade
     *
     * @param covenant the covenant
     * @return the list
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

            // Apply the Change

            String direction = change > 0 ? "gained" : "lost";
            String type = change > 0 ? "divine" : "cursed";

            // add the change to events arraylist<>
            events.add(String.format(" %s %s %d perception from a %s event",
                    target.getName(), direction, change, type));
        }

        // Random skill event
        if (random.nextDouble() < chance * 0.5) {
            Saint target = activeParty.get(random.nextInt(covenant.getActiveParty().size()));
            if (target.getSpecialAbility().isEmpty()) {
                String skill = SKILLS[random.nextInt(SKILLS.length)];
                target.setSpecialAbility(skill);
                events.add(String.format("%s has learned the new ability, %s", target, skill));
            }
        }
        // Chance for saints to retire
        for (Saint saint : activeParty) {
            double retireChance = 0.02 + (saint.getCrusadeCount() * 0.03);
            if (random.nextDouble() < retireChance) {
                int total = covenant.getActiveParty().size() + covenant.getReserves().size();
                if (total > 1) {
                    covenant.retireSaint(saint);
                    events.add(String.format("%s has retired, a humble end for our hero...", saint.getName()));
                    break;
                }
            }

        }
        return events;
    }

    /**
     * Apply stat.
     *
     * @param saint  the saint
     * @param amount the amount
     * @param stat   the stat
     */
    public void applyStat(Saint saint, int amount, StatType stat) {
        switch(stat) {
            case STRENGTH -> saint.changeStrength(amount);
            case WISDOM -> saint.changeWisdom(amount);
            case DEXTERITY -> saint.changeDexterity(amount);
            case STAMINA -> saint.changeMaxStamina(amount);
            case PERCEPTION -> saint.changePerception(amount);
        }
    }
}
