import processing.core.PImage;

import java.util.List;

public class Obstacle extends AnimateAble{
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;

    public Obstacle(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod) {
        super(id, position, images, imageIndex, key, animationPeriod);
    }
}
