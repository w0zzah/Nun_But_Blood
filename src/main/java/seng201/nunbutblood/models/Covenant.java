package seng201.nunbutblood.models;

import seng201.nunbutblood.characters.Apostles;
import seng201.nunbutblood.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*

Last Edited 1521 12/04/26 @author pgi33 - "Covenant Class --- Main hub to serve as Players 'home-base'"

        Covenant Class --- Management of Party and Resources
//        TODO:
          Attributes:
            ~ name (String) : Name of the players covenant
            ~ faith (int) : Currency (Gold equivalent)
            ~ totalCrusades (int) : Progress tracker
            ~ currentCrusade (int) : Goal (5-15)
            ~ activeParty (ArrayList) : Max 5
            ~ reserves (ArrayList) : Max 5
            ~ inventory (ArrayList) : Items/Objects
            - totalFaithEarned : int
            - score: int
          Methods:
            GETTERS
            + getName(): String
            + getFaith(): int
            +getTotalCrusades(): int
            + getCurrentCrusade(): int
            + getRemainingCrusades(): int
            + getActiveParty(): LIst
            + getReserves(): LIst
            + getInventory(): List
            + getTotalFaithEarned(): int
            + getScore(): int
            FUNCTIONS
            + addApostle(): bool
            + benchApostle(): bool
            + activateApostle(): bool
            + retireApostle(): bool
            + addFaith(): int
            + spendFaith(): int
            + payActiveParty(): int
            + addItem(Item item):
            + removeItem(Item item): bool
            + addScore(int points):
            + advanceCrusade():
            + isGameComplete(): bool
            + hasLost(): bool

 */
public class Covenant {

    private final String name;
    private int faith;
    private final int totalCrusades;
    private int currentCrusade;
    private int totalFaithEarned;
    private int score;

    private final ArrayList<Apostles> activeParty;
    private final ArrayList<Apostles> reserves;
    private final ArrayList<Item> inventory;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Covenant(String name, int totalCrusades, int startingFaith, List<Apostles> startingParty) {
        this.name             = name;
        this.totalCrusades    = totalCrusades;
        this.faith            = startingFaith;
        this.currentCrusade   = 0;
        this.totalFaithEarned = startingFaith;
        this.score            = 0;
        this.activeParty      = new ArrayList<>(startingParty);
        this.reserves         = new ArrayList<>();
        this.inventory        = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getName()           { return name; }
    public int getFaith()             { return faith; }
    public int getTotalCrusades()     { return totalCrusades; }
    public int getCurrentCrusade()    { return currentCrusade; }
    public int getRemainingCrusades() { return totalCrusades - currentCrusade; }
    public int getTotalFaithEarned()  { return totalFaithEarned; }
    public int getScore()             { return score; }

    public List<Apostles> getActiveParty() { return Collections.unmodifiableList(activeParty); }
    public List<Apostles> getReserves()    { return Collections.unmodifiableList(reserves); }
    public List<Item> getInventory()       { return Collections.unmodifiableList(inventory); }

    // ── Party Management ─────────────────────────────────────────────────────

    public boolean addApostle(Apostles apostle) {
        if (reserves.size() >= 5) return false;
        reserves.add(apostle);
        return true;
    }

    public boolean activateApostle(Apostles apostle) {
        if (activeParty.size() >= 5) return false;
        if (!reserves.contains(apostle)) return false;
        reserves.remove(apostle);
        activeParty.add(apostle);
        return true;
    }

    public boolean benchApostle(Apostles apostle) {
        if (activeParty.size() <= 1) return false;
        if (reserves.size() >= 5)    return false;
        if (!activeParty.contains(apostle)) return false;
        activeParty.remove(apostle);
        reserves.add(apostle);
        return true;
    }

    public boolean retireApostle(Apostles apostle) {
        int total = activeParty.size() + reserves.size();
        if (total <= 1) return false;
        if (activeParty.remove(apostle)) return true;
        if (reserves.remove(apostle))    return true;
        return false;
    }

    // ── Faith (Currency) ─────────────────────────────────────────────────────

    public int addFaith(int amount) {
        if (amount < 0) return faith;
        faith += amount;
        totalFaithEarned += amount;
        return faith;
    }

    public boolean spendFaith(int amount) {
        if (amount > faith) return false;
        faith -= amount;
        return true;
    }

    public int payActiveParty() {
        int total = activeParty.stream()
                .mapToInt(Apostles::getUpKeep)
                .sum();
        faith -= total;
        return total;
    }

    // ── Items ─────────────────────────────────────────────────────────────────

    public void addItem(Item item)       { inventory.add(item); }
    public boolean removeItem(Item item) { return inventory.remove(item); }

    // ── Score & Progression ───────────────────────────────────────────────────

    public void addScore(int points) { score += points; }

    public void advanceCrusade() {
        currentCrusade++;
        activeParty.forEach(Apostles::incrementCrusadeCount);
    }

    // ── Game State ────────────────────────────────────────────────────────────

    public boolean isGameComplete() {
        return currentCrusade >= totalCrusades;
    }

    public boolean hasLost() {
        boolean noApostles = activeParty.isEmpty() && reserves.isEmpty();
        return noApostles && faith <= 0;
    }
}