package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.StatType;

/**
 * The type Crusade event.
 */
public record CrusadeEvent(
    Saints target,
    StatType stat,
    int change,
    String description){}
