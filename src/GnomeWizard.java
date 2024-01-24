import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GnomeWizard extends Gnome implements Transformer{
    private int morphCount;
    public GnomeWizard(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, PathingStrategy strategy, int morphCount) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, strategy);
        this.morphCount = morphCount;
    }
    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
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
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Dude.DUDE_KEY)));
        if (target.isPresent() && moveTo(world, target.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
            Point temp = target.get().getPosition();
            world.removeEntity(scheduler, target.get());
            Gnome gnome = new GnomeBuild(this.getId(), temp, imageStore.getImageList("gnomebuild"), 0, GnomeBuild.GNOME_BUILD_KEY, this.getAnimationPeriod(), this.getActionPeriod(), Gnome.Gnome_Strategy);
            world.addEntity(gnome);
            gnome.scheduleAction(scheduler, world, imageStore);
        }
        Activity act = new Activity(this, world, imageStore);
        scheduler.scheduleEvent( this, act, this.getActionPeriod());
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (morphCount <= 0) {
            Sapling sap = new Sapling(this.getId(), this.getPosition(), imageStore.getImageList("sapling"), 0, Sapling.SAPLING_KEY,
                    Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, 5);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(sap);
            sap.scheduleAction(scheduler, world, imageStore);
            world.setOccupancyCell(sap.getPosition(), sap);
            return true;
        }
        morphCount--;
        return false;
    }
}

