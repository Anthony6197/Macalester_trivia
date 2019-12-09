import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionBank {
    private static final List<Question> masterQuestionList = List.of(
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
    private List<Question> availableQuestions = new ArrayList<>(masterQuestionList);

    public Question deleteQuestion(int index){
        return availableQuestions.remove(index);
    }

    public List<Question> findAllQuestionsOfType(String type){
        return availableQuestions.stream()
                .filter(question -> question.getType().equals(type))
                .collect(toList());
    }
}
