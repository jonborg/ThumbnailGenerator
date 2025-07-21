package thumbnailgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.json.read.ThumbnailForegroundLogoRead;

@Getter
@Setter
@AllArgsConstructor
public class ThumbnailForegroundLogo {
    private String imagePath;
    private float scale;
    private int verticalOffset;
    private boolean aboveForeground;

    public ThumbnailForegroundLogo(ThumbnailForegroundLogoRead thumbnailForegroundLogoRead){
        var isLogoNull = thumbnailForegroundLogoRead == null;
        this.imagePath = isLogoNull ? "" : thumbnailForegroundLogoRead.getImagePath();
        this.scale = isLogoNull ? 1.0f : thumbnailForegroundLogoRead.getScale();
        this.verticalOffset = isLogoNull ? 0 : thumbnailForegroundLogoRead.getVerticalOffset();
        this.aboveForeground = !isLogoNull && thumbnailForegroundLogoRead.isAboveForeground();
    }
}
