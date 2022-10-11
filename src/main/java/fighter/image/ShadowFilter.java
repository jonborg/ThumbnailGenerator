package fighter.image;

import lombok.AllArgsConstructor;

import java.awt.image.RGBImageFilter;

@AllArgsConstructor
public class ShadowFilter extends RGBImageFilter {
        private int color;
        //0xff_72_5c_ac
        public ShadowFilter() {
            // The filter's operation does not depend on the
            // pixel's location, so IndexColorModels can be
            // filtered directly.
            canFilterIndexColorModel = true;
            color = 0x00_00_00_00;
        }

        public int filterRGB(int x, int y, int rgb) {
            return ((rgb & 0xff_00_00_00) | color) ; // shadow color opaque
        }
}
