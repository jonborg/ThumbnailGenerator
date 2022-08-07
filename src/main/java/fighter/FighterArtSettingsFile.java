package fighter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FighterArtSettingsFile implements Cloneable {

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
        if (!(o instanceof FighterArtSettingsFile)) {
            return false;
        }
        FighterArtSettingsFile that = (FighterArtSettingsFile) o;
        return artType == that.artType &&
                Objects.equals(fighterImageSettingsPath,
                        that.fighterImageSettingsPath);
    }

    @Override
    public FighterArtSettingsFile clone() {
        FighterArtSettingsFile clone = null;
        try {
            clone = (FighterArtSettingsFile) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
