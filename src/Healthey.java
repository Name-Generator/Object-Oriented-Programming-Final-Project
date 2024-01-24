import processing.core.PImage;

import java.util.List;

public abstract class Healthey extends ActionAble implements Transformer{
    protected int health;
    public Healthey(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, int health) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod);
        this.health = health;
    }
    public int getHealth() {
        return this.health;
    }
}
