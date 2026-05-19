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
                    "Your party finds a cracked holy relic pulsing with unstable power.",
                    new Choice("Let the strongest saint touch it", StatType.STRENGTH, 2),
                    new Choice("Destroy it from a distance", StatType.WISDOM, -1)
            );

            case 1 -> new Event(
                    "A cursed fog surrounds the party and whispers old sins.",
                    new Choice("Push forward through the fog", StatType.STAMINA, -3),
                    new Choice("Stop and perform a cleansing rite", StatType.WISDOM, 2)
            );

            case 2 -> new Event(
                    "You discover strange footprints leading away from the path.",
                    new Choice("Track the creature carefully", StatType.PERCEPTION, 2),
                    new Choice("Rush after it", StatType.DEXTERITY, -1)
            );

            case 3 -> new Event(
                    "A wounded pilgrim offers a blessing in return for aid.",
                    new Choice("Help the pilgrim", StatType.STAMINA, 3),
                    new Choice("Ignore them and keep moving", StatType.PERCEPTION, -1)
            );

            default -> new Event(
                    "A demon shriek shakes the minds of the saints.",
                    new Choice("Stand firm and chant prayers", StatType.WISDOM, 1),
                    new Choice("Charge forward aggressively", StatType.STRENGTH, -1)
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