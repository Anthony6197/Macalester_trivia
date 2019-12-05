import comp127graphics.Rectangle;

import java.awt.*;

public class Block extends Rectangle {
    private final int index;

    public Block(double x, double y, double width, double height, int index) {
        super(x, y, width, height);
        this.index = index;
        setStrokeWidth(Math.rint((width + height) / 40 + 1) * 0.5);
        setActive(false);
    }

    public int getIndex(){
        return this.index;
    }

    /**
     * Changes the color of the box to indicate whether it is active. The meaning of “active” is up
     * to each widget that uses this class.
     */
    public void setActive(boolean active) {
        setFillColor(active
                ? new Color(0x3ba634)
                : new Color(0xD9D9D9));
    }

}
