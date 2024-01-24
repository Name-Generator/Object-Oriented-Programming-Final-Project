import processing.core.PImage;

import java.util.List;

public class Sapling extends Healthey{
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public Sapling(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod, int health) {
        super(id, position, images, imageIndex, key, animationPeriod, actionPeriod, health);
    }
    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        this.health++;
        if (!this.transform( world, scheduler, imageStore)) {
            Activity act = new Activity(this, world, imageStore);
            scheduler.scheduleEvent(this, act, this.getActionPeriod());
        }
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(),
                    this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY), 0, Stump.STUMP_KEY);

            world.removeEntity( scheduler,this);

            world.addEntity(stump);

            return true;
        } else if (this.health >= SAPLING_HEALTH_LIMIT) {
            Tree tree = new Tree(Tree.TREE_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Tree.TREE_KEY),
                    0, Tree.TREE_KEY, this.getPosition().getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN),
                    this.getPosition().getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN),
                    this.getPosition().getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
