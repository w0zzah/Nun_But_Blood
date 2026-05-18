package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemonService {
    private List<Demon> currentOptions = new ArrayList<>();

    /** Generates 3 random demons based on the current crusade difficulty. */
    public void generateNewOptions(int currentCrusade) {
        currentOptions.clear();

        List<Demon> masterPool = new ArrayList<>();
        // Logic to create 3 demons that get harder as currentCrusade increases
        masterPool.add(new Demon("Centaurian", "centaur.gif", 12 + currentCrusade, "strength", 50));
        masterPool.add(new Demon("Shadow", "demon2.gif", 13 + currentCrusade, "wisdom", 50));
        masterPool.add(new Demon("Ecplise", "devil.gif", 12 + currentCrusade, "dexterity", 50));
        masterPool.add(new Demon("Zagareth", "firehaunt.gif", 12 + currentCrusade, "strength", 50));
        masterPool.add(new Demon("Wildfire", "colourfire.gif", 13 + currentCrusade, "wisdom", 50));

        Collections.shuffle(masterPool);

        int limit = Math.min(3, masterPool.size());
        for (int i = 0; i < limit ; i++) {
            currentOptions.add(masterPool.get(i));
        }
    }

    public List<Demon> getCurrentOptions() {
        return currentOptions;
    }
}