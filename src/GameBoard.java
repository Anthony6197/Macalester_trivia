import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;
import comp127graphics.ui.Button;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numberCounter;
    private Random rand;
    private MapManager mapManager;
    private Button shaffle;
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

        mapManager = new MapManager(this.canvas);

        shaffle = new Button("Click");
        rand = new Random();
        shaffle.onClick(this::getSteps);
        shaffle.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.11);
        canvas.add(shaffle);

        allQuestions = new QuestionBank();

        run();
    }

    public void getSteps(){
        int steps = rand.nextInt(7);
        numberCounter.setText(steps + "steps");
        this.steps = steps;
        setColor();
    }

    public void setColor(){
        for(Map box:mapManager.getPassedBoxes(steps)){
            box.setActive(true);
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

    public void run(){
        mapManager.generateMapbox();
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
