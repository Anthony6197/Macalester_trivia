import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsGroup;

public class MapManger {
    private CanvasWindow canvas;
    private GraphicsGroup group;
    private final double MAPBOX_QUANTITY = 32;

    public MapManger(CanvasWindow canvas){
        this.canvas = canvas;
    }

    public void generateMapbox(){
        double x = canvas.getWidth()*0.1;
        double y = canvas.getHeight()*0.1;
        for(int i = 0; i < MAPBOX_QUANTITY; i++){
        Map mapbox = new Map(x, y,canvas.getWidth()*0.118,canvas.getWidth()*0.118);
        if (mapbox.getX()+mapbox.getWidth() < canvas.getWidth()){
            x += mapbox.getWidth();
            group.add(mapbox);
            canvas.add(mapbox);
            }
        else if(mapbox.getX()+mapbox.getWidth() == canvas.getWidth()){
            x -= mapbox.getWidth();
            y += 2*mapbox.getHeight();
        }
        }
    }
}
