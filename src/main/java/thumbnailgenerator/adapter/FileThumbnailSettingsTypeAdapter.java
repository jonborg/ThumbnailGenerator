package thumbnailgenerator.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.val;
import thumbnailgenerator.dto.FighterArtSettings;
import thumbnailgenerator.dto.FileThumbnailSettings;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.RivalsOfAether2FighterArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.Tekken8FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileThumbnailSettingsTypeAdapter extends TypeAdapter<FileThumbnailSettings> {


    @Override
    public void write(JsonWriter out, FileThumbnailSettings value) throws IOException {
        out.beginObject();
        out.name("game").value(value.getGame().getName());
        out.name("foreground").value(value.getForeground());
        out.name("background").value(value.getBackground());

        out.name("artSettings");
        out.beginArray();
        for (val artSetting : value.getArtTypeDir()) {
            out.beginObject();
            FighterArtType artType = artSetting.getArtType();
            if (artType instanceof SmashUltimateFighterArtType) {
                out.name("artType")
                        .value(((SmashUltimateFighterArtType) artType).getName());
            }
            if (artType instanceof RivalsOfAether2FighterArtType) {
                out.name("artType")
                        .value(((RivalsOfAether2FighterArtType) artType).getName());
            }
            if (artType instanceof StreetFighter6FighterArtType) {
                out.name("artType")
                        .value(((StreetFighter6FighterArtType) artType)
                                .getName());
            }
            out.name("fighterImageSettings").value(artSetting.getFighterImageSettingsPath());
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public FileThumbnailSettings read(JsonReader in) throws IOException {
        Game gameName = null;
        String foreground = null;
        String background = null;
        List<FighterArtSettings> artTypeSettings = new ArrayList<>();

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "game":
                    gameName = Game.valueOf(in.nextString());
                    break;
                case "foreground":
                    foreground = in.nextString();
                    break;
                case "background":
                    background = in.nextString();
                    break;
                case "artSettings":
                    in.beginArray();
                    artTypeSettings = adaptFighterArtSettings(in, gameName);
                    in.endArray();
                    break;
            }
        }
        in.endObject();
        return new FileThumbnailSettings(gameName, foreground, background, artTypeSettings, null);
    }

    private List<FighterArtSettings> adaptFighterArtSettings(JsonReader in, Game game) throws IOException {
        List<FighterArtSettings> artSettings = new ArrayList<>();
        while (in.hasNext()) {
            in.beginObject();
            FighterArtType artType = null;
            String fighterImageSettings = null;
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "artType":
                        artType = getEnumForGame(game, in.nextString());
                        break;
                    case "fighterImageSettings":
                        fighterImageSettings = in.nextString();
                        break;
                }
            }
            in.endObject();
            val artSetting = FighterArtSettings.builder()
                    .artType(artType)
                    .fighterImageSettingsPath(fighterImageSettings)
                    .build();
            artSettings.add(artSetting);
        }
        return artSettings;
    }

    private FighterArtType getEnumForGame(Game game, String artTypeValue) {
        switch (game) {
            case SSBU:
                return SmashUltimateFighterArtType.valueOf(artTypeValue);
            case ROA2:
                return RivalsOfAether2FighterArtType.valueOf(artTypeValue);
            case SF6:
                return StreetFighter6FighterArtType.valueOf(artTypeValue);
            case TEKKEN8:
                return Tekken8FighterArtType.valueOf(artTypeValue);
            default:
                throw new IllegalArgumentException("Unknown gameName: " + game);
        }
    }
}

