package thumbnailgenerator.dto.startgg.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.dto.startgg.match.StreamGG;

@Getter
@Setter
@Builder
public class SearchGamesGG {
    String query;
    String eventName;
    String searchMode;
    StreamGG stream;
    int gameId;
}
