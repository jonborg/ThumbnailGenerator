package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.FighterImageThumbnailSettings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.enums.LoadingType;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.service.json.JSONReaderService;
import thumbnailgenerator.service.json.JSONWriterService;
import thumbnailgenerator.ui.factory.alert.AlertFactory;
import thumbnailgenerator.ui.loading.LoadingState;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LegacyService {

    @Autowired
    private JSONReaderService jsonReaderService;
    @Autowired
    private JSONWriterService jsonWriterService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private SmashUltimateCharacterService smashUltimateCharacterService;
    @Autowired
    private GameEnumService gameEnumService;
    @Autowired
    private ExecutorService executorService;

    public void convertThumbnailCharacterOffsets(String filePath, Game game, FighterArtTypeEnum artType, LoadingState loadingState)
            throws MalformedURLException, OnlineImageNotFoundException {
        ImageSettings imageSettings = (ImageSettings) jsonReaderService
                .getJSONArrayFromFile(filePath,
                        new TypeToken<ArrayList<ImageSettings>>() {
                        }.getType()).get(0);
        List<FighterImageThumbnailSettings> newFighterImageSettingsList =
                new ArrayList<>();

        AtomicInteger characterCounter = new AtomicInteger();
        int totalCount = imageSettings.getFighterImages().size();
        var future = CompletableFuture.runAsync(() -> {
            for (var setting : imageSettings.getFighterImages()) {
                loadingState
                        .update(true, LoadingType.THUMBNAIL_POSITION_CONVERSION,
                                characterCounter.incrementAndGet(), totalCount);
                var characterCode = setting.getFighter();
                var scale = setting.getScale();
                int[] offset = new int[]{0, 0};
                try {
                    offset = setting.getOffset()[0] == 0 &&
                            setting.getOffset()[1] == 0
                            ? setting.getOffset()
                            : generateOffset(setting, game, artType);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                var flip = setting.isFlip();
                var newFighterImageSetting = new FighterImageThumbnailSettings(
                        characterCode,
                        offset,
                        scale,
                        flip
                );
                newFighterImageSettingsList.add(newFighterImageSetting);
            }
        }, executorService);

        future.thenRun(() -> {
            imageSettings.setFighterImages(newFighterImageSettingsList);
            jsonWriterService.updateThumbnailImageSettings(imageSettings, filePath);
            loadingState.disableLoading();
            Platform.runLater(() -> AlertFactory.displayInfo("Conversion completed."));
        })
        .exceptionally(throwable -> {
            Platform.runLater(() -> {
                loadingState.disableLoading();
                AlertFactory.displayError(
                    "An issue has occurred when converting offsets",
                    ExceptionUtils.getStackTrace(throwable));
            });
            return null;
        });
    }

    private int[] generateOffset(FighterImageThumbnailSettings settings, Game game, FighterArtTypeEnum artType)
            throws MalformedURLException, OnlineImageNotFoundException {
        var codeAltPair = game.equals(Game.SSBU)
                ? smashUltimateCharacterService.convertToCodeAndAlt(settings.getFighter())
                : new Pair<>(settings.getFighter(), 1);
        var imageFetcher = gameEnumService.getCharacterImageFetcher(game);

        var fighter = new Fighter(codeAltPair.getValue0(), codeAltPair.getValue0(),
                codeAltPair.getValue1(), settings.isFlip());
        var thumbnail = Thumbnail.builder().artType(artType).locally(false).build();
        var originalImage = imageFetcher.getCharacterImage(fighter, thumbnail);
        var scaledImage = imageService.resizeImage(originalImage, settings.getScale());
        var offsetImage = imageService.offsetImage(scaledImage, settings.getOffset());
        var flipImage = imageService.flipImage(offsetImage, settings.isFlip());
        var cropImage = imageService.cropImage(flipImage, 640, 720);

        var cropOffset = - Math.max((scaledImage.getWidth() + Math.abs(2*settings.getOffset()[0]) - 640) / 2, 0);
        var flipOffset = settings.isFlip() ? (int) Math.max((320.0-cropImage.getWidth())/2, 0) : 0;
        var offsetTotal = new int[]{
                Math.max(2*settings.getOffset()[0], 0) + cropOffset + flipOffset,
                settings.getOffset()[1]
        };
        return offsetTotal;
    }
}
