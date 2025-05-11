package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import thumbnailgenerator.enums.interfaces.FighterArtTypeEnum;

import java.util.List;

@Getter
@Setter
@Builder
public class ThumbnailInput {
    private String tournamentId;
    private String round;
    private String date;
    private FighterArtTypeEnum artType;
    private List<PlayerInput> players;
    private boolean saveLocally;

    public String getExpectedFileName(){
        return getPlayers().get(0).getPlayerName().replace("|","_").replace("/","_")+"-"+
                getPlayers().get(0).getCharacterName().toLowerCase()+getPlayers().get(0).getAlt()+"--"+
                getPlayers().get(1).getPlayerName().replace("|","_").replace("/","_")+"-"+
                getPlayers().get(1).getCharacterName().toLowerCase()+getPlayers().get(1).getAlt()+"--"+
                getRound()+"-"+getDate().replace("/","_")+".png";
    }
}
