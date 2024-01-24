import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude implements Transformer{
    private int resourceCount;
    private int resourceLimit;
    public DudeNotFull(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, int resourceCount, int resourceLimit, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, resourceLimit, strategy);
        this.resourceCount = 0;
    }
    public int getResourceCount() {
        return resourceCount;
    }
    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }
    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            this.setResourceCount(this.getResourceCount() + 1);
            ((Healthey)target).health--;
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
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.TREE_KEY, Sapling.SAPLING_KEY)));

        if (target.isEmpty() || !moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
            Activity act = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, act, this.getActionPeriod());
        }
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Dude dude = new DudeFull(this.getId(), this.getPosition(), this.getImages(), 0, Dude.DUDE_KEY,
                    this.getAnimationPeriod(), this.getActionPeriod(), this.getResourceLimit(), Dude.Dude_Strategy);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleAction(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
}
