package thumbnailgenerator.dto.json.read;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class TournamentListRead {
    @Expose
    @SerializedName("tournamentList")
    private List<TournamentListElementRead> tournamentList;

    @Getter
    public static class TournamentListElementRead{
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("logo")
        private String logo;
    }
}
