import processing.core.PImage;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class Dude extends MoveAble implements Transformer{
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    private int resourceLimit;
    public static final PathingStrategy Dude_Strategy = new AStarPathingStrategy();

    public Dude(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, int resourceLimit, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, strategy);
        this.resourceLimit = resourceLimit;
    }
    public int getResourceLimit() {return this.resourceLimit; }
    @Override
    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    @Override
    public Point nextPosition(WorldModel world, Point destPos){
        List<Point> path = Dude_Strategy.computePath(this.getPosition(),
                destPos,
                p -> world.withinBounds(p) && world.getOccupant(p).equals(Optional.empty()),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.isEmpty()){
            return this.getPosition();
        }
        else{
            return path.getFirst();
        }
    }
}
