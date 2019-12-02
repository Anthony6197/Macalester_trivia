import java.util.List;

public class Question{
    private String question;
    private List<String> choices;
    private String type;

    public Question(String myType,String question, List<String> myChoices) {
        this.type = myType;
        this.question = question;
        this.choices = myChoices;
    }
}


