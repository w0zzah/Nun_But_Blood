package seng201.nunbutblood.models;

/**
 * The interface Purchasable.
 */
/*

Last Edited 0412 02/04/26 @author cwi184 - "Interface for Market interactions"

        Purchasable Interface --- Shared by Items and Apostles
          Methods:
            ~ getCost() : Returns price in Faith
            ~ getDescription() : Returns stats/info for Market display
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
