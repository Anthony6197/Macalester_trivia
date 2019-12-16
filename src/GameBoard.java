
import comp127graphics.*;
import comp127graphics.Image;
import comp127graphics.ui.Button;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Author: Yutong Wu and Zuofu Huang
 *
 */
public class GameBoard {
    private CanvasWindow canvas;

    private BlockManager blockManager;
    private QuestionBank allQuestions;

    private GraphicsText numberCounter = new GraphicsText();
    private GraphicsText currentScoreBox;
    private int currentBlockNumber = -1;
    private int currentTotalScore = 0;
    private boolean exceed = false;

    private GraphicsGroup questionGroup;
    private Line questionGroupBoundary;
    private GraphicsText questionBox = new GraphicsText();
    private GraphicsText choiceBox1 = new GraphicsText();
    private GraphicsText choiceBox2 = new GraphicsText();
    private GraphicsText choiceBox3 = new GraphicsText();
    private GraphicsText choiceBox4 = new GraphicsText();

    private int currentRightAnswer;
    private int userChoice;
    private boolean secondChance = false;

    private Random rand;


    public GameBoard(){
        run();
    }

    /**
     * Set the background image of a canvas
     * @param canvas the canvas need to be set
     * @param path the file path of the background image
     */
    private void setBackgroundPicture(CanvasWindow canvas, String path) {
        Image background = new Image(0,0);
        background.setImagePath(path);
        canvas.add(background);
        canvas.draw();
    }

    /**
     * Set the grapoic box to show the choices for each question
     * @param choiceBox the graphic box used to contain choices
     * @param x the x coordinate of the graphic box
     * @param y the y coordinate of the graphic box
     * @param fontSize the font size of the strings used for illustrating choices
     */
    private void styleQuestionGroupBox(GraphicsText choiceBox, double x, double y, int fontSize) {
        choiceBox.setCenter(canvas.getWidth() * x,canvas.getHeight() * y);
        choiceBox.setFont("Helvetica", FontStyle.BOLD, fontSize);
        choiceBox.setFillColor(Color.WHITE);
        questionGroup.add(choiceBox);
    }

    /**
     * Set the choice button for user to push to answer the multiple choice questions
     * @param choiceIndex the index of choices
     * @param x the x coordinate of the choice button
     * @param y the y coordinate of the choice button
     */
    private void createChoiceButton(int choiceIndex, double x, double y) {
        Button button = new Button("Choose " + (char) ('A' + choiceIndex));
        button.onClick(() -> {
            userChoice = choiceIndex;
            ifCorrect();
        });
        button.setPosition(canvas.getWidth() * x,canvas.getHeight() * y);
        questionGroup.add(button);
    }

