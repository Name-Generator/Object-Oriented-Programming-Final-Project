import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Gnome extends MoveAble implements Transformer{
    public static final PathingStrategy Gnome_Strategy = new AStarPathingStrategy();
    public Gnome(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, strategy);
    }
    @Override
    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    @Override
    public Point nextPosition(WorldModel world, Point destPos){
        List<Point> path = Gnome_Strategy.computePath(this.getPosition(),
                destPos,
                p -> world.withinBounds(p) && world.getOccupant(p).equals(Optional.empty()),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.isEmpty()){
            return this.getPosition();
        }
        else{
//            System.out.println(destPos);
//            System.out.println(path);

            return path.getFirst();
        }
    }
}
