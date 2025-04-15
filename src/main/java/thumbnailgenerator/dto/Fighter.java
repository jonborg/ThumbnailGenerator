package thumbnailgenerator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Fighter  {

    private String  name;
    private String  urlName;
    private int     alt = 1;
    private boolean flip = false;

    public Fighter(){
        super();
    }

    public Fighter(String name, String urlName,int alt, boolean flip){
        this.name = name;
        this.urlName = urlName;
        this.alt = alt;
        this.flip = flip;
    }
}
