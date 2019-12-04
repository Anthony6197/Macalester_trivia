import comp127graphics.CanvasWindow;

import java.util.List;
import java.util.ArrayList;

public class MapManager {
    private CanvasWindow canvas;
    private List<Map> Boxes;
    private final double MAPBOX_QUANTITY = 40;

    public MapManager(CanvasWindow canvas){
        this.canvas = canvas;
    }

    public void generateMapbox(){
        this.Boxes = new ArrayList<>();
        double x = canvas.getWidth()*0.1;
        double y = canvas.getHeight()*0.2;
        for(int i = 0; i < MAPBOX_QUANTITY; i++){
        Map mapBox = new Map(x, y,canvas.getWidth()*0.118,canvas.getWidth()*0.118);
        if (mapBox.getX()+mapBox.getWidth() < canvas.getWidth()){
            x += mapBox.getWidth();
            Boxes.add(mapBox);
            canvas.add(mapBox);
        } else {
            x = canvas.getWidth() * 0.1;
            y += mapBox.getHeight();
            }
        }
        canvas.remove(getMapBox(8));
    }

    public Map getMapBox(int index){
        for(Map box:Boxes){
            if(index == box.getIndex()){
                return box;
            }
        }
        return null;
    }

    public List<Map> getPassedBoxes(int index){
        List<Map> passedBoxes = new ArrayList<>();
        for(Map box: Boxes){
            if(box.getIndex() <= index){
                passedBoxes.add(box);
            }
            box.setIndex(box.getIndex()-index);
        }
        return passedBoxes;
    }

}

