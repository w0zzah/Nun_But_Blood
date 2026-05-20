package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Covenant;
import seng201.nunbutblood.models.Item;
import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PostCrusadeService {

    /** Base probability of a random event after a crusade **/
    private static final double BASE_EVENT_CHANCE = 1;
    
    /** Difficulty increase for probability **/
    private static final double HARD_BONUS = 0.20;
    
    /** Skill's that can be learned after battle. */
    private static final String[] SKILLS = {
            "Holy Blessing", "Devout Survivor", "Covered in Blood"
    };
    
    
    private final Random random;
    
    private final boolean hardMode;
    
    public PostCrusadeService(String difficulty, int score, int reward) {
        this.random = new Random();
        this.hardMode = "Hard".equals(difficulty);

    }

    /**
     * Creates random events after the crusade
     *
     */
    public List<String> triggerEvents(Covenant covenant) {

        List<String> events = new ArrayList<>();
        List<Saints> activeParty = new ArrayList<>();
        activeParty.addAll(covenant.getActiveParty());

        if (activeParty.isEmpty()) return events;
        // (condition ? ifTrue : ifFalse) Sets the chance for a random event dependent on difficulty selected
        double chance = BASE_EVENT_CHANCE + (hardMode ? HARD_BONUS : 0);

        /**
         * Change a random stat of the Saint
         */
        if (random.nextDouble() < chance) {
            // Pick random saint
            Saints target = activeParty.get(random.nextInt(covenant.getActiveParty().size()));

            // set the change to either +3 or -2
            int change = random.nextBoolean() ? 3 : -2;

            StatType[] stats = StatType.values();
            StatType randomStat = stats[random.nextInt(stats.length)];

            // Apply the Change
            applyStat(target, change, randomStat);

            String direction = change > 0 ? "gained" : "lost";
            String type = change > 0 ? "divine" : "cursed";

            // add the change to events arraylist<>
            events.add(String.format(" %s %s %d perception from a %s event",
                    target.getName(), direction, change, type));
        }

        // Random skill event
        if (random.nextDouble() < chance * 0.5) {
            Saints target = activeParty.get(random.nextInt(covenant.getActiveParty().size()));
            if (target.getSpecialAbility().isEmpty()) {
                String skill = SKILLS[random.nextInt(SKILLS.length)];
                target.setSpecialAbility(skill);
                events.add(String.format("%s has learned the new ability, %s", target, skill));
            }
        }
        // Chance for saints to retire
        for (Saints saint : activeParty) {
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

    public void applyStat(Saints saint, int amount, StatType stat) {
        switch(stat) {
            case STRENGTH -> saint.changeStrength(amount);
            case WISDOM -> saint.changeWisdom(amount);
            case DEXTERITY -> saint.changeDexterity(amount);
            case STAMINA -> saint.changeMaxStamina(amount);
            case PERCEPTION -> saint.changePerception(amount);
        }
    }
}
