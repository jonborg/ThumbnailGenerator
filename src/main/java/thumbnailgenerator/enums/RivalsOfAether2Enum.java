package thumbnailgenerator.enums;

import lombok.Getter;

@Getter
public enum RivalsOfAether2Enum {

    CLAIREN("Clairen", "clairen", 2503),
    FLEET("Fleet", "fleet", 2498),
    FORSBURN("Forsburn", "forsburn", 2599),
    KRAGG("Kragg", "kragg", 2504),
    LOXODONT("Loxodont", "loxodont", 2505),
    MAYPUL("Maypul", "maypul", 2502),
    ORCANE("Orcane", "orcane", 2597),
    RANNO("Ranno", "ranno", 2501),
    WRASTOR("Wrastor", "wrastor", 2500),
    ZETTERBURN("Zetterburn", "zetterburn", 2499),
    ETALUS("Etalus", "etalus", 2615),
    OLYMPIA("Olympia", "olympia", 2619);

    private final String name;
    private final String code;
    private final int startGGId;

    RivalsOfAether2Enum(String name, String code, int startGGId) {
        this.name = name;
        this.code = code;
        this.startGGId = startGGId;
    }
}
