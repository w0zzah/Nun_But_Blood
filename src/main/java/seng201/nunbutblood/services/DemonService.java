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
        currentOptions.add(new Demon("Imp", "t1.png", 12 + currentCrusade, "strength", 100));
        currentOptions.add(new Demon("Shadow", "1.png", 13 + currentCrusade, "wisdom", 100));
        currentOptions.add(new Demon("Gargoyle", "2.png", 12 + currentCrusade, "dexterity", 100));
        currentOptions.add(new Demon("1", "3.png", 11 + currentCrusade, "strength", 100));
        currentOptions.add(new Demon("2", "1.png", 16 + currentCrusade, "wisdom", 100));
        currentOptions.add(new Demon("3", "4.png", 8 + currentCrusade, "dexterity", 100));
    }

    public List<Demon> getCurrentOptions() {
        return currentOptions;
    }
}