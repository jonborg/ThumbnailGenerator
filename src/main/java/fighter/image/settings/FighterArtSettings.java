package fighter.image.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

import fighter.FighterArtType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FighterArtSettings implements Cloneable {

    @Expose
    @SerializedName("artType")
    private FighterArtType artType;
    @Expose
    @SerializedName("fighterImageSettings")
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
