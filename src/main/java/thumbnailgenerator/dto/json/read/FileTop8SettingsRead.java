package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class FileTop8SettingsRead extends SettingsRead {
    @Expose
    @SerializedName("slotSettingsFile")
    private String slotSettingsFile;
}
