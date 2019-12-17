import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Zuofu Huang
 *
 * Math and Chemistry questions
 */
public class Question{
    private String prompt;
    private final List<String> choices;
    private String type;

    /**
     *  Constructor of a question class. Each question has a type (e.g. chem or math), a question body and four choices.
     */
    public Question(String myType,String question, List<String> myChoices) {
        this.type = Objects.requireNonNull(myType);
        this.prompt = Objects.requireNonNull(question);
        this.choices = Objects.requireNonNull(myChoices);
    }


    public String getType(){
        return type;
    }

    public String getPrompt(){
        return prompt;
    }

    public String getRightAnswer(){
        return choices.get(0);
    }

    /**
     * Return a mutable array list of all choices.
     * @return
     */
    public List<String> getAllChoices(){
        return new ArrayList<>(choices);
    }

    public static void changeX(int x){
        x ++;
        System.out.println(x);
    }

    public static void main(String[] args) {
        int i = 0;
        changeX(i);
        System.out.println(i);
    }
}


