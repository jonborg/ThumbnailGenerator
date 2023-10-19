package thumbnailgenerator.service;

import org.springframework.stereotype.Service;
import thumbnailgenerator.dto.Fighter;

@Service
public class SmashUltimateCharacterService {

    public void convertToAlternateRender(Fighter fighter){
        if (fighter.getUrlName() != null){
            switch(fighter.getUrlName().toLowerCase()){
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
                    if (fighter.getAlt() % 2 == 0) fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "olimar":
                    if (fighter.getAlt() > 4 ) fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "mii_gunner":
                case "random":
                    if (fighter.getAlt() == 2) fighter.setUrlName(fighter.getUrlName() + "2");
                    else fighter.setUrlName(fighter.getUrlName() + "1");
                    break;
                case "bowser_jr":
                    fighter.setUrlName(fighter.getUrlName() + fighter.getAlt());
                    break;
                case "joker":
                case "sephiroth":
                    if (fighter.getAlt()<7) fighter.setUrlName(fighter.getUrlName() + "1");
                    else fighter.setUrlName(fighter.getUrlName() + "2");
                    break;
                case "dq_hero":
                case "sora":
                    if (fighter.getAlt() % 4 == 1) fighter.setUrlName(fighter.getUrlName() + "1");
                    if (fighter.getAlt() % 4 == 2) fighter.setUrlName(fighter.getUrlName() + "2");
                    if (fighter.getAlt() % 4 == 3) fighter.setUrlName(fighter.getUrlName() + "3");
                    if (fighter.getAlt() % 4 == 0) fighter.setUrlName(fighter.getUrlName() + "4");
                    break;
                default:
            }
        }
    }

}
