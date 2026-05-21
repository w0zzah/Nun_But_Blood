package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing the roster of Saints.
 * Handles ops involving the active party, reserves, party transitions,
 * and all of the stat or status updates.
 */
public class SaintService {

    /** Active party of saints going on the next crusade. Max 5.*/
    private final List<Saint> activeParty = new ArrayList<>();

    /** Reserve saints not used in the upcoming crusade. Max 5.     */
    private final List<Saint> reserves = new ArrayList<>();

    /**
     * Gets the current list of active party members.
     *
     * @return a list containing the active Saints
     */
    public List<Saint> getActiveParty() { return activeParty;}

    /**
     * Gets the current list of reserve party members.
     *
     * @return a list containing the reserve Saints
     */
    public List<Saint> getReserves() { return reserves;}

    /**
     * Adds a new Saint to the roster
     *
     * Try to put in the activeParty first, and if not enough space, check reserves.
     * Then if not enough space in either, return false
     *
     * @param saint the Saint to be added to the roster
     * @return true if the Saint was successfully added to either the active party
     *         or reserves, false if both groups are completely full
     */
    public boolean addSaint(Saint saint) {
        if (activeParty.size() < 5) {
            activeParty.add(saint);
            return true;
        }
        else if (reserves.size() < 5) {
            reserves.add(saint);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Moves a Saint from the reserves list into the active party, if there is space
     *
     * @param saint the Saint to activate
     * @return true if the transferred, else false
     */
    public boolean activateSaint(Saint saint) {
        if (activeParty.size() >= 5 || !reserves.contains(saint)) return false;
        reserves.remove(saint);
        activeParty.add(saint);
        return true;
    }

    /**
     * Moves a Saint from the active party into the reserves list, if there is space
     *
     * @param saint the Saint to move to reserves
     * @return true if the transferred, else false
     */
    public boolean benchSaint(Saint saint) {
        if (reserves.size() >= 5 || !activeParty.contains(saint)) return false;
        activeParty.remove(saint);
        reserves.add(saint);
        return true;
    }

    /**
     * Permanently removes a Saint from the roster (either active or reserve).
     * To prevent a completely empty roster, will fail if the total number
     * of owned Saints is 1 or fewer.
     *
     * @param saint the Saint to retire from service
     * @return true if the Saint was successfully removed from the roster, else false
     */
    public boolean retireSaint(Saint saint) {
        if ((activeParty.size() + reserves.size() <= 1)) return false;
        return activeParty.remove(saint) || reserves.remove(saint);
    }

    /**
     * <b>Stream -></b>Stream  Return's the Active party one by one so that we can talk to each of them (sequential list)<br>
     * <b>mapToInt -></b>  Return Int values of the object (faster than objects for computing)<br>
     * <b>Saint::getUpKeep -></b>  Pick's out each saints upKeep<br>
     * <b>sum() -> </b> add's everything up <br>
     * <br>
     * <p>
     * This serves as a conveyer belt system where we line up all our Saint and slowly
     * pick out the ones we want, and the things we need, then what we want to do with it.
     * </p>
     * <br>
     * <br>
     * .stream() <b>(Line them up)</b>-> mapToInt(Saint::getUpKeep) <b>Pickout each Saint UpKeep) -</b>(> .sum() <b>(Add them all together)</b>
     *
     * @return the total upkeep cost of all active party members combined
     */
    public int calculateUpKeep() {
        return activeParty.stream().mapToInt(Saint::getUpKeep).sum();
    }


    /**
     * Refreshes and restores the sanity of all active party members.
     */
    public void refreshSanity() {
        activeParty.forEach(Saint::restoreSanity);
    }

    /**
     * Checks whether all currently owned Saints (both active and reserves) are completely exhausted.
     * A Saint is considered exhausted if their current sanity falls below 5.
     *
     * @return true if the entire roster is empty or if every owned Saint has less than 5 sanity,
     *         false if there is at least one Saint with 5 or more sanity remaining
     */
    public boolean allSaintsExhausted() {
        List<Saint> allSaints = getOwnedSaints();
        if (allSaints.isEmpty()) {
            return true;
        }
        return allSaints.stream().allMatch(saints -> saints.getCurrentSanity() < 5);
    }

    /**
     * Increments the total crusade completion count for all active party members.
     */
    public void incrementCrusadeCounts() {
        activeParty.forEach(Saint::incrementCrusadeCount);
    }

    /**
     * Checks if the service has no Saints allocated anywhere.
     *
     * @return true if both active party and reserves are completely empty, false otherwise
     */
    public boolean isEmpty() {
        return activeParty.isEmpty() && reserves.isEmpty();
    }

    /**
     * Aggregates and retrieves all Saints currently managed by the service.
     * Combines both active party members and reserve members into a unified list.
     *
     * @return a new list containing all active and reserve Saints
     */
    public List<Saint> getOwnedSaints() {
        List<Saint> allSaints = new ArrayList<>(activeParty);
        allSaints.addAll(reserves);
        return allSaints;
    }

    /**
     * Internal helper method to modify a specific statistic value of a given Saint.
     *
     * @param saint  the Saint whose stat is to be adjusted
     * @param stat   the type of statistic being targeted
     * @param amount the numerical amount to apply (positive or negative)
     */
    private void applyStat(Saint saint, StatType stat, int amount) {
        Saint.applyStat(saint, stat, amount);
    }
}