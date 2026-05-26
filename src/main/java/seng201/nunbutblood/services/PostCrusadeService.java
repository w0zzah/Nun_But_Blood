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
    private static final double BASE_EVENT_CHANCE = 0.05;

    /** Difficulty increase for probability **/
    private static final double HARD_BONUS = 0.20;

    /** Skills/Events that can be processed after a battle with explicit items mapped. */
    private static final String[] SKILLS = {
            "Collect the tithe (+300 faith)",
            "The Dead travel fast (+Blood Vial)",
            "Mary's Treasure (+Calcified Heart)",
            "Grave Robber's Delight (+Skull Candle)",
            "Visionary Guidance (+Eye Candy)",
            "Kitchen Duty (+Flambé Stew)",
            "Banshee's Echo (+Shriek Juice)",
            "Coronation (+Sacred Circlet)",
            "Forbidden Knowledge (+Necromancy Grimoire)",
            "Ancient Research (+Ancient Tome)",
            "Witchcraft Whispers (+Charged Doll)"
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

        if (random.nextDouble() < chance * 0.5) {
            Saint target = activeParty.get(random.nextInt(covenant.getActiveParty().size()));
            if (target.getSpecialAbility().isEmpty()) {
                String skill = SKILLS[random.nextInt(SKILLS.length)];

                switch (skill) {
                    case "Collect the tithe (+300 faith)" -> covenant.addFaith(300);
                    case "The Dead travel fast (+Blood Vial)" -> covenant.addItem(ItemCatalog.BLOOD_VIAL.create());
                    case "Mary's Treasure (+Calcified Heart)" -> covenant.addItem(ItemCatalog.CALCIFIED_HEART.create());
                    case "Grave Robber's Delight (+Skull Candle)" -> covenant.addItem(ItemCatalog.SKULL_CANDLE.create());
                    case "Visionary Guidance (+Eye Candy)" -> covenant.addItem(ItemCatalog.EYE_CANDY.create());
                    case "Kitchen Duty (+Flambé Stew)" -> covenant.addItem(ItemCatalog.FLAMBE_STEW.create());
                    case "Banshee's Echo (+Shriek Juice)" -> covenant.addItem(ItemCatalog.SHRIEK_JUICE.create());
                    case "Coronation (+Sacred Circlet)" -> covenant.addItem(ItemCatalog.CIRCLET.create());
                    case "Forbidden Knowledge (+Necromancy Grimoire)" -> covenant.addItem(ItemCatalog.PRAYER_TOME.create());
                    case "Ancient Research (+Ancient Tome)" -> covenant.addItem(ItemCatalog.TOME.create());
                    case "Witchcraft Whispers (+Charged Doll)" -> covenant.addItem(ItemCatalog.VOO_DOOL.create());
                }

                events.add(String.format("%s has Channeled the ability, %s", target.getName(), skill));
            }
        }
        return events;
    }
}