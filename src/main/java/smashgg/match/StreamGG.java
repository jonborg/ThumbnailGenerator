package smashgg.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class StreamGG {
    @Expose
    @SerializedName("streamName")
    private String streamName;

    public String toString(){
        return streamName == null ? "-" : streamName;
    }

    public boolean isNull(){
        return streamName == null;
    }
}
