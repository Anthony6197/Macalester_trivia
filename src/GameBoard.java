import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;
import comp127graphics.ui.Button;

import java.util.List;
import java.util.Random;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numberCounter;
    private Random rand;
    private BlockManager blockManager;
    private Button dice;
    private int steps;
    private GraphicsText questionBox;
    private GraphicsText choiceBox1;
    private GraphicsText choiceBox2;
    private GraphicsText choiceBox3;
    private GraphicsText choiceBox4;
    private QuestionBank allQuestions;

    public GameBoard(){
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        this.numberCounter = new GraphicsText();
        numberCounter.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.1);
        canvas.add(numberCounter);

        blockManager = new BlockManager(this.canvas);

        rand = new Random();

        dice = new Button("Click");
        dice.onClick(this::moveOnce);
        dice.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.11);
        canvas.add(dice);

        allQuestions = new QuestionBank();

        run();
    }

    public void moveOnce(){
        int diceRoll = rand.nextInt(6) + 1;
        numberCounter.setText(diceRoll + " steps");
        this.steps += diceRoll;
        updateBlockColor();
    }

    public void updateBlockColor(){
        for(Block block: blockManager.getPassedBlocks(steps)){
            block.setActive(true);
        }
    }
    private List<Question> createQuestionList(String type){
        return allQuestions.findAllQuestionsOfType(type);
    }

    public Question selectQuestion(String type){
        List<Question> questionList = createQuestionList(type);
        int randomNumber = rand.nextInt(questionList.size());
        return allQuestions.deleteQuestion(randomNumber);
    }

    public void showQuestion(String type){
        removeCurrentContent();
        Question thisQuestion = selectQuestion(type);
        assert thisQuestion.getType().equals(type);
    }

    public void removeCurrentContent(){
        canvas.remove(questionBox);
        canvas.remove(choiceBox1);
        canvas.remove(choiceBox2);
        canvas.remove(choiceBox3);
        canvas.remove(choiceBox4);
    }

    public void run(){
        blockManager.generateBlock();
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
