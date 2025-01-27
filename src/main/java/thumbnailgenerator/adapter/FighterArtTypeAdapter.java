package thumbnailgenerator.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import thumbnailgenerator.dto.Game;
import thumbnailgenerator.enums.RivalsOfAether2ArtType;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;
import thumbnailgenerator.enums.StreetFighter6FighterArtType;
import thumbnailgenerator.enums.interfaces.FighterArtType;

import java.io.IOException;

public class FighterArtTypeAdapter extends TypeAdapter<FighterArtType> {


    @Override
    public void write(JsonWriter out, FighterArtType value) throws IOException {
        out.beginObject();
        if(value instanceof SmashUltimateFighterArtType) {
            out.name("artType")
                    .value(((SmashUltimateFighterArtType) value).getName());
        }
        if(value instanceof RivalsOfAether2ArtType) {
            out.name("artType")
                    .value(((RivalsOfAether2ArtType) value).getName());
        }
        if(value instanceof StreetFighter6FighterArtType) {
            out.name("artType")
                    .value(((StreetFighter6FighterArtType) value).getName());
        }
        out.endObject();
    }

    @Override
    public FighterArtType read(JsonReader in) throws IOException {
        String gameName = null;
        String artTypeValue = null;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("game")) {
                gameName = in.nextString();
            } else if (name.equals("artType")) {
                artTypeValue = in.nextString();
            }
        }
        in.endObject();

        if (gameName == null || artTypeValue == null) {
            throw new IOException("Invalid JSON: Missing required fields for FighterArtType");
        }

        return getEnumForGame(gameName, artTypeValue);
    }

    private FighterArtType getEnumForGame(String gameName, String artTypeValue) {
        switch (Game.valueOf(gameName)) {
            case SSBU:
                return SmashUltimateFighterArtType.valueOf(artTypeValue);
            case ROA2:
                return RivalsOfAether2ArtType.valueOf(artTypeValue);
            case SF6:
                return StreetFighter6FighterArtType.valueOf(artTypeValue);
            default:
                throw new IllegalArgumentException("Unknown gameName: " + gameName);
        }
    }
}

