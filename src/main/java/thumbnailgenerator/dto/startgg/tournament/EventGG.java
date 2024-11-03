package thumbnailgenerator.dto.startgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class EventGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("videogame")
    private VideoGameGG videoGameGG;
    @Expose
    @SerializedName("phases")
    private List<PhaseGG> phases;

    @Override
    public String toString() {
        return name == null ? "-" : name;

    }

    public boolean isNull(){
        return name == null;
    }
}
