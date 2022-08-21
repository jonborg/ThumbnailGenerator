package top8.generate;

import com.google.gson.reflect.TypeToken;
import exception.CharacterImageSettingsNotFoundException;
import character.DownloadCharacterURL;
import character.Character;
import character.CharacterArtType;
import character.CharacterImage;
import file.json.JSONReader;
import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
import thumbnail.image.ImageSettings;

public class Top8 {

    public static void generateTop8()
            throws IOException, CharacterImageSettingsNotFoundException {
        var list = new ArrayList<Character>();
        list.add(new Character("Pokemon Trainer", "pokemon_trainer2", 1, false ));
        list.add(new Character("Ness", "ness", 1, false ));
        list.add(new Character("Mii Swordfighter", "mii_swordfighter", 1, false ));
        list.add(new Character("Terry", "terry", 1, false ));
        list.add(new Character("Banjo & Kazooie", "banjo_and_kazooie", 1, false ));
        list.add(new Character("King K. Rool", "king_k_rool", 1, false ));
        list.add(new Character("Zero Suit Samus", "zero_suit_samus", 1, false ));
        list.add(new Character("Piranha Plant", "piranha_plant", 1, false ));

        BufferedImage top8 = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = top8.createGraphics();

        List<CharacterSlot> slots = JSONReader.getJSONArray("settings/top8/slot/weeklyL_character_slots.json",
                new TypeToken<ArrayList<CharacterSlot>>() {}.getType());
        for (int i = 0; i < list.size(); i++){
            var place = i +1;
            var slot = slots.stream().filter(s -> s.getPlace() == place).findFirst().orElse(null);
            if (slot == null){
                return;
            }
            var mask = ImageIO.read(new File(slot.getMask()));
            var fighter = DownloadCharacterURL
                    .getCharacterImageOnline(list.get(i), CharacterArtType.RENDER);

            ImageSettings imageSettings = (ImageSettings)
                    JSONReader.getJSONArray("settings/thumbnails/images/default.json",
                            new TypeToken<ArrayList<ImageSettings>>() {}.getType()).get(0);
            var fighterImageSettings = imageSettings.findFighterImageSettings(list.get(i).getUrlName());
            CharacterImage characterImage = new CharacterImage(list.get(i), fighterImageSettings, fighter);

            var image = addShadow(characterImage.getImage(), 0x00_72_5c_ac, -30, 0);
            var maskedImage = applyMask(image, mask);

            g2d.drawImage(maskedImage,slot.getCoordinateY(),slot.getCoordinateX(),null);
        }
        var dir = new File("generated_top8/");
        if (!dir.exists()) dir.mkdir();
        var file = new File("generated_top8/test.png");
        ImageIO.write(top8, "png", file);

    }

    public static BufferedImage applyMask(BufferedImage image, BufferedImage mask) {

        BufferedImage dest = new BufferedImage(mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        AlphaComposite
                ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        g2.setComposite(ac);
        g2.drawImage(mask, 0, 0, null);
        g2.dispose();
        return dest;
    }

    public static BufferedImage createDropShadow(BufferedImage image,
                                                 int size, float opacity) {

        int width = image.getWidth() + size * 2;
        int height = image.getHeight() + size * 2;

        BufferedImage mask = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = mask.createGraphics();
        g2.drawImage(image, size, size, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN,
                opacity));
        g2.setColor(Color.MAGENTA);
        g2.fillRect(0, 0, width, height);
        g2.dispose();

        BufferedImage shadow = createBlurOp(size).filter(mask, null);
        g2 = shadow.createGraphics();
        g2.dispose();

        return shadow;
    }

    private static ConvolveOp createBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1f / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data),
                ConvolveOp.EDGE_NO_OP, null);
    }

    private static BufferedImage addShadow(BufferedImage image, int shadowColor, int offsetX, int offsetY){
        var result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g2 = result.createGraphics();

        var filter = new ShadowFilter(shadowColor);
        var producer = new FilteredImageSource(image.getSource(), filter);
        var shadow = Toolkit.getDefaultToolkit().createImage(producer);
        g2.drawImage(shadow, offsetX, offsetY, null);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        BufferedImage newImage = new BufferedImage(
                shadow.getWidth(null), shadow.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(shadow, 0, 0, null);
        g.dispose();
        try {
            ImageIO.write(newImage, ".png",
                    new File("generated_top8/shadow.png"));
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }

        return result;
    }
}
