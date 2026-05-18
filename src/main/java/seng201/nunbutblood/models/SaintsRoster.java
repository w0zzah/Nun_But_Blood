package seng201.nunbutblood.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * To serve as a List containing all information for the group of Saints.
 *
 * Every Apostle has their own strength, wisdom and dex, which when summed, display the rarity @Rarity
 *
 * There rarity of each doesn't determine their inherent usefulness however as using lower stat's will
 * be rewarded with more money in the early game to encourage different playstyles.
 *
 * The different Class's get a bonus to their determined stat (+3)
 *
 *
 */
public final class SaintsRoster {

    /**
     * record documentation for reference see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Record.html">here</a>
     *  Record containing the format for the Apostle's. The list is immutable so it is parsed in as the element type
     *  in a List (to be used similar to interface.)
     *
     * @param name        unique display name
     * @param portraitKey CSS key matching {@code .portrait-<key>} in style.css
     * @param type        class type: "EXE", "ORA", or "INQ"
     * @param baseStr     base Strength before class bonus
     * @param baseWis     base Wisdom before class bonus
     * @param baseDex     base Dexterity before class bonus
     */
    public record Entry(String name, String portraitKey, String type,
                        int baseStr, int baseWis, int baseDex, int stamina) {}
    
    private static final List<Entry> allSaints = buildRoster();

    /**
     * Returns a shuffled subset of {@code count} roster entries, drawn without
     * replacement. If {@code count} exceeds the roster size, all entries are returned.
     *
     * @param count  number of entries to return
     * @param random shared Random instance from the caller
     * @return shuffled list of roster entries
     */
    public static List<Entry> pickRandom(int count, Random random) {
        // Set's List 'shuffled' = to a temporary ArrayList that contains all Saints
        List<Entry> shuffled = new ArrayList<>(allSaints);
        // Collections.shuffle shuffles element's in an ArrayList (Element, Method)
        Collections.shuffle(shuffled, random);
        // Returns the new list with the target count or whatever is available
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    /**
     * Returns all roster entries for the setup screen's fixed starting pool.
     *
     * @return unmodifiable view of the full roster
     */
    public static List<Entry> getAllSaints() {
        return Collections.unmodifiableList(allSaints);
    }

    /**
     * Instantiates an Apostle from a roster entry.
     *
     * @param entry the roster entry to instantiate
     * @return a new Apostle object of the appropriate subclass
     */
    public static Saints create(Entry entry) {
        return switch (entry.type()) {
            case "EXE" -> new Executioner(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex(), entry.stamina);
            case "ORA" -> new Oracle(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex(), entry.stamina);
            default    -> new Inquisitor(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex(), entry.stamina);
        };
    }

    /**
     * Builds all possible Saints with unique identifiers. <br>
     * (6 Common, 6 Rare, 4 Epic, 2 Legendary)
     * <br>
     *
     * @return mutable list of all entries
     */
    private static List<Entry> buildRoster() {
        List<Entry> saintRecord = new ArrayList<>();


        // Common (Total stats under 30)
        saintRecord.add(new Entry("Saint Germain", "Man.png", "INQ", 6, 5, 7, 20));
        saintRecord.add(new Entry("Saint Arsenia", "Arsenia.png", "INQ", 4, 7, 6, 20));
        saintRecord.add(new Entry("Saint Winifred", "Winifred.png", "EXE", 8, 6, 5, 20));
        saintRecord.add(new Entry("Saint Hilda", "Hilda.png", "EXE", 9, 4, 7, 20));
        saintRecord.add(new Entry("Saint Anastasius", "Anastasius.png", "ORA", 5, 8, 9, 20));
        saintRecord.add(new Entry("Saint Chrysostomi", "Chrysostomi.png", "ORA", 7, 9, 10, 20));

        // Rare (Total stats 30–39)
        saintRecord.add(new Entry("Saint Lucian", "Lucian.png", "EXE", 14, 8, 10, 25));
        saintRecord.add(new Entry("Saint Selene", "Selene.png", "INQ", 9, 11, 13, 18));
        saintRecord.add(new Entry("Saint Helena", "Helena.png", "ORA", 10, 12, 11, 20));
        saintRecord.add(new Entry("Saint George", "George.png", "EXE", 16, 10, 12, 25));
        saintRecord.add(new Entry("Saint Joan of Arc", "JoanOfArc.png", "INQ", 11, 13, 14, 25));
        saintRecord.add(new Entry("Saint Michael", "Michael.png", "EXE", 17, 12, 10, 30));

        // Epic (Total stats 40–50)
        saintRecord.add(new Entry("Saint Maurice", "Maurice.png", "EXE", 16, 13, 12, 25));
        saintRecord.add(new Entry("Saint Martin of Tours", "MartinOfTours.png", "INQ", 14, 15, 13, 25));
        saintRecord.add(new Entry("Saint Theodora", "Theodora.png", "ORA", 12, 17, 14, 22));
        saintRecord.add(new Entry("Saint Longinus", "Longinus.png", "EXE", 18, 11, 15, 25));

        // Legendary (Total stats over 50)
        saintRecord.add(new Entry("Saint Vladimir", "Vladimir.png", "INQ", 19, 16, 17, 28));
        saintRecord.add(new Entry("Saint David of Wales", "DavidOfWales.png", "ORA", 15, 18, 19, 22));
        return saintRecord;
    }
}