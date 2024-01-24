import processing.core.PImage;

import java.util.List;

public class Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private String key;

    public final static int PROPERTY_KEY = 0;
    public final static int PROPERTY_ID = 1;
    public static final int PROPERTY_COL = 2;
    public static final int PROPERTY_ROW = 3;
    public static final int ENTITY_NUM_PROPERTIES = 4;

    public Entity(String id, Point position, List<PImage> images, int imageIndex, String key){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
        this.key = key;
    }
    public String getId() {return id;}
    public void setPosition(Point position) {this.position = position;}
    public Point getPosition() { return position; }
    public String getKey() {return key;}
    public List<PImage> getImages(){ return this.images; }
    public int getImageIndex() {return imageIndex;}

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }
    /**
    * Helper method for testing. Preserve this functionality while refactoring.
    */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(),
                        this.position.getY(), this.imageIndex);
    }
}
