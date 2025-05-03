package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum FatalFuryCotwEnum {

    ROCK("Rock Howard", "rock", 1),
    TERRY("Terry Bogard", "terry", 1),
    JENET("B. Jenet", "jenet", 1),
    MARCO("Marco Rodrigues", "marco", 1),
    PREECHA("Preecha", "preecha", 1),
    HOTARU("Hotaru Futaba", "hotaru", 1),
    VOX("Vox Reaper", "vox", 1),
    TIZOC("Tizoc", "tizoc", 1),
    KEVIN("Kevin Rian", "kevin", 1),
    BILLY("Billy Kane", "billy", 1),
    MAI("Mai Shiranui", "mai", 1),
    KIM("Kim Dong Hwan", "kim", 1),
    GATO("Gato", "gato", 1),
    KAIN("Kain R. Heinlein", "kain", 1),
    CR7("Cristiano Ronaldo", "cr7", 1),
    GANACCI("Salvatore Ganacci", "ganacci", 1),
    HOKUTO("Hokuto Maru", "hokuto", 1);

    private final String name;
    private final String code;
    private final int startGGId;


    FatalFuryCotwEnum(String name, String code, int startGGId) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
    }
}
