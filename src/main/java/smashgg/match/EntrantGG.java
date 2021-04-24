package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntrantGG {
    @Expose
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
