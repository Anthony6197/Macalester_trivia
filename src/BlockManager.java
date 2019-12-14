import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;

import java.util.List;
import java.util.ArrayList;

public class BlockManager {
    private CanvasWindow canvas;
    private List<Block> blocks;
    private static final int BLOCK_QUANTITY = 36;
    private final List<String> availableTypes = List.of("Math", "Chem");

    public BlockManager(CanvasWindow canvas) {
        this.canvas = canvas;
    }

    public void generateBlock() {
        this.blocks = new ArrayList<>();

        List<String> BlockByType = new ArrayList<>();
        int rounds = BLOCK_QUANTITY / availableTypes.size();
        int remainder = BLOCK_QUANTITY % availableTypes.size();
        for(int i = 0; i < rounds; i++){
            BlockByType.addAll(availableTypes);
        }
        if(remainder != 0){
            BlockByType.addAll(availableTypes.subList(0,remainder));
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
                    System.out.println(cornerBlock.getIndex());
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

        public Block getBlock(int index){
            for (Block block : blocks) {
                if (index == block.getIndex()) {
                    return block;
                }
            }
            return null;
        }

        public List<Block> getPassedBlocks(int index){
            return blocks.subList(0, index + 1);
        }

        public int getBlockQuantity () {
            return BLOCK_QUANTITY;
        }
}