    /**
     * Create the button for user to push to move forwardly with random steps
     *
     */
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
        if (currentTotalScore < 60 && exceed){
            restart();
        }
    }

    /**
     * Change the color of the blocks passed by
     */
    private void updateBlockColor(){
        for(Block block: blockManager.getPassedBlocks(currentBlockNumber)){
            block.setActive(true);
        }

    }

    /**
     * Create a list of questions given the types of the blocks
     * @param type the type of blocks (either "math" or "chem")
     * @return return a list of questions corresponding to blocks
     */
    private List<Question> createQuestionList(String type){
        return allQuestions.findAllQuestionsOfType(type);
    }

    /**
     * Select a question from the question list created by creatQuestionList
     * @return the list of questions with the chosen questions deleted from it
     */
    private Question selectQuestion(){
        String type = blockManager.getBlock(currentBlockNumber).getType();
        List<Question> questionList = createQuestionList(type);
        int randomNumber = rand.nextInt(questionList.size());
        return allQuestions.deleteQuestion(randomNumber);
    }

    /**
     * Display the question and its choices on the canvas
     */
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

    /**
     * Check if users make the right choice. If correct, the questions and choices will be removed from canvas.
     * if it is incorrect, the user will have another attempt allowed to recover it for the first wrong question and only one
     * attempt allowed for the other questions.
     */
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

    /**
     * Show whether the user make the right or wrong choices in text and display it on the canvas
     * @param text the text used to described the result
     * @param color the color of the text
     */
    private void giveResultInText(String text, Color color) {
        GraphicsText textBox = new GraphicsText(text, canvas.getWidth()*0.5, canvas.getHeight()*0.535);
        textBox.setFont("Helvetica", FontStyle.BOLD,30);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
        canvas.pause(1000);
        canvas.remove(textBox);
    }

    /**
     * Display the number of users get from the current correct question they
     * @param point scores the user can get
     */
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
        GraphicsText textBox = new GraphicsText(s, canvas.getWidth() * 0.3, canvas.getHeight() * 0.58);
        textBox.setFont("Helvetica", FontStyle.BOLD_ITALIC, 40);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
    }


    private void showScore(){
        currentScoreBox.setText("You have " + currentTotalScore + " points!");
    }

    public void startGameCallback(){
        setBackgroundPicture(canvas,"MacShade.png");

        numberCounter.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.11);
        numberCounter.setFont("Helvetica",FontStyle.BOLD,18);
        canvas.add(numberCounter);

        blockManager = new BlockManager(this.canvas);

        Button dice = new Button("Move forward");
        dice.onClick(this::moveForward);
        dice.setPosition(canvas.getWidth()*0.865,canvas.getHeight()*0.15);
        canvas.add(dice);

        this.questionGroup = new GraphicsGroup();

        createChoiceButton(0,0.14, 0.725);
        createChoiceButton(1,0.54, 0.725);
        createChoiceButton(2,0.14, 0.825);
        createChoiceButton(3,0.54, 0.825);

        allQuestions = new QuestionBank();
        styleQuestionGroupBox(choiceBox1,0.25,0.75,18);
        styleQuestionGroupBox(choiceBox2,0.65,0.75,18);
        styleQuestionGroupBox(choiceBox3,0.25,0.85,18);
        styleQuestionGroupBox(choiceBox4,0.65,0.85,18);
        styleQuestionGroupBox(questionBox,0.25,0.65,20);

        currentScoreBox = new GraphicsText("You have " + currentTotalScore + " points!", canvas.getWidth()*0.05, canvas.getHeight()*0.05);
        currentScoreBox.setFont("Helvetica",FontStyle.BOLD,25);
        currentScoreBox.setFillColor(Color.BLACK);
        canvas.add(currentScoreBox);

        questionGroupBoundary = new Line(canvas.getWidth()*0.05,canvas.getHeight()*0.6,canvas.getWidth()*0.95,canvas.getHeight()*0.6);
        questionGroupBoundary.setStrokeColor(Color.GRAY);
        questionGroupBoundary.setStrokeWidth(5);

        showScore();
        blockManager.generateBlock();
    }

    public void restart(){
        Button restart = new Button("restart");
        restart.setPosition(canvas.getWidth()*0.4,canvas.getHeight()*0.8);
        canvas.add(restart);
        restart.onClick(()->{
            canvas.closeWindow();
            new GameBoard();
        });
    }

    public void run(){
        this.rand = new Random();
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        canvas.setBackground(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
        setBackgroundPicture(canvas,"smallLogo.png");

        GraphicsText introduction = new GraphicsText("MATH & CHEM TRIVIA",canvas.getWidth()*0.4,canvas.getHeight()*0.5);
        introduction.setFont(Font.SANS_SERIF, FontStyle.PLAIN, 18);
        canvas.add(introduction);

        GraphicsText title = new GraphicsText("Can you graduate from MSCS and Chemistry?",canvas.getWidth()*0.2,canvas.getHeight()*0.3);
        title.setFont(Font.SANS_SERIF, FontStyle.BOLD, 34);
        title.setFillColor(new Color(239,79,38));
        canvas.add(title);
        Button startGame = new Button("I Wish to Start!");

        Button help = new Button("Need help?");
        help.onClick(()->{
            CanvasWindow helpPage = new CanvasWindow("help", 300,300);
            GraphicsGroup instructionBoxes = new GraphicsGroup();
            helpPage.setBackground(Color.CYAN);

            GraphicsText instructionl0 = new GraphicsText();
            instructionl0.setPosition(helpPage.getWidth() * 0.2, helpPage.getHeight() * 0.3);
            instructionl0.setText("Welcome to Graduation Game!");
            instructionl0.setFont(Font.DIALOG ,FontStyle.BOLD, 12);
            instructionBoxes.add(instructionl0);

            GraphicsText instructionl1 = new GraphicsText();
            instructionl1.setPosition(helpPage.getWidth() * 0.05, helpPage.getHeight() * 0.4);
            instructionl1.setText("Click on 'move forward' to move with random");
            instructionl1.setFont(Font.DIALOG ,FontStyle.BOLD, 12);
            instructionBoxes.add(instructionl1);

            GraphicsText instructionl2 = new GraphicsText();
            instructionl2.setPosition(helpPage.getWidth() * 0.05, helpPage.getHeight() * 0.5);
            instructionl2.setText("steps forward, and try your best to answer");
            instructionl2.setFont(Font.DIALOG ,FontStyle.BOLD, 12);
            instructionBoxes.add(instructionl2);

            GraphicsText instructionl3 = new GraphicsText();
            instructionl3.setPosition(helpPage.getWidth() * 0.05, helpPage.getHeight() * 0.6);
            instructionl3.setText("questions to receive points. If your points >=");
            instructionl3.setFont(Font.DIALOG ,FontStyle.BOLD, 12);
            instructionBoxes.add(instructionl3);

            GraphicsText instructionl4 = new GraphicsText();
            instructionl4.setPosition(helpPage.getWidth() * 0.05, helpPage.getHeight() * 0.7);
            instructionl4.setText("60 points before reaching the end block, then");
            instructionl4.setFont(Font.DIALOG ,FontStyle.BOLD, 12);
            instructionBoxes.add(instructionl4);

            GraphicsText instructionl5 = new GraphicsText();
            instructionl5.setPosition(helpPage.getWidth() * 0.05, helpPage.getHeight() * 0.8);
            instructionl5.setText("CONGRATULATIONS!");
            instructionl5.setFont(Font.DIALOG ,FontStyle.BOLD, 24);
            instructionl5.setFillColor(Color.ORANGE);
            instructionBoxes.add(instructionl5);

            helpPage.add(instructionBoxes);

            Button exit = new Button("return");
            exit.onClick(()->helpPage.closeWindow());
            helpPage.add(exit);
            exit.setPosition(helpPage.getWidth() * 0.05, helpPage.getHeight() * 0.9);

        });
        startGame.onClick(() ->{
                    canvas.remove(startGame);
                    canvas.remove(help);
                    startGameCallback();
                    canvas.animate(() ->{
                        if (currentTotalScore < 60 && exceed){
                            showFinalResult("YOU ARE ALMOST THERE!", Color.RED);
                        } else if (currentTotalScore >= 60 && exceed){
                            showFinalResult("Congratulations!", Color.ORANGE);
                        }
                    });

                }
        );
        help.setPosition(canvas.getWidth() * 0.45, canvas.getHeight() * 0.70);
        canvas.add(help);

        startGame.setPosition(canvas.getWidth()*0.45, canvas.getHeight()*0.75);
        canvas.add(startGame);
    }

    public static void main(String[] args){
        new GameBoard();
    }

}
//Add a note about another change: A unfinished restart button will show up after player lose nad the
//starter page will show up with random color. (temporarily fixed the restart button)