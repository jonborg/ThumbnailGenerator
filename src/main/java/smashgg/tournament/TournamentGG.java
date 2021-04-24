package smashgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TournamentGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("events")
    private List<EventGG> events;


    public int getId() {
        return id;
    }

    public List<EventGG> getEvents() {
        return events;
    }
}
