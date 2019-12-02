import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;

import java.util.List;
import java.util.Random;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numbercounter;
    private Random rand;
    private List<Map> passbyBoxes;

    public GameBoard(){
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        this.numbercounter = new GraphicsText();
        numbercounter.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.1);
    }
}
