package thumbnailgenerator.service;

import com.google.gson.reflect.TypeToken;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.FighterImageSettings;
import thumbnailgenerator.dto.FighterImageThumbnailSettings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.dto.ImageSettings;
import thumbnailgenerator.dto.Thumbnail;
import thumbnailgenerator.exception.OnlineImageNotFoundException;
import thumbnailgenerator.service.json.JSONReaderService;
import thumbnailgenerator.service.json.JSONWriterService;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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

    public void convertThumbnailCharacterOffsets(String filePath, Game game, String artTypeString)
            throws MalformedURLException, OnlineImageNotFoundException {
        ImageSettings imageSettings = (ImageSettings) jsonReaderService
                .getJSONArrayFromFile(filePath,
                        new TypeToken<ArrayList<ImageSettings>>() {
                        }.getType()).get(0);
        List<FighterImageThumbnailSettings> newFighterImageSettingsList =
                new ArrayList<>();

        for (var setting : imageSettings.getFighterImages()) {
            var characterCode = setting.getFighter();
            var scale = setting.getScale();
            var offset = setting.getOffset()[0] == 0 && setting.getOffset()[1] == 0
                    ? setting.getOffset()
                    : generateOffset(setting, game, artTypeString);
            var flip = setting.isFlip();
            var newFighterImageSetting = new FighterImageThumbnailSettings(
                    characterCode,
                    offset,
                    scale,
                    flip
            );
            newFighterImageSettingsList.add(newFighterImageSetting);
        }
        imageSettings.setFighterImages(newFighterImageSettingsList);

        jsonWriterService.updateThumbnailImageSettings(imageSettings, filePath);
    }

    private int[] generateOffset(FighterImageThumbnailSettings settings, Game game, String artTypeString)
            throws MalformedURLException, OnlineImageNotFoundException {
        var codeAltPair = game.equals(Game.SSBU)
                ? smashUltimateCharacterService.convertToCodeAndAlt(settings.getFighter())
                : new Pair<>(settings.getFighter(), 1);
        var imageFetcher = gameEnumService.getCharacterImageFetcher(game);
        var artTypeEnum = gameEnumService.getArtTypeEnum(game, artTypeString);

        var fighter = new Fighter(codeAltPair.getValue0(), codeAltPair.getValue0(),
                codeAltPair.getValue1(), settings.isFlip());
        var thumbnail = Thumbnail.builder().artType(artTypeEnum).locally(false).build();
        var originalImage = imageFetcher.getCharacterImage(fighter, thumbnail);
        var scaledImage = imageService.resizeImage(originalImage, settings.getScale());

        var cropOffset = - Math.max((scaledImage.getWidth() + Math.abs(2*settings.getOffset()[0]) - 640) / 2, 0);
        var flipMultiplier = settings.isFlip() ? -1 : 1;
        var offsetTotal = new int[]{flipMultiplier*Math.max(2*settings.getOffset()[0], 0) + flipMultiplier*cropOffset, settings.getOffset()[1]};

        return offsetTotal;
    }
}
