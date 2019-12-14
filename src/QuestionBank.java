import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionBank {
    private static final List<Question> masterQuestionList = List.of(
                new Question("Math", "What is a commonly used p-value threshold?", List.of("0.05", "0.1", "0.01", "What are you talking about?")),
                new Question("Math", "What class is Math 236?", List.of("Linear Algebra", "Calculus 3", "Capstone", "Calc 2")),
                new Question("Math", "What corresponds to Binomial distribution?", List.of("Bin(n,p)", "Bern(p)", "Beta(a,b)", "Pois(lambda)")),
                new Question("Math", "What have to be true for f to be Riemann integrable?", List.of("U(f) = L(f)", "L(P) = A(P)", "K(a) = K(b)", "A=B=C=D")),
                new Question("Math", "What's # of Federal Rules of Evidence for relevance? ", List.of("399+2", "444-40", "10+43", "99*4")),
                new Question("Math", "What is a supremum of [0,4]?", List.of("4", "5", "0", "NA")),
                new Question("Math", "What is 2^3/(3^10) bigger than", List.of("0", "1", "2", "3")),
                new Question("Math", "What is contrary to frequentist in statistics?", List.of("Bayesian", "Lagrangian", "Murphy", "Dimensional")),
                new Question("Math", "What is CLA in MSCS?", List.of("Computational Linear Algebra", "Contrary Linear Application", "Computation Linear Application", "Comparative List Analysis")),
                new Question("Math", "How many iMacs does Room 256 have?", List.of("10", "12", "9", "8")),
                new Question("Math", "How many session of Intro to Stats do MSCS offer in Spring 2020?", List.of("4", "2", "3", "6")),
                new Question("Math", "What is equivalent to a set being compact?", List.of("Closed and bounded", "Open and closed", "open and bounded", "None of the others")),
                new Question("Math", "What does MLE stand for?", List.of("Maximum Likelihood Estimation", "Marshmallow Like Estimation", "Mathematical Likelihood Estimation", "Moment Likelihood Estimation")),
                new Question("Math", "What is a prime number?", List.of("1001", "1003", "1005", "1008")),
                new Question("Math", "How do we represent the square root of -1", List.of("i", "k", "j", "There's no such thing!")),
                new Question("Math", "What is not a valid p-value?", List.of("1.5", "0.5", "0.00001", "0.05")),
                new Question("Chem", "What is a the most oxidizing element?", List.of("O", "Cl", "F", "N")),
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
