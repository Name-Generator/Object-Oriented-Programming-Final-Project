import processing.core.PImage;

import java.util.List;

public class GnomeHut extends Entity{
    public static final String GNOME_HUT_KEY = "hut";
    public GnomeHut(String id, Point position, List<PImage> images, int imageIndex, String key) {
        super(id, position, images, imageIndex, key);
    }
}
