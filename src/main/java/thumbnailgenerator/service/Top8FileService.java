package thumbnailgenerator.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;
import thumbnailgenerator.dto.Player;
import thumbnailgenerator.dto.Top8;
import thumbnailgenerator.enums.SmashUltimateFighterArtType;


@Service
public class Top8FileService extends FileService<Top8, Player>{

    private int gameIndex = 2;

    public Top8 getTop8FromFile (InputStream inputStream, Boolean saveLocally){
        var tuple = readGraphicGenerationFile(inputStream);
        var top8 = tuple.getValue0();
        var playerList = tuple.getValue1();

        top8.setPlayers(playerList);
        top8.setLocally(saveLocally);
        if (top8.getArtType() == null){
            top8.setArtType(SmashUltimateFighterArtType.RENDER);
        }

        return top8;
    }

    @Override
    protected Top8 createEmptyGeneratedGraphic() {
        return Top8.builder().build();
    }

    @Override
    protected int getGameIndex(){
        return this.gameIndex;
    }


    @Override
    protected Player getCharacterData(List<String> parameters) {

        var characters = new ArrayList<Fighter>();
        var characterQuantity = parameters.size()/2;
        for (int i = 0; i<characterQuantity; i++) {
            characters.add(new Fighter("", parameters.get(2*i),
                    Integer.parseInt(parameters.get(1+2*i)), false));
        }
        return new Player("", characters);
    }
}
