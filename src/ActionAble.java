import processing.core.PImage;

import java.util.List;

public abstract class ActionAble extends AnimateAble{
    private double actionPeriod;
    public double getActionPeriod() {return actionPeriod;}
    public ActionAble(String id, Point position, List<PImage> images, int imageIndex, String key, double animationPeriod, double actionPeriod) {
        super(id, position, images, imageIndex, key, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public abstract void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    @Override
    public void scheduleAction(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        Animation animation = new Animation(this, 0);
        Activity act = new Activity(this, world, imageStore);
        scheduler.scheduleEvent(this, act, this.actionPeriod);
        scheduler.scheduleEvent(this, animation, getAnimationPeriod());
    }
}
