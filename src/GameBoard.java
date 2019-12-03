import comp127graphics.CanvasWindow;
import comp127graphics.GraphicsText;

import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class GameBoard {
    private CanvasWindow canvas;
    private GraphicsText numbercounter;
    private Random rand;
    private List<Map> passbyBoxes;
    private GraphicsText questionBox;
    private GraphicsText choiceBox1;
    private GraphicsText choiceBox2;
    private GraphicsText choiceBox3;
    private GraphicsText choiceBox4;
    private QuestionBank allQuestions;

    public GameBoard(){
        this.canvas = new CanvasWindow("Graduation Game",1000,1000);
        this.numbercounter = new GraphicsText();
        numbercounter.setPosition(canvas.getWidth()*0.9,canvas.getHeight()*0.1);

        allQuestions = new QuestionBank();
    }

    public List<Question> questionModule(String type){
        return QuestionBank.questionList.stream()
                .filter(question -> question.getType().equals(type))
                .collect(toList());
    }

    public static void main(String[] args){
        new GameBoard();
    }
}
