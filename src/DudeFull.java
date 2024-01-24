import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude{
    public DudeFull(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, int resourceLimit, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, resourceLimit, strategy);
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(House.HOUSE_KEY)));

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            Activity act = new Activity(this, world, imageStore);
            scheduler.scheduleEvent( this, act, this.getActionPeriod());
        }
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Dude dude = new DudeNotFull(this.getId(), this.getPosition(), this.getImages(), 0, Dude.DUDE_KEY,
                this.getAnimationPeriod(), this.getActionPeriod(), 0, this.getResourceLimit(), Dude.Dude_Strategy);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleAction(scheduler, world, imageStore);
        return true;
    }
}
