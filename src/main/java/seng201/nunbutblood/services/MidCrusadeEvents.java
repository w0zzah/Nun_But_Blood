package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.StatType;

import java.util.List;
import java.util.Random;

public class MidCrusadeEvents {

    private final Random random = new Random();

    public record Choice(
            String text,
            StatType stat,
            int amount
    ) {}

    public record Event(
            String description,
            Choice choiceA,
            Choice choiceB
    ) {}

    public Event getRandomEvent() {
        return switch (random.nextInt(5)) {
            case 0 -> new Event(
                    "HIS POWER LEVEL IS OVER 9000!!! ",
                    new Choice("Side with Goku ? ", StatType.STRENGTH, 2),
                    new Choice("Side with Vegeta ?", StatType.DEXTERITY, 2)
            );

            case 1 -> new Event(
                    "The Epstein files have just been unredacted, Epstein is calling...",
                    new Choice("Apply baby oil", StatType.STAMINA, -3),
                    new Choice("Call Diddy for help", StatType.STAMINA, -2)
            );

            case 2 -> new Event(
                    "One will crash your game, the other hopefully wont...",
                    new Choice("Good", StatType.PERCEPTION, 2),
                    new Choice("Luck!", StatType.WISDOM, -1)
            );

            case 3 -> new Event(
                    "Red or Blue pill? .. or green (you wont find it...)",
                    new Choice("red", StatType.STAMINA, 3),
                    new Choice("blue", StatType.PERCEPTION, -1)
            );

            default -> new Event(
                    "Button or Button? That is the question...",
                    new Choice("I'm Button", StatType.WISDOM, 1),
                    new Choice("And im BUTTon <3", StatType.STRENGTH, -1)
            );
        };
    }

    public String applyChoice(List<Saints> party, Choice choice) {
        if (party.isEmpty()) {
            return "No saints were affected.";
        }

        Saints target = party.get(random.nextInt(party.size()));
        applyStat(target, choice.stat(), choice.amount());

        String changeText = choice.amount() > 0 ? "+" + choice.amount() : String.valueOf(choice.amount());

        return target.getName() + "'s " + choice.stat().label + " changed by " + changeText + ".";
    }

    private void applyStat(Saints saint, StatType stat, int amount) {
        switch (stat) {
            case STRENGTH -> saint.changeStrength(amount);
            case WISDOM -> saint.changeWisdom(amount);
            case DEXTERITY -> saint.changeDexterity(amount);
            case PERCEPTION -> saint.changePerception(amount);
            case STAMINA -> saint.changeMaxStamina(amount);
        }
    }
}