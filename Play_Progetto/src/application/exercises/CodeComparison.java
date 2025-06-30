package application.exercises;

public class CodeComparison {
    private String code1;
    private String code2;
    private String question;
    private String explanation;
    private String correctAnswer;
    private int difficulty;
    
    // Costruttore completo
    public CodeComparison(String code1, String code2, String question, String correctAnswer, String explanation) {
        this.code1 = code1;
        this.code2 = code2;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }
    
    // Costruttore semplificato
    public CodeComparison(String code1, String code2, String question, String correctAnswer) {
        this(code1, code2, question, correctAnswer, "");
    }
    
    // Getter e Setter
    public String getCode1() {
        return code1;
    }
    
    public void setCode1(String code1) {
        this.code1 = code1;
    }
    
    public String getCode2() {
        return code2;
    }
    
    public void setCode2(String code2) {
        this.code2 = code2;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}