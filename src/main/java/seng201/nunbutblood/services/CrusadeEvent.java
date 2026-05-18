package seng201.nunbutblood.services;

import seng201.nunbutblood.models.Saints;
import seng201.nunbutblood.models.StatType;

public record CrusadeEvent(
    Saints target,
    StatType stat,
    int change,
    String description){}
