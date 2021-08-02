package smashgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import smashgg.match.StreamGG;

import java.util.List;

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


    public int getId() {
        return id;
    }

    public List<StreamGG> getStreams() {
        return streams;
    }

    public List<EventGG> getEvents() {
        return events;
    }
}
