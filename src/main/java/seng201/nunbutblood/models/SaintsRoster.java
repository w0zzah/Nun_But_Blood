package seng201.nunbutblood.models;
/*
    TODO:
      Attributes:
        + ENTRY: record (name, portraittKey, type, str, wis, dex)
        - allSaints: List <Entry>
      Methods:
        + getAllSaints() : List<Entry> {static}
        + pickRandom(int, Random): List<Entry> {static}
        + create(entry): Saints {static}
        + buldRoster(): ArrayList<Entry>
 */
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
public class SaintsRoster {

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
                        int baseStr, int baseWis, int baseDex) {}



    /** The complete named Apostle catalogue.
     * List called 'allSaints' containing all Saints from @buildRoster function
     *
     * Take's the Entry element type and creates a list with immutable size (prevent memory leaks) and mutable elements.
     *
     */
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
        // Return switch.
        //     Take's a list at index type and return's a
        return switch (entry.type()) {
            // create
            case "EXE" -> new Executioner(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex());
            case "ORA" -> new Oracle(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex());
            default    -> new Inquisitor(entry.name(), entry.portraitKey(),
                    entry.baseStr(), entry.baseWis(), entry.baseDex());
        };
    }

    // ── Roster definition ──────────────────────────────────────────────────────

    /**
     * Builds the complete named Apostle catalogue.
     * Stat totals shown are BEFORE the +5 class bonus is applied.
     *
     * @return mutable list of all entries
     */
    private static List<Entry> buildRoster() {
        List<Entry> r = new ArrayList<>();

        // ── COMMON tier (post-bonus total ~14–29) ──────────────────────────────
        // base totals 9–24 → after +5 class bonus: 14–29
        r.add(new Entry("Sister Beatrice",  "sister-beatrice",  "EXE",  8,  5,  5));  // total 23
        return r;
    }
}