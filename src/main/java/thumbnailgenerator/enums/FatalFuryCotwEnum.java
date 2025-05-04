package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum FatalFuryCotwEnum {

    ROCK("Rock Howard", "rock", 2692),
    TERRY("Terry Bogard", "terry", 2694),
    JENET("B. Jenet", "jenet", 2680),
    MARCO("Marco Rodrigues", "marco", 2690),
    PREECHA("Preecha", "preecha", 2691),
    HOTARU("Hotaru Futaba", "hotaru", 2685),
    VOX("Vox Reaper", "vox", 2696),
    TIZOC("Tizoc", "tizoc", 2695),
    KEVIN("Kevin Rian", "kevin", 2687),
    BILLY("Billy Kane", "billy", 2681),
    MAI("Mai Shiranui", "mai", 2689),
    KIM("Kim Dong Hwan", "kim", 2688),
    GATO("Gato", "gato", 2683),
    KAIN("Kain R. Heinlein", "kain", 2686),
    CR7("Cristiano Ronaldo", "cr7", 2682),
    GANACCI("Salvatore Ganacci", "ganacci", 2693),
    HOKUTO("Hokuto Maru", "hokuto", 2684);

    private final String name;
    private final String code;
    private final int startGGId;

    FatalFuryCotwEnum(String name, String code, int startGGId) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
    }
}
