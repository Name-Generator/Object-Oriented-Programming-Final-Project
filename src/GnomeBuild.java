import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GnomeBuild extends Gnome implements Transformer{
    private int builtcount = 5;
    public static final String GNOME_BUILD_KEY = "gnome_build";
    public GnomeBuild(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, PathingStrategy strategy) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, strategy);
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
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Obstacle.OBSTACLE_KEY)));
        boolean adjacent = moveTo(world, target.get(), scheduler);

        if (target.isEmpty() || !adjacent || !this.transform(world, scheduler, imageStore)) {
            Activity act = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, act, this.getActionPeriod());
        }
        if (adjacent){
            Point temp = target.get().getPosition();
            world.removeEntity(scheduler, target.get());
            Background bridge = new Background("bridge", imageStore.getImageList("gnomebridge"));
            world.setBackgroundCell(temp, bridge);
            this.builtcount--;
            this.transform(world, scheduler, imageStore);

        }
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (builtcount <= 0){
            Gnome gnome = new GnomeBuilt(this.getId(), this.getPosition(), imageStore.getImageList("gnomebuilt"), 0, "gnomebuilt", this.getAnimationPeriod(), this.getActionPeriod(), Gnome_Strategy);
            scheduler.unscheduleAllEvents(this);
            world.removeEntity(scheduler, this);

            world.addEntity(gnome);
            Activity act = new Activity(gnome, world, imageStore);
            scheduler.scheduleEvent( gnome, act, gnome.getActionPeriod());
            return true;
        }
        return false;
    }
}
