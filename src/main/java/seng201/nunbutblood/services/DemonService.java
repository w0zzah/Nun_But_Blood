package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import java.util.ArrayList;
import java.util.List;

public class DemonService {
    private List<Demon> currentOptions = new ArrayList<>();

    /** Generates 3 random demons based on the current crusade difficulty. */
    public void generateNewOptions(int currentCrusade) {
        currentOptions.clear();
        // Logic to create 3 demons that get harder as currentCrusade increases
        currentOptions.add(new Demon("Imp", "/images/imp.png", 6 + currentCrusade, "strength", 100));
        currentOptions.add(new Demon("Shadow", "/images/imp.png", 5 + currentCrusade, "wisdom", 100));
        currentOptions.add(new Demon("Gargoyle", "/images/imp.png", 6 + currentCrusade, "dexterity", 100));
    }

    public List<Demon> getCurrentOptions() {
        return currentOptions;
    }
}