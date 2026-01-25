package thumbnailgenerator.dto.json.write;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import thumbnailgenerator.dto.Tournament;

import java.util.List;

@Getter
public class TournamentListWrite {
    @Expose
    @SerializedName("tournamentList")
    private final List<TournamentListElementWrite> tournamentList;

    public TournamentListWrite(List<TournamentListElementWrite> tournamentList) {
        this.tournamentList = tournamentList;
    }

    @Getter
    public static class TournamentListElementWrite{
        @Expose
        @SerializedName("id")
        private final String id;
        @Expose
        @SerializedName("name")
        private final String name;
        @Expose
        @SerializedName("logo")
        private final String logo;

        public TournamentListElementWrite(Tournament tournament){
            this.id = tournament.getTournamentId();
            this.name = tournament.getName();
            this.logo = tournament.getImage();
        }
    }
}
