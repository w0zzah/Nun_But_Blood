package seng201.nunbutblood.models;

/**
 * Common contract for any assets that can be acquired through market interactions.
 * Implemented by both item commodities and recruit units.
 */
public interface Purchasable {

    /**
     * Return the Faith cost of Apostle's and Item's
     *
     * @return cost of target
     */
    int getCost();


    /**
     * Return's the description of Apostle's and Item's
     *
     * @return description of target
     */
    String getDescription();

}