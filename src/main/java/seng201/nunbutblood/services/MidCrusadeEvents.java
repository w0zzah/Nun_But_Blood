package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.List;
import java.util.Random;

/**
 * Handles picking and executing random text choices/events during a crusade.
 */
public class MidCrusadeEvents {

    private final Random random = new Random();

    /**
     * Data outcome of a single choice.
     */
    public record Choice(
            String text,
            StatType stat,
            int amount,
            Saint saint) {}

    /**
     * An event scenario holding two branching choices.
     */
    public record Event(
            String description,
            Choice choiceA,
            Choice choiceB
    ) {}

    /**
     * Picks a random event out of 15 scenarios and assigns targets from the party.
     *
     * @param activeParty current active list of saints
     * @return the selected event scenario
     */
    public Event getRandomEvent(List<Saint> activeParty) {

        // Possiblity for same
        Saint target = activeParty.get(random.nextInt(activeParty.size()));
        Saint target2 = activeParty.get(random.nextInt(activeParty.size()));

        return switch (random.nextInt(15)) {
            case 0 -> new Event(
                    "A weeping stone statue of the Martyr bleeds warm blood. Do you drink it or pray?",
                    // Choice A (Positive Faith)
                    new Choice(String.format("%s drinks the tears, feeling an unholy fervor.", target, StatType.WISDOM), StatType.WISDOM, 3, target),
                    // Choice B (Negative Sanity)
                    new Choice(String.format("%s kneels to pray, but the whispers break their mind", target2, StatType.SANITY), StatType.SANITY, -2, target2)
            );

            case 1 -> new Event(
                    "The cathedral's iron-wrought bell begins to toll on its own, vibrating through your bones.",
                    // Choice A (Negative Strength)
                    new Choice(String.format("%s tries to steady the heavy rope, straining a muscle", target, StatType.STRENGTH), StatType.STRENGTH, -2, target),
                    // Choice B (Positive Intellect)
                    new Choice(String.format("%s deciphers the rhythmic tolls as a hidden warning", target2, StatType.DEXTERITY), StatType.DEXTERITY, 2, target2)
            );

            case 2 -> new Event(
                    "A flock of plague-ridden ravens bursts from the confessional box!",
                    // Choice A (Positive Dexterity)
                    new Choice(String.format("%s swats them away with lightning reflexes", target, StatType.DEXTERITY), StatType.DEXTERITY, 2, target),
                    // Choice B (Negative Faith)
                    new Choice(String.format("%s views this as an omen of abandonment, losing hope", target2, StatType.WISDOM), StatType.WISDOM, -3, target2)
            );

            case 3 -> new Event(
                    "The Sister Superior's ghost wanders the cloister, clutching a razor-sharp rosary.",
                    // Choice A (Positive Strength)
                    new Choice(String.format("%s wrestles the relic from her spectral grasp!", target, StatType.STRENGTH), StatType.STRENGTH, 2, target),
                    // Choice B (Negative Sanity)
                    new Choice(String.format("%s locks eyes with her hollow sockets and freezes in horror", target2, StatType.SANITY), StatType.SANITY, -3, target2)
            );

            case 4 -> new Event(
                    "The catacomb floor collapses, revealing a pit of bones and ancient texts.",
                    // Choice A (Negative Dexterity)
                    new Choice(String.format("%s slips on the unstable shale and falls hard", target, StatType.DEXTERITY), StatType.DEXTERITY, -2, target),
                    // Choice B (Positive Intellect)
                    new Choice(String.format("%s unearths a lost liturgical manuscript from the debris", target2, StatType.DEXTERITY), StatType.DEXTERITY, 3, target2)
            );

            case 5 -> new Event(
                    "A massive stained-glass window shatters inward under the weight of a gargoyle!",
                    // Choice A (Negative Strength)
                    new Choice(String.format("%s shields the group from falling shards, taking a heavy hit", target, StatType.STRENGTH), StatType.STRENGTH, -3, target),
                    // Choice B (Positive Dexterity)
                    new Choice(String.format("%s gracefully ducks beneath the flying glass", target2, StatType.DEXTERITY), StatType.DEXTERITY, 2, target2)
            );

            case 6 -> new Event(
                    "You discover a hidden spiked discipline belt (scourge) inside a hollowed-out bible.",
                    // Choice A (Positive Faith)
                    new Choice(String.format("%s accepts the penance, hardening their resolve", target, StatType.WISDOM), StatType.WISDOM, 3, target),
                    // Choice B (Negative Strength)
                    new Choice(String.format("%s tries to disarm the trap but cuts their hands deeply", target2, StatType.STRENGTH), StatType.STRENGTH, -2, target2)
            );

            case 7 -> new Event(
                    "The shadows in the scriptorium begin to twist and mimic your movements.",
                    // Choice A (Positive Sanity)
                    new Choice(String.format("%s blankly stares them down, mastering their fear", target, StatType.SANITY), StatType.SANITY, 2, target),
                    // Choice B (Negative Intellect)
                    new Choice(String.format("%s tries to logically comprehend the darkness and gets a migraine", target2, StatType.DEXTERITY), StatType.DEXTERITY, -2, target2)
            );

            case 8 -> new Event(
                    "An altar candle suddenly blazes with an eerie, blinding violet flame.",
                    // Choice A (Positive Intellect)
                    new Choice(String.format("%s recognizes the alchemical property of the flame", target, StatType.DEXTERITY), StatType.DEXTERITY, 2, target),
                    // Choice B (Negative Faith)
                    new Choice(String.format("%s fears it is witchery, doubting the chapel's sanctity", target2, StatType.WISDOM), StatType.WISDOM, -2, target2)
            );

            case 9 -> new Event(
                    "The crypt is flooding with corrupted holy water that smells of sulfur.",
                    // Choice A (Negative Dexterity)
                    new Choice(String.format("%s splashes frantically, wading through the thick muck", target, StatType.DEXTERITY), StatType.DEXTERITY, -2, target),
                    // Choice B (Positive Faith)
                    new Choice(String.format("%s purifies the water with a desperate, fierce prayer", target2, StatType.WISDOM), StatType.WISDOM, 3, target2)
            );

            case 10 -> new Event(
                    "A swarm of locusts pours out of the grand organ pipes.",
                    // Choice A (Positive Strength)
                    new Choice(String.format("%s slams the organ lid shut with brute force", target, StatType.STRENGTH), StatType.STRENGTH, 2, target),
                    // Choice B (Negative Sanity)
                    new Choice(String.format("%s is covered in the biting insects and panics", target2, StatType.SANITY), StatType.SANITY, -3, target2)
            );

            case 11 -> new Event(
                    "A dying heretic offers you a forbidden black book before his last breath.",
                    // Choice A (Positive Intellect)
                    new Choice(String.format("%s reads the texts, gaining dark, forbidden knowledge", target, StatType.DEXTERITY), StatType.DEXTERITY, 3, target),
                    // Choice B (Negative Faith)
                    new Choice(String.format("%s burns it, but the heretic's curses weaken their soul", target2, StatType.WISDOM), StatType.WISDOM, -2, target2)
            );

            case 12 -> new Event(
                    "The heavy iron abbey doors begin to slide shut, threatening to trap you forever.",
                    // Choice A (Positive Dexterity)
                    new Choice(String.format("%s slides underneath the closing door just in time!", target, StatType.DEXTERITY), StatType.DEXTERITY, 3, target),
                    // Choice B (Negative Strength)
                    new Choice(String.format("%s tries to hold it open but gets their arm crushed", target2, StatType.STRENGTH), StatType.STRENGTH, -3, target2)
            );

            case 13 -> new Event(
                    "You find a pristine white habit among charred ruins.",
                    // Choice A (Positive Sanity)
                    new Choice(String.format("%s puts it on, feeling a sense of clean renewal", target, StatType.SANITY), StatType.SANITY, 2, target),
                    // Choice B (Negative Dexterity)
                    new Choice(String.format("%s trips over the long, heavy ceremonial fabric", target2, StatType.DEXTERITY), StatType.DEXTERITY, -1, target2)
            );

            case 14 -> new Event(
                    "A demonic entity mimics the voice of a missing Sister, calling from the well.",
                    // Choice A (Positive Faith)
                    new Choice(String.format("%s rebukes the demon with an unshakeable hymn", target, StatType.WISDOM), StatType.WISDOM, 2, target),
                    // Choice B (Negative Sanity)
                    new Choice(String.format("%s leans in too close, haunted by the whispers", target2, StatType.SANITY), StatType.SANITY, -3, target2)
            );

            default -> null;
        };
    }

    /**
     * Mutates the targeted saint's stats based on the chosen option.
     *
     * @param party the active squad list
     * @param choice the selection made by the player
     * @return summary text describing the exact change
     */
    public String applyChoice(List<Saint> party, Choice choice) {

        applyStat(choice.saint, choice.stat(), choice.amount());

        String changeText = choice.amount() > 0 ? "+" + choice.amount() : String.valueOf(choice.amount());

        return choice.saint.getName() + "'s " + choice.stat().label + " changed by " + changeText + ".";
    }

    private void applyStat(Saint saint, StatType stat, int amount) {
        Saint.applyStat(saint, stat, amount);
    }
}