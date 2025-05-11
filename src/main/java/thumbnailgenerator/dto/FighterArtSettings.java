package thumbnailgenerator.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

@Builder
@Getter
@Setter
@ToString
public class FighterArtSettings implements Cloneable {

    private FighterArtTypeEnum artType;
    private String fighterImageSettingsPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FighterArtSettings)) {
            return false;
        }
        FighterArtSettings that = (FighterArtSettings) o;
        return artType == that.artType &&
                Objects.equals(fighterImageSettingsPath,
                        that.fighterImageSettingsPath);
    }

    @Override
    public FighterArtSettings clone() {
        FighterArtSettings clone = null;
        try {
            clone = (FighterArtSettings) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
