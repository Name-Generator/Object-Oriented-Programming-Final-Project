import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Fairy extends MoveAble{
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;
    public static final PathingStrategy Fairy_Strategy = new AStarPathingStrategy();
    public Fairy(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, strategy);
    }


    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity( scheduler,target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }


    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> path = Fairy_Strategy.computePath(this.getPosition(),
                destPos, p -> world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.isEmpty()){
            return this.getPosition();
        }
        else{
            return path.getFirst();
        }
    }

    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Stump.STUMP_KEY)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveTo( world, fairyTarget.get(), scheduler)) {

                Sapling sapling = new Sapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos,
                        imageStore.getImageList(Sapling.SAPLING_KEY), 0, Sapling.SAPLING_KEY, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_HEALTH);

                world.addEntity(sapling);
                sapling.scheduleAction(scheduler, world, imageStore);
            }
        }
        Activity act = new Activity(this, world, imageStore);
        scheduler.scheduleEvent(this, act, this.getActionPeriod());
    }
}
