public class Animation extends Action {
    private int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    @Override
    public void execute(EventScheduler scheduler) {
        this.getEntity().nextImage();

        if (this.repeatCount != 1) {
            AnimateAble animation = (AnimateAble)this.getEntity();
            Action action = new Animation(animation, Math.max(this.repeatCount - 1, 0));
            scheduler.scheduleEvent(animation, action, animation.getAnimationPeriod());
        }
    }
}
