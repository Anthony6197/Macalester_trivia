import comp127graphics.CanvasWindow;

import java.util.List;
import java.util.ArrayList;

public class BlockManager {
    private CanvasWindow canvas;
    private List<Block> blocks;
    private final double BLOCK_QUANTITY = 32;

    public BlockManager(CanvasWindow canvas){
        this.canvas = canvas;
    }

    public void generateBlock() {
        this.blocks = new ArrayList<>();
        double leftEdge = canvas.getWidth() * 0.1;
        double rightEdge = canvas.getWidth() * 0.9;
        double x = leftEdge;
        double y = canvas.getHeight() * 0.1;
        double blockSize = canvas.getWidth() * 0.058;

        for (int i = 0; i < BLOCK_QUANTITY; i++) {
            Block block = new Block(x, y, blockSize, blockSize, i);
            blocks.add(block);
            canvas.add(block);

            x += blockSize;
            if (x + blockSize >= rightEdge) {
                x = leftEdge;
                y += blockSize ;
            }
        }
    }

    public Block getBlock(int index){
        for(Block block: blocks){
            if(index == block.getIndex()){
                return block;
            }
        }
        return null;
    }

    public List<Block> getPassedBlocks(int index){
        return blocks.subList(0,index);
    }

}

