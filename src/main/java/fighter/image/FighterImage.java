package fighter.image;

import java.awt.image.BufferedImage;

import fighter.Fighter;
import fighter.image.settings.FighterImageSettings;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.javatuples.Pair;

@Getter
public abstract class FighterImage {
    private final Logger LOGGER = LogManager.getLogger(FighterImage.class);

    protected Fighter fighter;
    protected FighterImageSettings fighterImageSettings;
    protected BufferedImage image;


    public FighterImage(Fighter fighter, FighterImageSettings slotImageSettings, BufferedImage image){
        this.fighter = fighter;
        this.fighterImageSettings = slotImageSettings;
        this.image = image;
    }

    protected abstract BufferedImage editImage(BufferedImage bufferedImage);

    static public void convertToAlternateRender(Fighter fighter) {
        if (fighter.getUrlName() != null) {
            switch (fighter.getUrlName().toLowerCase()) {
                case "ike":
                case "pokemon_trainer":
                case "villager":
                case "wii_fit_trainer":
                case "robin":
                case "cloud":
                case "corrin":
                case "bayonetta":
                case "inkling":
                case "byleth":
                case "kazuya":
                    if (fighter.getAlt() % 2 == 0)
                        fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "olimar":
                    if (fighter.getAlt() > 4)
                        fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "mii_gunner":
                case "random":
                    if (fighter.getAlt() == 2)
                        fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "bowser_jr":
                    fighter.setUrlName(fighter.getUrlName() + fighter.getAlt());
                    break;
                case "joker":
                case "sephiroth":
                    if (fighter.getAlt() < 7)
                        fighter.setUrlName(fighter.getUrlName() + "1");
                    else fighter.setUrlName(fighter.getUrlName() + "2");
                    break;
                case "dq_hero":
                case "sora":
                    if (fighter.getAlt() % 4 == 1)
                        fighter.setUrlName(fighter.getUrlName() + "1");
                    if (fighter.getAlt() % 4 == 2)
                        fighter.setUrlName(fighter.getUrlName() + "2");
                    if (fighter.getAlt() % 4 == 3)
                        fighter.setUrlName(fighter.getUrlName() + "3");
                    if (fighter.getAlt() % 4 == 0)
                        fighter.setUrlName(fighter.getUrlName() + "4");
                    break;
                default:
            }
        }
    }

    static public Pair<String,Integer> convertFromAlternateRender(String urlName){
        if (urlName != null){
            String name = urlName.substring(0, urlName.length() - 1);
            int altNumber = 1;
            try{
                altNumber = Integer.parseInt(urlName.substring(urlName.length() - 1, urlName.length()));
            } catch (NumberFormatException ignored){

            }
            switch(name.toLowerCase()){
                case "ike":
                case "pokemon_trainer":
                case "villager":
                case "wii_fit_trainer":
                case "robin":
                case "cloud":
                case "corrin":
                case "bayonetta":
                case "inkling":
                case "byleth":
                case "kazuya":
                case "mii_gunner":
                case "random":
                    if (altNumber == 2 ) return new Pair<>(name, 2);
                    else return new Pair<>(name, 1);
                case "olimar":
                    if (altNumber == 2 ) return new Pair<>(name, 4);
                    else return new Pair<>(name, 1);
                case "bowser_jr":
                    return new Pair<>(name, altNumber);
                case "joker":
                case "sephiroth":
                    if (altNumber == 2 ) return new Pair<>(name, 7);
                    else return new Pair<>(name, 1);
                case "dq_hero":
                case "sora":
                    if (altNumber == 4 ) return new Pair<>(name, 4);
                    else if (altNumber == 3 ) return new Pair<>(name, 3);
                    else if (altNumber == 2 ) return new Pair<>(name, 2);
                    else return new Pair<>(name, 1);
                default:
                    return new Pair<>(urlName, 1);
            }
        }
        return null;
    }
}
