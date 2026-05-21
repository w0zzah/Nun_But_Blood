package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Saint service.
 */
public class SaintService {

    /** Active party of apostles going on the next crusade. Max 5.*/
    private final List<Saint> activeParty = new ArrayList<>();

    /** Reserve apostles not used in the upcoming crusade. Max 5.     */
    private final List<Saint> reserves = new ArrayList<>();

    /**
     * Gets active party.
     *
     * @return the active party
     */
    public List<Saint> getActiveParty() { return activeParty;}

    /**
     * Gets reserves.
     *
     * @return the reserves
     */
    public List<Saint> getReserves() { return reserves;}

    /**
     * Add saint boolean.
     *
     * @param saint the saint
     * @return the boolean
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
     * Activate saint boolean.
     *
     * @param saint the saint
     * @return the boolean
     */
    public boolean activateSaint(Saint saint) {
        if (activeParty.size() >= 5 || !reserves.contains(saint)) return false;
        reserves.remove(saint);
        activeParty.add(saint);
        return true;
    }

    /**
     * Bench saint boolean.
     *
     * @param saint the saint
     * @return the boolean
     */
    public boolean benchSaint(Saint saint) {
        if (reserves.size() >= 5 || !activeParty.contains(saint)) return false;
        activeParty.remove(saint);
        reserves.add(saint);
        return true;
    }

    /**
     * Retire saint boolean.
     *
     * @param saint the saint
     * @return the boolean
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
     * @return the int
     */
    public int calculateUpKeep() {
        return activeParty.stream().mapToInt(Saint::getUpKeep).sum();
    }


    /**
     * Refresh stamina.
     */
    public void refreshStamina() {
        activeParty.forEach(Saint::restoreStamina);
    }

    public boolean allSaintsExhausted() {
        List<Saint> allSaints = getOwnedSaints();
        if (allSaints.isEmpty()) {
            return true;
        }
        return allSaints.stream().allMatch(saints -> saints.getCurrentStamina() < 5);
    }
    /**
     * Increment crusade counts.
     */
    public void incrementCrusadeCounts() {
        activeParty.forEach(Saint::incrementCrusadeCount);
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return activeParty.isEmpty() && reserves.isEmpty();
    }

    /**
     * Gets owned saints.
     *
     * @return the owned saints
     */
    public List<Saint> getOwnedSaints() {
        List<Saint> allSaints = new ArrayList<>(activeParty);
        allSaints.addAll(reserves);
        return allSaints;
    }

    private void applyStat(Saint saint, StatType stat, int amount) {
        switch (stat) {
            case STRENGTH -> saint.changeStrength(amount);
            case WISDOM -> saint.changeWisdom(amount);
            case DEXTERITY -> saint.changeDexterity(amount);
            case PERCEPTION -> saint.changePerception(amount);
            case STAMINA -> saint.changeMaxStamina(amount);
        }
    }
}