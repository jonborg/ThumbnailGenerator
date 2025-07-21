package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class FileThumbnailSettingsRead extends SettingsRead {
    @Expose
    @SerializedName("foreground")
    protected ThumbnailForegroundRead foreground;
}
