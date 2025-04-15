package thumbnailgenerator.dto.startgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import thumbnailgenerator.dto.startgg.match.StreamGG;

@Getter
public class TournamentGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("streams")
    private List<StreamGG> streams;
    @Expose
    @SerializedName("events")
    private List<EventGG> events;
}
