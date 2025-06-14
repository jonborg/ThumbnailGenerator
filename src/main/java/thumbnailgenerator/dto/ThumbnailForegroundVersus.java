package thumbnailgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.json.read.ThumbnailForegroundVersusRead;

@Getter
@Setter
@AllArgsConstructor
public class ThumbnailForegroundVersus {
    private String imagePath;
    private float scale;
    private int verticalOffset;

    public ThumbnailForegroundVersus(ThumbnailForegroundVersusRead thumbnailForegroundVersusRead){
        var isVersusNull = thumbnailForegroundVersusRead == null;
        this.imagePath = isVersusNull ? "" : thumbnailForegroundVersusRead.getImagePath();
        this.scale = isVersusNull ? 1.0f : thumbnailForegroundVersusRead.getScale();
        this.verticalOffset = isVersusNull ? 0 : thumbnailForegroundVersusRead.getVerticalOffset();
    }
}
