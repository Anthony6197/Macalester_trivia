import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numberCounter;
    private Random rand;
    private MapManager mapManager;
    private List<Map> passbyBoxes;
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

        rand = new Random();
        int steps = rand.nextInt(6);
        numberCounter.setText(steps+"steps");
        canvas.add(numberCounter);

        allQuestions = new QuestionBank();

        run();
    }

    public List<Question> createQuestionList(String type){
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
