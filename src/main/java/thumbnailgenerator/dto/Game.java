package thumbnailgenerator.dto;

import lombok.Getter;

@Getter
public enum Game {
    SSBU("ssbu","Smash Ultimate", 1386),
    SSBM("ssbm","Smash Melee", 1),
    SSB64R("ssb64r","Smash 64 Remix", 4),
    ROA2("roa2","Rivals of Aether 2", 53945),
    SF6("sf6", "Street Fighter 6", 43868),
    TEKKEN8("tekken8", "Tekken 8", 49783),
    GGST("ggst", "Guilty Gear: Strive", 33945),
    FFCOTW("ffcotw", "Fatal Fury CotW", 73221),
    GBFVR("gbfvr", "Granblue Fantasy Versus: Rising", 48548);

    private String code;
    private String name;
    private int startGGId;

    Game(String code, String name, int startGGId) {
        this.code = code;
        this.name = name;
        this.startGGId = startGGId;
    }
}
