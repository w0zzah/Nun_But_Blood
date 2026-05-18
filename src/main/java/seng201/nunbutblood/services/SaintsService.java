package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Item;
import seng201.nunbutblood.models.Saints;

import java.util.ArrayList;
import java.util.List;

public class SaintsService {

    /** Active party of apostles going on the next crusade. Max 5.*/
    private final ArrayList<Saints> activeParty = new ArrayList<>();

    /** Reserve apostles not used in the upcoming crusade. Max 5.     */
    private final ArrayList<Saints> reserves = new ArrayList<>();

    public List<Saints> getActiveParty() { return activeParty;}
    public List<Saints> getReserves() { return reserves;}

    public boolean addSaints(Saints saints) {
        if (activeParty.size() < 5) {
            activeParty.add(saints);
            return true;
        }
        else if (reserves.size() < 5) {
            reserves.add(saints);
            return true;
        } else {
            return false;
        }
    }

    public boolean activateSaint(Saints saint) {
        if (activeParty.size() >= 5 || !reserves.contains(saint)) return false;
        reserves.remove(saint);
        activeParty.add(saint);
        return true;
    }

    public boolean benchSaint(Saints saint) {
        if (reserves.size() >= 5 || !activeParty.contains(saint)) return false;
        activeParty.remove(saint);
        reserves.add(saint);
        return true;
    }

    public boolean retireSaint(Saints saint) {
        if ((activeParty.size() + reserves.size() <= 1)) return false;
        return activeParty.remove(saint) || reserves.remove(saint);
    }

    /**
     * <b>Stream -></b>Stream  Return's the Active party one by one so that we can talk to each of them (sequential list)<br>
     * <b>mapToInt -></b>  Return Int values of the object (faster than objects for computing)<br>
     * <b>Saints::getUpKeep -></b>  Pick's out each saints upKeep<br>
     * <b>sum() -> </b> add's everything up <br>
     * <br>
     * <p>
     * This serves as a conveyer belt system where we line up all our Saints and slowly
     * pick out the ones we want, and the things we need, then what we want to do with it.
     * </p>
     * <br>
     * <br>
     * .stream() <b>(Line them up)</b>-> mapToInt(Saints::getUpKeep) <b>Pickout each Saints UpKeep) -</b>(> .sum() <b>(Add them all together)</b>
     * */
    public int calculateUpKeep() {
        return activeParty.stream().mapToInt(Saints::getUpKeep).sum();
    }


    public void refreshStamina() {
        activeParty.forEach(Saints::restoreStamina);
    }

    public void incrementCrusadeCounts() {
        activeParty.forEach(Saints::incrementCrusadeCount);
    }

    public boolean isEmpty() {
        return activeParty.isEmpty() && reserves.isEmpty();
    }

    public List<Saints> getOwnedSaints() {
        List<Saints> allSaints = new ArrayList<>(activeParty);
        allSaints.addAll(reserves);
        return allSaints;
    }

}