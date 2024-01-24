public class Activity extends Action {
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void execute(EventScheduler scheduler) {
        ActionAble action = (ActionAble) this.getEntity();
        action.execute(this.world, this.imageStore, scheduler);
    }
}
