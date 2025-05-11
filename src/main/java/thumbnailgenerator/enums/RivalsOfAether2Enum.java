package thumbnailgenerator.enums;

import lombok.Getter;
import thumbnailgenerator.enums.interfaces.CharacterEnum;

@Getter
public enum RivalsOfAether2Enum implements CharacterEnum {

    CLAIREN("Clairen", "clairen", 2503, 1),
    FLEET("Fleet", "fleet", 2498, 1),
    FORSBURN("Forsburn", "forsburn", 2599, 1),
    KRAGG("Kragg", "kragg", 2504, 1),
    LOXODONT("Loxodont", "loxodont", 2505, 1),
    MAYPUL("Maypul", "maypul", 2502, 1),
    ORCANE("Orcane", "orcane", 2597, 1),
    RANNO("Ranno", "ranno", 2501, 1),
    WRASTOR("Wrastor", "wrastor", 2500, 1),
    ZETTERBURN("Zetterburn", "zetterburn", 2499, 1),
    ETALUS("Etalus", "etalus", 2615, 1),
    OLYMPIA("Olympia", "olympia", 2619, 1);

    private final String name;
    private final String code;
    private final int startGGId;
    private final int altQuantity;

    RivalsOfAether2Enum(String name, String code, int startGGId, int altQuantity) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
        this.altQuantity = altQuantity;
    }
}
