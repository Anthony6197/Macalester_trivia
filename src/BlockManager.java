import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Yutong Wu
 * @author Zuofu Huang
 *
 * Block manager class is used to generate map blocks, store the blocks passed by into a list
 * for to set colors and assign types to each map blocks it generates.
 */

public class BlockManager {
    private CanvasWindow canvas;
    private List<Block> blocks = new ArrayList<>();
    private static final int BLOCK_QUANTITY = 36;
    private final List<String> availableTypes = List.of("Math", "Chem"); // flexible to add more types

    public BlockManager(CanvasWindow canvas) {
        this.canvas = canvas;
    }

    /**
     * Generate all blocks, each with a type and a position in the Boustrophedon.
     * The 1st section determines the type of each question by repeatedly looping through the list of available types
     * (in this version, there are two: math and chem. Flexible to add more.)
     * The 2nd section determines the size and layout of blocks with respect to the canvas.
     * The 3rd section loops through each block and determines individual locations in the Boustrophedon.
     */
    public void generateBlock() {

        List<String> BlockByType = new ArrayList<>();
        int rounds = BLOCK_QUANTITY / availableTypes.size();
        int remainder = BLOCK_QUANTITY % availableTypes.size();
        for(int i = 0; i < rounds; i++){
            BlockByType.addAll(availableTypes);
        }
        if(remainder != 0){
            BlockByType.addAll(availableTypes.subList(1,remainder));
        }

        double leftEdge = canvas.getWidth() * 0.1;
        double rightEdge = canvas.getWidth() * 0.9;
        double x = leftEdge;
        double y = canvas.getHeight() * 0.1;
        boolean reverse = false;
        double blockSize = canvas.getWidth() * 0.058;
        int i;

        for (i = 0; i < BLOCK_QUANTITY; i++) {
            Block block = new Block(x, y, blockSize, blockSize,i);
            block.setType(BlockByType.get(i));
            blocks.add(block);
            canvas.add(block);

            if (x + 2 * blockSize >= rightEdge && !reverse || x - blockSize < leftEdge && reverse) {
                reverse = !reverse;
                i++;
                y += blockSize;
                GraphicsText vacation = new GraphicsText();
                vacation.setPosition(x + 0.5*blockSize,y + 0.5*blockSize);
                Block cornerBlock = new Block(x, y, blockSize, blockSize, i);
                cornerBlock.setType(BlockByType.get(i));
                blocks.add(cornerBlock);
                canvas.add(cornerBlock);
                y += blockSize;
            } else {
                if (reverse){
                    x -= blockSize;
                } else {
                    x += blockSize;
                }
            }
        }
    }

    /**
     * Return the block of a specific index from the list of blocks.
     * If there's no such index (meaning there's an error), return null.
     */
    public Block getBlock(int index){
        for (Block block : blocks) {
            if (index == block.getIndex()) {
                return block;
            }
        }
        return null;
    }

    /**
     * When the player is on the block of a certain index, return all blocks the user has surpassed.
     */
    public List<Block> getPassedBlocks(int index){
        return blocks.subList(0, index + 1);
    }

    public int getBlockQuantity () {
        return BLOCK_QUANTITY;
    }
}

