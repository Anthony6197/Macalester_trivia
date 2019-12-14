import comp127graphics.*;
import comp127graphics.Image;
import comp127graphics.ui.Button;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private CanvasWindow canvas;
    private BlockManager blockManager;
    private QuestionBank allQuestions;

    private GraphicsText numberCounter;
    private GraphicsText currentScoreBox;
    private int currentBlockNumber = -1;
    private int currentTotalScore = 0;
    private boolean exceed = false;

    private GraphicsGroup questionGroup;
    private Line questionGroupBoundary;
    private GraphicsText questionBox;
    private GraphicsText choiceBox1;
    private GraphicsText choiceBox2;
    private GraphicsText choiceBox3;
    private GraphicsText choiceBox4;

    private int currentRightAnswer;
    private int userChoice;
    private boolean secondChance = false;

    private Random rand;

    public GameBoard(){
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        this.numberCounter = new GraphicsText();
        numberCounter.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.1);
        numberCounter.setFont("Helvetica",FontStyle.BOLD,18);
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
            if (currentTotalScore < 60 && exceed){
                showFinalResult("YOU ARE ALMOST THERE!", Color.RED);
            } else if (currentTotalScore >= 60 && exceed){
                showFinalResult("Congratulations!", Color.ORANGE);
            }
        });

        allQuestions = new QuestionBank();

        choiceBox1 = new GraphicsText();
        styleQuestionGroupBox(choiceBox1,0.25,0.75,18);
        choiceBox2 = new GraphicsText();
        styleQuestionGroupBox(choiceBox2,0.65,0.75,18);
        choiceBox3 = new GraphicsText();
        styleQuestionGroupBox(choiceBox3,0.25,0.85,18);
        choiceBox4 = new GraphicsText();
        styleQuestionGroupBox(choiceBox4,0.65,0.85,18);
        questionBox = new GraphicsText();
        styleQuestionGroupBox(questionBox,0.25,0.65,20);

        currentScoreBox = new GraphicsText("You have " + currentTotalScore + " points!", canvas.getWidth()*0.05, canvas.getHeight()*0.05);
        currentScoreBox.setFont("Helvetica",FontStyle.BOLD,25);
        currentScoreBox.setFillColor(Color.BLACK);
        canvas.add(currentScoreBox);

        questionGroupBoundary = new Line(canvas.getWidth()*0.05,canvas.getHeight()*0.6,canvas.getWidth()*0.95,canvas.getHeight()*0.6);
        questionGroupBoundary.setStrokeColor(Color.BLACK);
        questionGroupBoundary.setStrokeWidth(5);

        Image background = new comp127graphics.Image(0,0);
        background.setImagePath("Mac.png");
        canvas.add(background);
        canvas.draw();

        showScore();
        run();
    }

    private void styleQuestionGroupBox(GraphicsText choiceBox, double x, double y, int fontSize) {
        choiceBox.setCenter(canvas.getWidth() * x,canvas.getHeight() * y);
        choiceBox.setFont("Helvetica", FontStyle.BOLD, fontSize);
        choiceBox.setFillColor(Color.WHITE);
        questionGroup.add(choiceBox);
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

    private void moveForward(){
        int diceRoll = rand.nextInt(6) + 1;
        numberCounter.setText(diceRoll + " steps");
        if (this.currentBlockNumber + diceRoll < blockManager.getBlockQuantity()){
            this.currentBlockNumber += diceRoll;
            showQuestion();
            if (questionGroup.getCanvas() == null){
                canvas.add(questionGroup);
            }
        } else {
            this.currentBlockNumber = blockManager.getBlockQuantity() - 1;
            this.exceed = true;
        }
        updateBlockColor();
    }

    private void updateBlockColor(){
        for(Block block: blockManager.getPassedBlocks(currentBlockNumber)){
            block.setActive(true);
        }

    }
    private List<Question> createQuestionList(String type){
        return allQuestions.findAllQuestionsOfType(type);
    }

    private Question selectQuestion(){
        String type = blockManager.getBlock(currentBlockNumber).getType();
        List<Question> questionList = createQuestionList(type);
        System.out.println(questionList.size());
        int randomNumber = rand.nextInt(questionList.size());
        return allQuestions.deleteQuestion(randomNumber);
    }

    private void showQuestion(){
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

        canvas.add(questionGroupBoundary);
    }

    private void ifCorrect(){
        int score = rand.nextInt(4) + 8;
        if (userChoice == currentRightAnswer){
            giveResultInText("Correct!",Color.green);
            if (secondChance){
                currentTotalScore += score / 2;
                giveScoreOnMap(score / 2);
            } else{
                currentTotalScore += score;
                giveScoreOnMap(score);
            }
            canvas.remove(questionGroup);
            canvas.remove(questionGroupBoundary);
            secondChance = false;
            markResultOnMap(15, 30, 25, 40, Color.green, 27, 38, 47, 18);
        } else {
            giveResultInText("Wrong!",Color.red);
            if(secondChance){
                markResultOnMap(10, 10, 45, 45, Color.red, 45, 10, 10, 45);
                canvas.remove(questionGroup);
                canvas.remove(questionGroupBoundary);
            } else {
                secondChance = true;
            }
        }
        showScore();
    }

    private void giveResultInText(String text, Color color) {
        GraphicsText textBox = new GraphicsText(text, canvas.getWidth()*0.5, canvas.getHeight()*0.535);
        textBox.setFont("Helvetica", FontStyle.BOLD,30);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
        canvas.pause(1000);
        canvas.remove(textBox);
    }

    private void giveScoreOnMap(int point){
        Block currentBlock = blockManager.getBlock(currentBlockNumber);
        GraphicsText textBox = new GraphicsText("+" + point, currentBlock.getX()+6, currentBlock.getY()+40);
        textBox.setFont("Helvetica", FontStyle.BOLD,30);
        textBox.setFillColor(Color.green);
        canvas.add(textBox);
        canvas.draw();
        canvas.pause(1000);
        canvas.remove(textBox);
    }

    private void markResultOnMap(int i, int i2, int i3, int i4, Color color, int i5, int i6, int i7, int i8) {
        Block currentBlock = blockManager.getBlock(currentBlockNumber);
        Line line1 = new Line(currentBlock.getX() + i, currentBlock.getY() + i2, currentBlock.getX() + i3, currentBlock.getY() + i4);
        line1.setStrokeColor(color);
        line1.setStrokeWidth(7);
        Line line2 = new Line(currentBlock.getX() + i5, currentBlock.getY() + i6, currentBlock.getX() + i7, currentBlock.getY() + i8);
        line2.setStrokeColor(color);
        line2.setStrokeWidth(7);
        canvas.add(line1);
        canvas.add(line2);
        canvas.draw();
    }

    private void showFinalResult(String s, Color color) {
        GraphicsText textBox = new GraphicsText(s, canvas.getWidth() * 0.3, canvas.getHeight() * 0.55);
        textBox.setFont("Helvetica", FontStyle.BOLD_ITALIC, 40);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
    }

    private void run(){
        blockManager.generateBlock();
    }

    private void showScore(){
        currentScoreBox.setText("You have " + currentTotalScore + " points!");
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
