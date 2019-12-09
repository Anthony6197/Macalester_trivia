import comp127graphics.CanvasWindow;
import comp127graphics.FontStyle;
import comp127graphics.GraphicsText;
import comp127graphics.ui.Button;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numberCounter;
    private Random rand;
    private BlockManager blockManager;
    private Button dice;
    private int currentBlock;
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
        dice.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.15);
        canvas.add(dice);

        allQuestions = new QuestionBank();

        questionBox = new GraphicsText();
        choiceBox1 = new GraphicsText();
        choiceBox2 = new GraphicsText();
        choiceBox3 = new GraphicsText();
        choiceBox4 = new GraphicsText();

        questionBox.setCenter(canvas.getWidth()*0.3,canvas.getHeight()*0.65);
        questionBox.setFont("Helvetica", FontStyle.BOLD,20);
        choiceBox1.setCenter(canvas.getWidth()*0.3,canvas.getHeight()*0.77);
        choiceBox1.setFont("Helvetica", FontStyle.PLAIN,18);
        choiceBox2.setCenter(canvas.getWidth()*0.7,canvas.getHeight()*0.77);
        choiceBox2.setFont("Helvetica", FontStyle.PLAIN,18);
        choiceBox3.setCenter(canvas.getWidth()*0.3,canvas.getHeight()*0.87);
        choiceBox3.setFont("Helvetica", FontStyle.PLAIN,18);
        choiceBox4.setCenter(canvas.getWidth()*0.7,canvas.getHeight()*0.87);
        choiceBox4.setFont("Helvetica", FontStyle.PLAIN,18);

        canvas.add(questionBox);
        canvas.add(choiceBox1);
        canvas.add(choiceBox2);
        canvas.add(choiceBox3);
        canvas.add(choiceBox4);

        currentBlock = 0;

        run();
    }

    public void moveOnce(){
        int diceRoll = rand.nextInt(6) + 1;
        numberCounter.setText(diceRoll + " steps");
        this.currentBlock += diceRoll;
        updateBlockColor();
        showQuestion();
    }

    public String getCurrentBlockType(){
        return blockManager.getBlock(currentBlock).getType();
    }

    public void updateBlockColor(){
        for(Block block: blockManager.getPassedBlocks(currentBlock)){
            block.setActive(true);
        }
    }
    private List<Question> createQuestionList(String type){
        return allQuestions.findAllQuestionsOfType(type);
    }

    public Question selectQuestion(){
        String type = getCurrentBlockType();
        List<Question> questionList = createQuestionList(type);
        int randomNumber = rand.nextInt(questionList.size());
        return allQuestions.deleteQuestion(randomNumber);
    }

    public GraphicsText showQuestion(){
        Question thisQuestion = selectQuestion();
        questionBox.setText(thisQuestion.getPrompt());

        String rightAnswer = thisQuestion.getRightAnswer();
        List<String> listOfChoices = thisQuestion.getAllChoices();
        System.out.println(listOfChoices);

        Collections.shuffle(listOfChoices);

        choiceBox1.setText(listOfChoices.get(0));
        choiceBox2.setText(listOfChoices.get(1));
        choiceBox3.setText(listOfChoices.get(2));
        choiceBox4.setText(listOfChoices.get(3));

        int rightIndex = listOfChoices.indexOf(rightAnswer);
        return List.of(choiceBox1,choiceBox2,choiceBox3,choiceBox4).get(rightIndex);
    }

//    public boolean checkIfCorrect(){
//        GraphicsText correctChoiceBox = showQuestion();
//        return false;
//    }

    public void run(){
        blockManager.generateBlock();
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
