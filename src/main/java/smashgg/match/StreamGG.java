package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StreamGG {
    @Expose
    @SerializedName("streamName")
    private String streamName;

    public String getStreamName() {
        return streamName;
    }
}
