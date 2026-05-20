package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Demon;
import seng201.nunbutblood.models.StatType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemonService {

    private int crusadeCheck = -1;
    private List<Demon> currentOptions = new ArrayList<>();

    /** Generates 3 random demons based on the current crusade difficulty. */
    public void generateNewOptions(int currentCrusade, String difficulty) {

        crusadeCheck = currentCrusade;
        currentOptions.clear();

        // Set difficulty of demons, If its hard mode +3
        int difficultyBonus = currentCrusade;
        if (difficulty.equals("Hard")){ difficultyBonus += 3;}

        List<Demon> masterPool = new ArrayList<>();

        // Logic to create 3 demons that get harder as currentCrusade increases
        masterPool.add(new Demon("Zagareth", "firehaunt.gif", 10 + difficultyBonus, StatType.STRENGTH, 300));
        masterPool.add(new Demon("Shadow", "demon2.gif", 12 + difficultyBonus, StatType.WISDOM, 450));
        masterPool.add(new Demon("Centaurian", "centaur.gif", 14 + difficultyBonus, StatType.DEXTERITY, 600));
        masterPool.add(new Demon("Ecplise", "devil.gif", 16 + difficultyBonus, StatType.STRENGTH, 800));
        masterPool.add(new Demon("Zoltrak", "demon.gif", 18 + difficultyBonus, StatType.WISDOM, 1000));
        masterPool.add(new Demon("Wildfire", "colourfire.gif", 20 + difficultyBonus, StatType.DEXTERITY, 1250));

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