package seng201.nunbutblood.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * To serve as a List containing all information for the group of Saint.
 * <p>
 * Every Saint has their own strength, wisdom and dex, which when summed, display the rarity @Rarity
 * <p>
 * There rarity of each doesn't determine their inherent usefulness however as using lower stat's will
 * be rewarded with more money in the early game to encourage different playstyles.
 * <p>
 * The different Class's get a bonus to their determined stat (+3)
 *
 *
 */
public final class SaintRoster {

    private Random random;
    /**
     * record documentation for reference see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Record.html">here</a>
     * Record containing the format for the Saint's. The list is immutable so it is parsed in as the element type
     * in a List (to be used similar to interface.)
     *
     * @param name        unique display name
     * @param portraitKey CSS key matching {@code .portrait-<key>} in style.css
     * @param type        class type: "EXE", "ORA", or "INQ"
     * @param baseStr     base Strength before class bonus
     * @param baseWis     base Wisdom before class bonus
     * @param baseDex     base Dexterity before class bonus
     * @param sanity      base Sanity total
     */
    public record Entry(String name, String portraitKey, String type,
                        int baseStr, int baseWis, int baseDex, int sanity) {}

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
        // Set's List 'shuffled' = to a temporary ArrayList that contains all Saint
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
     * Instantiates an Saint from a roster entry.
     *
     * @param entry the roster entry to instantiate
     * @return a new Saint object of the appropriate subclass
     */
    public static Saint create(Entry entry) {
        return switch (entry.type()) {
            case "EXE" -> new Executioner(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex(), entry.sanity);
            case "ORA" -> new Oracle(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex(), entry.sanity);
            default    -> new Inquisitor(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex(), entry.sanity);
        };
    }

    /**
     * Builds all possible Saint with unique identifiers. <br>
     * (6 Common, 6 Rare, 4 Epic, 2 Legendary)
     * <br>
     *
     * @return mutable list of all entries
     */
    private static List<Entry> buildRoster() {
        List<Entry> saintRecord = new ArrayList<>();


        // Common (stat total < 20)
        saintRecord.add(new Entry("Luxana", "Luxana.png", "ORA", 4, 9, 5, 20));
        saintRecord.add(new Entry("Chia", "Chia.png", "INQ", 3, 5, 9, 20));
        saintRecord.add(new Entry("Mack", "Mack.png", "EXE", 9, 4, 5, 20));
        saintRecord.add(new Entry("Torvald", "Torvald.png", "EXE", 10, 3, 6, 20));
        saintRecord.add(new Entry("Unohana", "Unohana.png", "ORA", 4, 10, 5, 20));
        saintRecord.add(new Entry("Tsundere", "Tsundere.png", "INQ", 5, 6, 8, 20));

        // Rare
        saintRecord.add(new Entry("Issac", "Issac.png", "INQ", 8, 8, 16, 20));
        saintRecord.add(new Entry("Rukia", "Rukia.png", "EXE", 15, 6, 9, 20));
        saintRecord.add(new Entry("Krac", "Krac.png", "ORA", 8, 14, 10, 20));
        saintRecord.add(new Entry("Kai'Sa", "KaiSa.png", "ORA", 11, 16, 12, 20));

        // Epic
        saintRecord.add(new Entry("Yamato", "Yamato.png", "EXE", 17, 11, 12, 20));
        saintRecord.add(new Entry("Lego'ne", "Legone.png", "INQ", 11, 12, 16, 20));
        saintRecord.add(new Entry("Jarvan", "Jarvan.png", "ORA", 10, 17, 13, 20));
        saintRecord.add(new Entry("Argo'Sax", "ArgoSax.png", "EXE", 18, 10, 14, 20));

        // Legendary
        saintRecord.add(new Entry("Flamme'", "Flamme.png", "ORA", 17, 20, 18, 20));
        saintRecord.add(new Entry("Crystalis", "Crystalis.png", "INQ", 18, 19, 20, 20));
        saintRecord.add(new Entry("Crystolis", "Crystolis.png", "EXE", 20, 18, 19, 20));
        saintRecord.add(new Entry("Mother", "Mother.png", "Ora", 20, 20, 20, 20));

        return saintRecord;
    }
}