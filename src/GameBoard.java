import comp127graphics.*;
import comp127graphics.Image;
import comp127graphics.ui.Button;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Create canvas windows for the game.
 *
 * @author Yutong Wu
 * @author Zuofu Huang
 */

public class GameBoard {
    private CanvasWindow canvas;
    private BlockManager blockManager;
    private QuestionBank allQuestions;

    private GraphicsText stepsCounter = new GraphicsText();
    private GraphicsText currentScoreBox;
    private int currentBlockNumber = -1;
    private int currentTotalScore = 0;
    private boolean exceed = false;

    private GraphicsGroup questionGroup = new GraphicsGroup();
    private Line questionGroupBoundary;
    private GraphicsText question = new GraphicsText();
    private GraphicsText choice1 = new GraphicsText();
    private GraphicsText choice2 = new GraphicsText();
    private GraphicsText choice3 = new GraphicsText();
    private GraphicsText choice4 = new GraphicsText();

    private int currentRightAnswer;
    private int userChoice;
    private boolean secondChance = false;

    private Random rand = new Random();

    private GameBoard(){
        run();
    }

    /**
     * Constructs the starter interface with welcome, instructions and buttons that allow the user to start the game.
     * On click, the method will call startGameCallBack that starts one game.
     */
    private void run(){
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        canvas.setBackground(new Color(rand.nextInt(200)+55,rand.nextInt(150)+105,rand.nextInt(200)+55));
        setBackgroundPicture(canvas, "smallLogo.png");

        GraphicsText introduction = new GraphicsText("MATH & CHEM TRIVIA",
                canvas.getWidth()*0.35,canvas.getHeight()*0.45);
        introduction.setFont(Font.SANS_SERIF, FontStyle.BOLD, 26);
        canvas.add(introduction);

        GraphicsText title = new GraphicsText("Can you graduate from MSCS and Chemistry?",
                canvas.getWidth()*0.14,canvas.getHeight()*0.3);
        title.setFont(Font.SANS_SERIF, FontStyle.BOLD, 34);
        canvas.add(title);

        Button startGame = new Button("I Wish to Start!");
        startGame.setCenter(canvas.getWidth()*0.5, canvas.getHeight()*0.7);
        canvas.add(startGame);

        Button helpButton = new Button("Need Help?");
        helpButton.setCenter(canvas.getWidth() * 0.5, canvas.getHeight() * 0.6);
        canvas.add(helpButton);

        helpButton.onClick(()->{
            CanvasWindow helpPage = new CanvasWindow("Instructions", 310,300);
            GraphicsGroup instructionBoxes = new GraphicsGroup();
            helpPage.add(instructionBoxes);
            helpPage.setBackground(new Color(90,180,53));

            createInstructionLine(helpPage, instructionBoxes, 0.2, 0.2, "Welcome to Graduation Game!",12,Color.black);
            createInstructionLine(helpPage, instructionBoxes, 0.05, 0.3, "Click on 'move forward' to move with random",12,Color.black);
            createInstructionLine(helpPage, instructionBoxes, 0.05, 0.4, "steps forward, and try your best to answer",12,Color.black);
            createInstructionLine(helpPage, instructionBoxes, 0.05, 0.5, "questions to receive points. If your points >=",12,Color.black);
            createInstructionLine(helpPage, instructionBoxes, 0.05, 0.6, "60 points before reaching the end block, then",12,Color.black);
            createInstructionLine(helpPage, instructionBoxes,0.1,0.78,"CONGRATULATIONS!",24,Color.orange);

            Button exit = new Button("return");
            exit.onClick(helpPage::closeWindow);
            helpPage.add(exit);
            exit.setPosition(helpPage.getWidth() * 0.4, helpPage.getHeight() * 0.9);
        });

        startGame.onClick(() ->{
            canvas.remove(startGame);
            canvas.remove(helpButton);
            startGameCallback();

            canvas.animate(this::determineFinalResult);
        });
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
     * Set the graphic box to show the choices for each question
     * @param box the graphic box used to contain choices and the question stem
     * @param x the x coordinate of the graphic box
     * @param y the y coordinate of the graphic box
     * @param fontSize the font size of the strings used for illustrating choices
     */
    private void styleQuestionGroupBox(GraphicsText box, double x, double y, int fontSize) {
        box.setCenter(canvas.getWidth() * x,canvas.getHeight() * y);
        box.setFont("Helvetica", FontStyle.BOLD, fontSize);
        box.setFillColor(Color.WHITE);
        questionGroup.add(box);
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
     * Moves forward the user once per dice roll and update the block color with that move forward.
     * Show the question if the user hasn't reached the finish line.
     */
    private void moveForward(){
        int diceRoll = rand.nextInt(6) + 1;
        stepsCounter.setText(diceRoll + " steps");
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

    /**
     * Change the color of the blocks that the user passed by.
     */
    private void updateBlockColor(){
        for(Block block: blockManager.getPassedBlocks(currentBlockNumber)){
            block.setActive(true);
        }
    }

    /**
     * Display the question and its choices on the canvas in random order.
     */
    private void showQuestion(){
        Question thisQuestion = allQuestions.selectQuestion(blockManager, currentBlockNumber);
        question.setText(thisQuestion.getPrompt());
        String rightAnswer = thisQuestion.getRightAnswer();
        List<String> listOfChoices = thisQuestion.getAllChoices();

        Collections.shuffle(listOfChoices);
        choice1.setText(listOfChoices.get(0));
        choice2.setText(listOfChoices.get(1));
        choice3.setText(listOfChoices.get(2));
        choice4.setText(listOfChoices.get(3));
        currentRightAnswer = listOfChoices.indexOf(rightAnswer);

        canvas.add(questionGroupBoundary);
    }

    /**
     * Check if users make the right choice. If correct, the questions and choices will be removed from canvas.
     * if it is incorrect, the user will have another attempt (and only one) to retry the question. They will receive
     * half of what the question is worth if they are correct the second time.
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
                secondChance = false;
            } else {
                secondChance = true;
            }
        }
        updateScore();
    }

    /**
     * Show whether the user make the right or wrong choices in text and display it on the canvas
     * @param text the text used to described the result
     * @param color the color of the text
     */
    private void giveResultInText(String text, Color color) {
        GraphicsText textBox = new GraphicsText(text, canvas.getWidth()*0.46, canvas.getHeight()*0.535);
        textBox.setFont("Helvetica", FontStyle.BOLD,30);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
        canvas.pause(1000);
        canvas.remove(textBox);
    }

    /**
     * Display the points the user gets from the current question they got correct.
     * @param point amount of points the user gets
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

    /**
     * Construct green checks or red crosses on the map to indicate if the user gets the question right/wrong.
     * Use two line objects from GraphicsObject.
     */
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

    /**
     * Determines if the user has won. If the user has exceeded the finish line with less than 60 points, then lose.
     */
    private void determineFinalResult() {
        if (currentTotalScore < 60 && exceed){
            showFinalResult("YOU ARE ALMOST THERE!", Color.RED);
            restart();
        } else if (currentTotalScore >= 60 && exceed){
            showFinalResult("Congratulations!", Color.ORANGE);
            restart();
        }
    }

    /**
     * Create a GraphicsText object to indicate whether the user won the game after they have surpassed the finish line.
     */
    private void showFinalResult(String s, Color color) {
        GraphicsText textBox = new GraphicsText(s);
        textBox.setFont("Helvetica", FontStyle.BOLD_ITALIC, 40);
        textBox.setCenter(canvas.getWidth() * 0.5, canvas.getHeight() * 0.565);
        textBox.setFillColor(color);
        canvas.add(textBox);
        canvas.draw();
    }

    /**
     * Update the current score after each question is answered correctly, at the very top left of the canvas.
     */
    private void updateScore(){
        currentScoreBox.setText("You have " + currentTotalScore + " points!");
    }

    /**
     * Constructs elements on the main game canvas, including the map, dice buttons and question boxes.
     */
    private void startGameCallback(){
        setBackgroundPicture(canvas, "MacShade.png");

        stepsCounter.setPosition(canvas.getWidth()*0.89,canvas.getHeight()*0.12);
        stepsCounter.setFont("Helvetica",FontStyle.BOLD,22);
        canvas.add(stepsCounter);

        blockManager = new BlockManager(this.canvas);

        Button dice = new Button("Move forward");
        dice.onClick(this::moveForward);
        dice.setPosition(canvas.getWidth()*0.865,canvas.getHeight()*0.15);
        canvas.add(dice);

        createChoiceButton(0,0.14, 0.725);
        createChoiceButton(1,0.54, 0.725);
        createChoiceButton(2,0.14, 0.825);
        createChoiceButton(3,0.54, 0.825);

        allQuestions = new QuestionBank();
        styleQuestionGroupBox(choice1,0.25,0.75,18);
        styleQuestionGroupBox(choice2,0.65,0.75,18);
        styleQuestionGroupBox(choice3,0.25,0.85,18);
        styleQuestionGroupBox(choice4,0.65,0.85,18);
        styleQuestionGroupBox(question,0.25,0.65,20);

        currentScoreBox = new GraphicsText("You have " + currentTotalScore + " points!",
                canvas.getWidth()*0.05, canvas.getHeight()*0.05);
        currentScoreBox.setFont("Helvetica",FontStyle.BOLD,25);
        currentScoreBox.setFillColor(new Color(253,174,21));
        canvas.add(currentScoreBox);

        questionGroupBoundary = new Line(canvas.getWidth()*0.05,canvas.getHeight()*0.6,
                canvas.getWidth()*0.95,canvas.getHeight()*0.6);
        questionGroupBoundary.setStrokeColor(Color.GRAY);
        questionGroupBoundary.setStrokeWidth(5);

        updateScore();
        blockManager.generateBlock();
    }

    /**
     * Restart the game after the use has finished one. Remove all question-related objects, if applicable,
     * before adding the restart button.
     */
    private void restart(){
        Button restart = new Button("restart");
        restart.setCenter(canvas.getWidth()*0.5,canvas.getHeight()*0.8);
        canvas.add(restart);

        try {
            canvas.remove(questionGroup);
        } catch (Exception NoSuchElementException){
            return;
        }

        restart.onClick(()->{
            new GameBoard(); // canvas.removeAll(); after the mechanism of canvasWindow is changed.
            canvas.closeWindow(); // run();
        });
    }

    /**
     * Helper function used to regulate the instruction lines display in the pops up after clicking on the "help" button
     * @param helpPage the page pops up after clicking on the "help" button
     * @param instructionBoxes the graphic groups consists of labels that display the instruction
     * @param x the proportion of width of help page used to determine the x coordinate of the GraphicText
     * @param y the proportion of height of help page used to determine the x coordinate of the GraphicText
     * @param content the instruction lines
     * @param font the font of the text of instruction lines
     * @param color the color of the text of the instruction lines
     */
    private void createInstructionLine(CanvasWindow helpPage, GraphicsGroup instructionBoxes, double x, double y,
                                       String content, int font, Color color) {
        GraphicsText instructionLine = new GraphicsText();
        instructionLine.setPosition(helpPage.getWidth() * x, helpPage.getHeight() * y);
        instructionLine.setText(content);
        instructionLine.setFont(Font.DIALOG, FontStyle.BOLD, font);
        instructionLine.setFillColor(color);
        instructionBoxes.add(instructionLine);
    }

    public static void main(String[] args){
        new GameBoard();
    }
}