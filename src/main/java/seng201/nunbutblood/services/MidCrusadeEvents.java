package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.List;
import java.util.Random;

/**
 * Handles picking and executing random text choices/events during a crusade.
 * Infused with classic Gothic literature themes, dark romanticism, and macabre lore.
 */
public class MidCrusadeEvents {

    private final Random random = new Random();

    // Cooldown duration in milliseconds (e.g., 2000 ms = 2 seconds)
    private static final long COOLDOWN_MS = 2000;
    private long lastAppliedTime = 0;

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

        // Possibility for same target tracking
        Saint target = activeParty.get(random.nextInt(activeParty.size()));
        Saint target2 = activeParty.get(random.nextInt(activeParty.size()));

        return switch (random.nextInt(15)) {
            case 0 -> new Event(
                    "An ancient, pallid nobleman offers a dark toast: 'The blood is the life, and it shall be yours.' Do you drink?",
                    new Choice(String.format("%s drinks deeply, embracing a profound, unholy clarity.", target.getName(), StatType.WISDOM), StatType.WISDOM, 2, target),
                    new Choice(String.format("%s recoils in horror, but the carnal temptation shatters their peace of mind.", target2.getName(), StatType.SANITY), StatType.SANITY, -5, target2)
            );

            case 1 -> new Event(
                    "A lightning strike illuminates an operating table where a stitched monstrosity stirs. 'Did I request thee, Maker, from my clay to mould me man?'",
                    new Choice(String.format("%s rushes to sever the galvanic conductors, tearing a tendon.", target.getName(), StatType.STRENGTH), StatType.STRENGTH, -2, target),
                    new Choice(String.format("%s studies the spark of life, deciphering forbidden anatomical secrets.", target2.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 2, target2)
            );

            case 2 -> new Event(
                    "A grim, ungainly raven perches upon a bust of Pallas just above the cloister door, croaking a low 'Nevermore'.",
                    new Choice(String.format("%s hurls a heavy thurible, driving the dark fiend away with sharp precision.", target.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 2, target),
                    new Choice(String.format("%s listens too closely to the prophetic bird, sliding into deep despair.", target2.getName(), StatType.WISDOM), StatType.WISDOM, -3, target2)
            );

            case 3 -> new Event(
                    "A ghostly hand scratches at the ice-coated lattice window, a voice weeping: 'Let me in! I'm come home!'",
                    new Choice(String.format("%s shatters the window frame and forces the weeping specter back into the moors.", target.getName(), StatType.STRENGTH), StatType.STRENGTH, 2, target),
                    new Choice(String.format("%s stares into the hollow, frozen eyes of Catherine's ghost and freezes in raw terror.", target2.getName(), StatType.SANITY), StatType.SANITY, -3, target2)
            );

            case 4 -> new Event(
                    "You discover an elegant, silk-lined velvet coffin surrounded by withered black roses and handwritten love poetry.",
                    new Choice(String.format("%s slips on the slick, unholy oils pooled near the base and falls hard.", target.getName(), StatType.DEXTERITY), StatType.DEXTERITY, -2, target),
                    new Choice(String.format("%s translates the tragic, romantic verses, unearthing hidden artistic insights.", target2.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 3, target2)
            );

            case 5 -> new Event(
                    "High up in the shadows, a heavy stone gargoyle detaches from the vaulted ceiling, falling directly toward the group!",
                    new Choice(String.format("%s throws themselves under the masonry to save the squad, taking a crushing blow.", target.getName(), StatType.STRENGTH), StatType.STRENGTH, -3, target),
                    new Choice(String.format("%s reads the shifting shadows instantly and dodges the plummeting rock.", target2.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 2, target2)
            );

            case 6 -> new Event(
                    "Behind a heavy velvet curtain hangs a hidden portrait. The painted face is twisted in a cruel, decaying sneer, though you remain unscathed.",
                    new Choice(String.format("%s gazes upon the portrait's sins, hardening their own spiritual resolve.", target.getName(), StatType.WISDOM), StatType.WISDOM, 3, target),
                    new Choice(String.format("%s tries to slash the cursed canvas but cuts their hands on splintered glass.", target2.getName(), StatType.STRENGTH), StatType.STRENGTH, -2, target2)
            );

            case 7 -> new Event(
                    "The shadows across the stone floor divide into two distinct shapes: a pious scholar and a brutal, primitive beast.",
                    new Choice(String.format("%s accepts their internal duality, mastering the creeping psychological horror.", target.getName(), StatType.SANITY), StatType.SANITY, 2, target),
                    new Choice(String.format("%s tries to logically separate the light from the dark, inducing a piercing migraine.", target2.getName(), StatType.DEXTERITY), StatType.DEXTERITY, -2, target2)
            );

            case 8 -> new Event(
                    "Deep within the dark woods, an altar candle suddenly flares with an unholy, brilliant violet flame.",
                    new Choice(String.format("%s recognizes the specific alchemical properties of the occult flame.", target.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 2, target),
                    new Choice(String.format("%s fears they have stumbled into a devilish coven, doubting their divine calling.", target2.getName(), StatType.WISDOM), StatType.WISDOM, -2, target2)
            );

            case 9 -> new Event(
                    "A manic, mocking laughter echoes down from a locked attic room, followed by the smell of smoke and kindling.",
                    new Choice(String.format("%s fumbles blindly through the thick smoke, coughing violently.", target.getName(), StatType.DEXTERITY), StatType.DEXTERITY, -2, target),
                    new Choice(String.format("%s dampens the raging flames with a fierce, unwavering prayer.", target2.getName(), StatType.WISDOM), StatType.WISDOM, 3, target2)
            );

            case 10 -> new Event(
                    "The grand pipe organ begins to play a droning, melancholic melody on its own, its chords singing of bat wings and theater capes.",
                    new Choice(String.format("%s slams the heavy wooden keyboard cover down, silencing the dark anthem.", target.getName(), StatType.STRENGTH), StatType.STRENGTH, 2, target),
                    new Choice(String.format("%s falls into a deep, paralyzing existential gloom from the bleak arrangement.", target2.getName(), StatType.SANITY), StatType.SANITY, -3, target2)
            );

            case 11 -> new Event(
                    "An enchanting lady clad in a trailing morning-dress emerges from a carriage, offering a velvet-bound book of secrets.",
                    new Choice(String.format("%s opens the forbidden text, absorbing dark, hypnotic literature.", target.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 3, target),
                    new Choice(String.format("%s burns the unholy text, but her parting, melancholy curse saps their soul.", target2.getName(), StatType.WISDOM), StatType.WISDOM, -2, target2)
            );

            case 12 -> new Event(
                    "The iron gates of the crumbling Italian castle begin to drop rapidly, threatening to lock you in the dungeon forever.",
                    new Choice(String.format("%s performs a desperate, swift slide beneath the iron spikes.", target.getName(), StatType.DEXTERITY), StatType.DEXTERITY, 3, target),
                    new Choice(String.format("%s attempts to hold the dropping portcullis, severely crushing their arm bones.", target2.getName(), StatType.STRENGTH), StatType.STRENGTH, -3, target2)
            );

            case 13 -> new Event(
                    "You uncover a pristine, silk embroidered habit hidden inside a soot-stained, sacrilegious chamber.",
                    new Choice(String.format("%s puts on the solemn robes, feeling a dark, aesthetic transfiguration.", target.getName(), StatType.SANITY), StatType.SANITY, 2, target),
                    new Choice(String.format("%s trips over the excessively long, dramatic velvet train of the garment.", target2.getName(), StatType.DEXTERITY), StatType.DEXTERITY, -1, target2)
            );

            case 14 -> new Event(
                    "Sly, rat-faced merchant creatures call out from a dark glen: 'Come buy our orchard fruits, sweet to tongue and sharp to tooth!'",
                    new Choice(String.format("%s chants an unshakeable, pure hymn to drive the tempters back into the bushes.", target.getName(), StatType.WISDOM), StatType.WISDOM, 2, target),
                    new Choice(String.format("%s tastes a single toxic blackberry, forever haunted by an insatiable hunger.", target2.getName(), StatType.SANITY), StatType.SANITY, -3, target2)
            );

            default -> null;
        };
    }

    /**
     * @param party the active squad list
     * @param choice the selection made by the player
     * @return summary text describing the exact change, or a cooldown warning if spammed
     */
    public String applyChoice(List<Saint> party, Choice choice) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAppliedTime < COOLDOWN_MS) {
            return "The shadows remain unmoved. Rest your weary fingers for a moment.";
        }

        lastAppliedTime = currentTime;

        applyStat(choice.saint, choice.stat(), choice.amount());

        String changeText = choice.amount() > 0 ? "+" + choice.amount() : String.valueOf(choice.amount());

        return choice.saint.getName() + "'s " + choice.stat().label + " shifted by " + changeText + " in the gloom.";
    }

    private void applyStat(Saint saint, StatType stat, int amount) {
        Saint.applyStat(saint, stat, amount);
    }
}