import comp127graphics.*;
import comp127graphics.ui.Button;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numberCounter;
    private Random rand;
    private BlockManager blockManager;
    private int currentBlock = -1;
    private GraphicsText questionBox;
    private GraphicsText choiceBox1;
    private GraphicsText choiceBox2;
    private GraphicsText choiceBox3;
    private GraphicsText choiceBox4;
    private QuestionBank allQuestions;
    private GraphicsGroup questionGroup;

    private int currentScore = 0;
    private boolean exceed = false;

    private int currentRightAnswer;
    private int userChoice;

    private GraphicsText textBox3;

    public GameBoard(){
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        this.numberCounter = new GraphicsText();
        numberCounter.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.1);
        canvas.add(numberCounter);

        blockManager = new BlockManager(this.canvas);
        rand = new Random();

        Button dice = new Button("Move forward");
        dice.onClick(this::moveForward);
        dice.setPosition(canvas.getWidth()*0.865,canvas.getHeight()*0.15);
        canvas.add(dice);

        this.questionGroup = new GraphicsGroup();

        createChoiceButton(0,0.14, 0.725);
        createChoiceButton(1,0.54, 0.725);
        createChoiceButton(2,0.14, 0.825);
        createChoiceButton(3,0.54, 0.825);

        canvas.animate(() ->{
            if (currentScore < 60 && exceed){
                showLose();
            } else if (currentScore >= 60 && exceed){
                showWin();
            }
        });

        allQuestions = new QuestionBank();

        questionBox = new GraphicsText();
        choiceBox1 = new GraphicsText();
        choiceBox2 = new GraphicsText();
        choiceBox3 = new GraphicsText();
        choiceBox4 = new GraphicsText();

        questionBox.setCenter(canvas.getWidth()*0.3,canvas.getHeight()*0.65);
        questionBox.setFont("Helvetica", FontStyle.BOLD,20);
        choiceBox1.setCenter(canvas.getWidth()*0.25,canvas.getHeight()*0.75);
        choiceBox1.setFont("Helvetica", FontStyle.PLAIN,18);
        choiceBox2.setCenter(canvas.getWidth()*0.65,canvas.getHeight()*0.75);
        choiceBox2.setFont("Helvetica", FontStyle.PLAIN,18);
        choiceBox3.setCenter(canvas.getWidth()*0.25,canvas.getHeight()*0.85);
        choiceBox3.setFont("Helvetica", FontStyle.PLAIN,18);
        choiceBox4.setCenter(canvas.getWidth()*0.65,canvas.getHeight()*0.85);
        choiceBox4.setFont("Helvetica", FontStyle.PLAIN,18);

        questionGroup.add(questionBox);
        questionGroup.add(choiceBox1);
        questionGroup.add(choiceBox2);
        questionGroup.add(choiceBox3);
        questionGroup.add(choiceBox4);

        textBox3 = new GraphicsText("You have " + currentScore + " points!", canvas.getWidth()*0.05, canvas.getHeight()*0.05);
        textBox3.setFont("Helvetica",FontStyle.BOLD,25);
        textBox3.setFillColor(Color.BLACK);
        canvas.add(textBox3);

        showScore();
        run();

//        Image background = new comp127graphics.Image(0,0);
//        background.setImagePath("Mac.png");
//        canvas.add(background);
//
    }

    private void createChoiceButton(int choiceIndex, double x, double y) {
        Button button = new Button("Choose " + (char) ('A' + choiceIndex));
        button.onClick(() -> {
            userChoice = choiceIndex;
            ifCorrect();
        });
        button.setPosition(canvas.getWidth() * x,canvas.getHeight() * y);
        questionGroup.add(button);
    }

    public void moveForward(){
        int diceRoll = rand.nextInt(6) + 1;
        numberCounter.setText(diceRoll + " steps");
        if (this.currentBlock + diceRoll < blockManager.getBlockQuantity()){
            this.currentBlock += diceRoll;
            showQuestion();
            if (questionGroup.getCanvas() == null){
                canvas.add(questionGroup);
            }
        } else {
            this.currentBlock = blockManager.getBlockQuantity() - 1;
            this.exceed = true;
        }
        updateBlockColor();
//        ifCorrect();
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

    public void showQuestion(){
        Question thisQuestion = selectQuestion();
        questionBox.setText(thisQuestion.getPrompt());

        String rightAnswer = thisQuestion.getRightAnswer();
        List<String> listOfChoices = thisQuestion.getAllChoices();

        Collections.shuffle(listOfChoices);

        choiceBox1.setText(listOfChoices.get(0));
        choiceBox2.setText(listOfChoices.get(1));
        choiceBox3.setText(listOfChoices.get(2));
        choiceBox4.setText(listOfChoices.get(3));

        currentRightAnswer = listOfChoices.indexOf(rightAnswer);
//        this.currentAnswer = List.of(choiceBox1,choiceBox2,choiceBox3,choiceBox4).get(rightIndex);
    }

    public void ifCorrect(){
        if (userChoice == currentRightAnswer){
            int randomScore = rand.nextInt(4)+8;
            currentScore += randomScore;

            GraphicsText textBox2 = new GraphicsText("CORRECT!", canvas.getWidth()*0.5, canvas.getHeight()*0.52);
            textBox2.setFont("Helvetica",FontStyle.BOLD,30);
            textBox2.setFillColor(Color.PINK);
            canvas.add(textBox2);
            canvas.draw();
            canvas.pause(1000);
            canvas.remove(textBox2);
            canvas.remove(questionGroup);
        } else {
            GraphicsText textBox2 = new GraphicsText("WRONG!", canvas.getWidth()*0.5, canvas.getHeight()*0.52);
            textBox2.setFont("Helvetica",FontStyle.BOLD,30);
            textBox2.setFillColor(Color.BLUE);
            canvas.add(textBox2);
            canvas.draw();
            canvas.pause(1000);
            canvas.remove(textBox2);
        }
        showScore();
    }

    public void showLose(){
        GraphicsText textBox = new GraphicsText("YOU ARE ALMOST THERE!", canvas.getWidth()*0.4, canvas.getHeight()*0.52);
        textBox.setFont("Helvetica",FontStyle.BOLD_ITALIC,40);
        textBox.setFillColor(Color.RED);
        canvas.add(textBox);
        canvas.draw();
    }

    public void showWin(){
        GraphicsText textBox = new GraphicsText("Congratulations!", canvas.getWidth()*0.4, canvas.getHeight()*0.52);
        textBox.setFont("Helvetica",FontStyle.BOLD_ITALIC,40);
        textBox.setFillColor(Color.ORANGE);
        canvas.add(textBox);
        canvas.draw();
    }

    public void run(){
        blockManager.generateBlock();
    }

    public void showScore(){
        textBox3.setText("You have " + currentScore + " points!");
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
