package seng201.nunbutblood.models;

import seng201.nunbutblood.characters.Apostles;

/*

Last Edited 0405 02/04/26 @author cwi184 - "Item Class implementation"

        Item Class --- Implements Purchasable
        TODO:
          Attributes:
            + ItemType: enum
            - itemName: string
            - type: ItemType
            - statAffected: String
            - bonusValue: int
            - cost: int
            - itemDescription: String
          Methods:
            + applyEffect(Apostle apostle)
            + getItemName(): ItemType
            + getStatAffected(): String
            + getBonusValue(): int
            + getCost(): int <<purchasble>>
            + getDecription: String <<purchasable>>
*/
public class Item {

    public enum ItemType {
        COMMON, RARE, EPIC, LEGENDARY
    }
    private String itemName;
    private ItemType type;
    private String statAffected;
    private int bonusValue;
    private int cost;
    private String itemDescription;

    public Item(String itemName, ItemType type, String statAffected, int bonusValue, int cost, String itemDescription){
        this.itemName = itemName;
        this.type = type;
        this.statAffected = statAffected;
        this.bonusValue = bonusValue;
        this.cost = cost;
        this.itemDescription = itemDescription;
    }
    public void applyEffect(Apostles apostles){}

    public String getItemName(){ return itemName; }

    public String getStatAffected(){ return statAffected; }

    public int getBonusValue(){ return bonusValue; }

    public int getCost(){return cost; }

    public String getItemDescription(){ return itemDescription; }
}
