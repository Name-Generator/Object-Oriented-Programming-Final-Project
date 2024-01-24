import processing.core.PImage;

import java.util.List;

public class AnimateAble extends Entity {
    private double animationPeriod;

    public AnimateAble(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod) {
        super(id, position, images, imageIndex, key);
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, animation, getAnimationPeriod());
    }
}
