import processing.core.PImage;

import java.util.List;

public abstract class MoveAble extends ActionAble{
    private PathingStrategy strategy;
    public MoveAble(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod);
        this.strategy = strategy;
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    public abstract Point nextPosition(WorldModel world, Point destPos);
}
