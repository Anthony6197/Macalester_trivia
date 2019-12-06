import java.util.List;
import java.util.Objects;

public class Question{
    private String prompt;
    private List<String> choices;
    private String type;

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

    public List<String> getAllChoices(){
        return List.copyOf(choices);
    }
}


