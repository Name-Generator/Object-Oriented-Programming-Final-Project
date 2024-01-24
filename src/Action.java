public abstract class Action {
    private Entity entity;
    public Entity getEntity() {return entity;}

    public Action(Entity entity) {
        this.entity = entity;
    }

    public abstract void execute(EventScheduler scheduler);



}
