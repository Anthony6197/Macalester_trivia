import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionBank {
    private ArrayList<Question> copyOfBank = new ArrayList<>();
    private static List<Question> questionList = List.of(
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Math", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D")),
                new Question("Chem", "What is a commonly used p-value threshold?", List.of("A", "B", "C", "D"))
        );

    public QuestionBank(){
        addQuestion();
    }

    private void addQuestion(){
        copyOfBank.addAll(questionList);
    }

    public Question deleteQuestion(int index){
        return copyOfBank.remove(index);
    }

    public List<Question> findAllQuestionsOfType(String type){
        return copyOfBank.stream()
                .filter(question -> question.getType().equals(type))
                .collect(toList());
    }
}
