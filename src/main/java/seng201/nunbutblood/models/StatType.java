package seng201.nunbutblood.models;

public enum StatType {
    STRENGTH("strength", "STR"),
    WISDOM("wisdom", "WIS"),
    DEXTERITY("dexterity", "DEX"),
    STAMINA("stamina", "STA"),
    PERCEPTION("perception", "PER");

    public final String key;    // used in applyEffect / getAttributeValue
    public final String label;  // used in UI display
    
    StatType(String key, String label) {
        this.key = key;
        this.label = label;
    }
}
