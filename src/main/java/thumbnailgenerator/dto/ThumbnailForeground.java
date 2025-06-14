package thumbnailgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.json.read.ThumbnailForegroundRead;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ThumbnailForeground {
    private String foreground;
    private Map<String, String> colors;
    private ThumbnailForegroundVersus thumbnailForegroundVersus;
    private ThumbnailForegroundLogo thumbnailForegroundLogo;
    private boolean customForeground;

    public ThumbnailForeground(ThumbnailForegroundRead thumbnailForegroundRead){
        this.foreground = thumbnailForegroundRead.getForeground();
        setupColors(thumbnailForegroundRead.getColors());
        this.thumbnailForegroundVersus = new ThumbnailForegroundVersus(thumbnailForegroundRead
                .getThumbnailForegroundVersus());
        this.thumbnailForegroundLogo = new ThumbnailForegroundLogo(thumbnailForegroundRead
                .getThumbnailForegroundLogo());
        this.customForeground = thumbnailForegroundRead.isCustomForeground();
    }

    private void setupColors(Map<String, String> colors){
        this.colors = colors;
        this.colors.putIfAbsent("primary", "#333333FF");
        this.colors.putIfAbsent("secondary", "#337733FF");
    }
}
