import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GnomeBuilt extends Gnome{
    public static final String GNOME_KEY = "gnome";
    public GnomeBuilt(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, strategy);
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
        Optional<Entity> huttarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(GnomeHut.GNOME_HUT_KEY)));
        if (huttarget.isPresent() && moveTo(world, huttarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            Activity act = new Activity(this, world, imageStore);
            scheduler.scheduleEvent( this, act, this.getActionPeriod());
        }
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Dude dude = new DudeNotFull(this.getId(), this.getPosition(), imageStore.getImageList("dude"), 0, Dude.DUDE_KEY,
                0.180, 0.787, 0 ,5 , Dude.Dude_Strategy);
        world.removeEntity(scheduler, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(dude);
        dude.scheduleAction(scheduler, world, imageStore);

        return true;
    }
}