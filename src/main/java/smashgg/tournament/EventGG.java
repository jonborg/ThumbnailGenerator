package smashgg.tournament;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventGG {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("phases")
    private List<PhaseGG> phases;

    public int getId() {
        return id;
    }

    public String getName() {
        return name == null ? "-" : name;
    }

    public List<PhaseGG> getPhases() {
        return phases;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isNull(){
        return name == null;
    }
}
