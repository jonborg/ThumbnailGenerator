package thumbnailgenerator.dto.startgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import java.util.List;

@Getter
public class SetNodeGG{
    @Expose
    @SerializedName("fullRoundText")
    private String roundName;
    @Expose
    @SerializedName("slots")
    private List<SlotGG> slots;
    @Expose
    @SerializedName("games")
    private List<GameGG> games;
    @Expose
    @SerializedName("stream")
    private StreamGG stream;

    public String getRoundName() {
        if (roundName.contains("-Final")) {
            return roundName.replace("-Final", "s");
        }
        if (roundName.contains("Final")){
            return roundName + "s";
        }
        return roundName;
    }

    public String getStreamName() {
        return stream.getStreamName();
    }

    public Boolean hasStream(){
        return stream != null && getStreamName() != null;
    }

    public EntrantGG getEntrant(int entrantIndex){
        return slots.get(entrantIndex).getEntrant();
    }

    public String getEntrateNameWithNoTeam(String name){
        return name.contains(" | ") ? name.split(" \\| ")[1].trim() : name;
    }
}

