package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saint;
import seng201.nunbutblood.models.StatType;

/**
 * The type Crusade event.
 */
public record CrusadeEvent(
    Saint target,
    StatType stat,
    int change,
    String description){}
