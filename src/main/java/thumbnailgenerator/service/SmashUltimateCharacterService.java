package thumbnailgenerator.service;

import org.javatuples.Pair;
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

    public Pair<String, Integer> convertToCodeAndAlt(String altCode) {
        String code = altCode.substring(0, altCode.length()-1);
        int alt;
        try {
            alt = Integer.parseInt(altCode.substring(altCode.length()-1));
        } catch (NumberFormatException ex){
            return new Pair<>(altCode, 1);
        }

        switch(code){
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
                if (alt % 2 == 0) return new Pair<>(code, 2);
                else return new Pair<>(code,  1);
            case "olimar":
                if (alt > 4 ) return new Pair<>(code, 2);
                else return new Pair<>(code, 1);
            case "mii_gunner":
            case "random":
                if (alt == 2) return new Pair<>(code,  2);
                else return new Pair<>(code,  1);
            case "bowser_jr":
                return new Pair<>(code, alt);
            case "joker":
            case "sephiroth":
                if (alt < 7) return new Pair<>(code, 1);
                else return new Pair<>(code, 2);
            case "dq_hero":
            case "sora":
                if (alt % 4 == 1) return new Pair<>(code, 1);
                if (alt % 4 == 2) return new Pair<>(code, 2);
                if (alt % 4 == 3) return new Pair<>(code, 3);
                if (alt % 4 == 0) return new Pair<>(code, 4);
                break;
            default:
        }
        return new Pair<>(altCode, alt);
    }

}
