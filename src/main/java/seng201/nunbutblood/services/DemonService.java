package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemonService {
    private List<Demon> currentOptions = new ArrayList<>();

    /** Generates 3 random demons based on the current crusade difficulty. */
    public void generateNewOptions(int currentCrusade, String difficulty) {
        currentOptions.clear();

        // Set difficulty of demons, If its hard mode +3
        int difficultyBonus = currentCrusade;
        if (difficulty.equals("Hard")){ difficultyBonus += 3;}

        List<Demon> masterPool = new ArrayList<>();

        // Logic to create 3 demons that get harder as currentCrusade increases
        masterPool.add(new Demon("Zagareth", "firehaunt.gif", 10 + difficultyBonus, "strength", 300));
        masterPool.add(new Demon("Shadow", "demon2.gif", 12 + difficultyBonus, "wisdom", 450));
        masterPool.add(new Demon("Centaurian", "centaur.gif", 14 + difficultyBonus, "strength", 600));
        masterPool.add(new Demon("Ecplise", "devil.gif", 16 + difficultyBonus, "dexterity", 800));
        masterPool.add(new Demon("Zoltrak", "demon.gif", 18 + difficultyBonus, "wisdom", 1000));
        masterPool.add(new Demon("Wildfire", "colourfire.gif", 20 + difficultyBonus, "wisdom", 1250));

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