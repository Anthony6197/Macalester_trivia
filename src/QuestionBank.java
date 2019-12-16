import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

/**
 * * A master list of 32 questions that will randomly appear in the trivia.
 * The first in the masterQuestionList is the correct answer.
 */
public class QuestionBank {
    private static final List<Question> masterQuestionList = List.of(
            new Question("Math", "What is a commonly used p-value threshold?", List.of("0.05", "0.1", "0.01", "What are you talking about?")),
            new Question("Math", "What class is Math 236?", List.of("Linear Algebra", "Calculus 3", "Capstone", "Calc 2")),
            new Question("Chem", "What is a the most oxidizing element?", List.of("F", "Cl", "O", "N")),
            new Question("Chem", "Which is the most abundant element in your body?", List.of("C", "Fe", "O", "N")),
            new Question("Chem", "Who discover oxygen in the air?", List.of("de Lavoisier", "Le Chatelier", "Robert Boyle", "Amedeo Avogadro")),
            new Question("Chem", "How CO kills people?", List.of("coordination bonding", "covalent bonding", "LDF", "Hydrogen bonding")),
            new Question("Chem", "Select the compound that can be used to clean water", List.of("ClO", "HCN", "CO", "HF")),
            new Question("Chem", "What is a Lewis acid?", List.of("electron-pair acceptor", "proton donor", "H+ donor", "electron pusher")),
            new Question("Chem", "Which one is the Tollens' reagent?", List.of("[Ag(NH3)2]+", "AgNO3", "CuSO4+NaOH", "CuC2")),
            new Question("Chem", "Which one is Fehling's reagent?", List.of( "CuSO4+NaOH","[Ag(NH3)2]+","R-MgX", "CuC2")),
            new Question("Chem", "Which one is Gilman's reagent?", List.of("CuC2","CuSO4+NaOH","[Ag(NH3)2]+","R-MgX")),
            new Question("Math", "What corresponds to Binomial distribution?", List.of("Bin(n,p)", "Bern(p)", "Beta(a,b)", "Pois(lambda)")),
            new Question("Math", "What have to be true for f to be Riemann integrable?", List.of("U(f) = L(f)", "L(P) = A(P)", "K(a) = K(b)", "A=B=C=D")),
            new Question("Math", "What's # of Federal Rules of Evidence for relevance? ", List.of("399+2", "444-40", "10+43", "99*4")),
            new Question("Math", "What is a supremum of [0,4]?", List.of("4", "5", "0", "NA")),
            new Question("Math", "What is 2^3/(3^10) bigger than", List.of("0", "1", "2", "3")),
            new Question("Math", "What is contrary to frequentist in statistics?", List.of("Bayesian", "Lagrangian", "Murphy", "Dimensional")),
            new Question("Chem", "Which one is Grignard's reagent?", List.of("R-MgX", "LAH", "DIBAL-H", "[Ag(NH3)2]+")),
            new Question("Chem", "Which one is an important neurotransmitter?", List.of("NO", "CO", "Sugar", "NO2")),
            new Question("Chem", "Which one is the major component of cell wall?", List.of("sugar", "Lipid", "protein", "inorganic elements")),
            new Question("Chem", "Who discover the element H?", List.of("Henry Cavendish", "Priestley", "Robert Boyle", "de Lavoisier")),
            new Question("Chem", "Which one is the strongest inorganic acid?", List.of("HClO4", "HSO4", "HF", "HNO3")),
            new Question("Chem", "Which one is the acid that can be used to erode glass", List.of("HF", "HClO4", "HSO4", "HCl")),
            new Question("Chem", "Which one is the noble gas?", List.of("Ar", "B", "Be", "F")),
            new Question("Math", "What is CLA in MSCS?", List.of("Computational Linear Algebra", "Contrary Linear Application", "Computation Linear Application", "Comparative List Analysis")),
            new Question("Math", "How many iMacs does Room 256 have?", List.of("10", "12", "9", "8")),
            new Question("Math", "How many session of Intro to Stats do MSCS offer in Spring 2020?", List.of("4", "2", "3", "6")),
            new Question("Math", "What is equivalent to a set being compact?", List.of("Closed and bounded", "Open and closed", "open and bounded", "None of the others")),
            new Question("Math", "What does MLE stand for?", List.of("Maximum Likelihood Estimation", "Marshmallow Like Estimation", "Mathematical Likelihood Estimation", "Moment Likelihood Estimation")),
            new Question("Math", "What is a prime number?", List.of("1001", "1003", "1005", "1008")),
            new Question("Math", "How do we represent the square root of -1", List.of("i", "k", "j", "There's no such thing!")),
            new Question("Math", "What is not a valid p-value?", List.of("1.5", "0.5", "0.00001", "0.05"))

    );
    private List<Question> availableQuestions = new ArrayList<>(masterQuestionList);
    private Random rand = new Random();

    /**
     * Given the index that a question is at, remove and return the same question.
     */
    private Question deleteQuestion(int index){
        return availableQuestions.remove(index);
    }

    /**
     * Return all available questions (those who have not appeared) of a specific type in the master list.
     */
    private List<Question> findAllQuestionsOfType(String type){
        return availableQuestions.stream()
                .filter(question -> question.getType().equals(type))
                .collect(toList());
    }

    /**
     * Select a question from the list of questions filtered by findAllQuestionsOfType
     * @return the list of questions with the chosen question deleted from it
     */
    public Question selectQuestion(BlockManager blockManager, int currentBlockNumber){
        String type = blockManager.getBlock(currentBlockNumber).getType();
        List<Question> questionList = findAllQuestionsOfType(type);
        int randomNumber = rand.nextInt(questionList.size());
        return deleteQuestion(randomNumber);
    }
}
