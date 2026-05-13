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
public class DemonsRoster {

    public record Entry(String name, String portraitKey, int difficultyRating, String weakness, int faithReward){}

    /** The complete named Apostle catalogue.
     * List called 'allSaints' containing all Saints from @buildRoster function
     *
     * Take's the Entry element type and creates a list with immutable size (prevent memory leaks) and mutable elements.
     *
     */
    private static final List<Entry> allDemons = buildRoster();

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
        List<Entry> shuffled = new ArrayList<>(allDemons);
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
        return Collections.unmodifiableList(allDemons);
    }

    /**
     * Builds the complete named Apostle catalogue.
     * Stat totals shown are BEFORE the +5 class bonus is applied.
     *
     * @return mutable list of all entries
     */
    private static List<Entry> buildRoster() {
        List<Entry> r = new ArrayList<>();
        r.add(new Entry("Saint Germain",  "Germain.png",  15,  "wisdom",  5));
        r.add(new Entry("Saint Arsenia",  "t4.png",  13,  "strength",  5));
        r.add(new Entry("Saint Winifred",  "Winifred.png",  18,  "dexterity",  8));
        r.add(new Entry("Saint Hilda",  "Hilda.png",  10,  "strength",  15));
        r.add(new Entry("Saint Anastasius",  "Anastasius.png",  12,  "strength",  15));
        r.add(new Entry("Saint Chrysostomi",  "t1.png",  14,  "strength",  14));
        return r;
    }
}