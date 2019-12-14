import comp127graphics.*;
import comp127graphics.Rectangle;
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
    private int currentBlock = -1;
    private int currentScore = 0;
    private boolean exceed = false;

    private GraphicsGroup questionGroup;
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

        currentScoreBox = new GraphicsText("You have " + currentScore + " points!", canvas.getWidth()*0.05, canvas.getHeight()*0.05);
        currentScoreBox.setFont("Helvetica",FontStyle.BOLD,25);
        currentScoreBox.setFillColor(Color.BLACK);
        canvas.add(currentScoreBox);

//        Image background = new comp127graphics.Image(0,0);
//        background.setImagePath("Mac.png");
//        canvas.add(background);
//        canvas.drawImage

        showScore();
        run();
    }

//    private void rec(){
//        Rectangle rec = new Rectangle();
//    }

    private void styleQuestionGroupBox(GraphicsText choiceBox, double x, double y, int fontSize) {
        choiceBox.setCenter(canvas.getWidth() * x,canvas.getHeight() * y);
        choiceBox.setFont("Helvetica", FontStyle.PLAIN,fontSize);
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
    }

    private void updateBlockColor(){
        for(Block block: blockManager.getPassedBlocks(currentBlock)){
            block.setActive(true);
        }

    }
    private List<Question> createQuestionList(String type){
        return allQuestions.findAllQuestionsOfType(type);
    }

    private Question selectQuestion(){
        String type = blockManager.getBlock(currentBlock).getType();
        List<Question> questionList = createQuestionList(type);
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
    }

    private void ifCorrect(){
        int score = rand.nextInt(4)+8;
        if (userChoice == currentRightAnswer){
            giveResult("Correct!",Color.pink);
            if (secondChance){
                currentScore += score / 2;
            } else{
                currentScore += score;
            }
            canvas.remove(questionGroup);
            secondChance = false;
        } else {
            giveResult("Wrong!",Color.blue);
            if(secondChance){
                canvas.remove(questionGroup);
            } else {
                secondChance = true;
            }
        }
        showScore();
    }

    private void giveResult(String text, Color color) {
        GraphicsText textBox = new GraphicsText(text, canvas.getWidth()*0.5, canvas.getHeight()*0.535);
        textBox.setFont("Helvetica", FontStyle.BOLD,30);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
        canvas.pause(1000);
        canvas.remove(textBox);
    }

    private void showLose(){
        GraphicsText textBox = new GraphicsText("YOU ARE ALMOST THERE!", canvas.getWidth()*0.4, canvas.getHeight()*0.52);
        textBox.setFont("Helvetica",FontStyle.BOLD_ITALIC,40);
        textBox.setFillColor(Color.RED);
        canvas.add(textBox);
        canvas.draw();
    }

    private void showWin(){
        GraphicsText textBox = new GraphicsText("Congratulations!", canvas.getWidth()*0.4, canvas.getHeight()*0.52);
        textBox.setFont("Helvetica",FontStyle.BOLD_ITALIC,40);
        textBox.setFillColor(Color.ORANGE);
        canvas.add(textBox);
        canvas.draw();
    }

    private void run(){
        blockManager.generateBlock();
    }

    private void showScore(){
        currentScoreBox.setText("You have " + currentScore + " points!");
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
