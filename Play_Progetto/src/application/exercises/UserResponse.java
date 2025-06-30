package application.exercises;

public class UserResponse {
    private String answer;
    private boolean isCorrect;
    private boolean attempted;
    private long timestamp;
    
    // Costruttore di default
    public UserResponse() {
        this.answer = "";
        this.isCorrect = false;
        this.attempted = false;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Costruttore con risposta
    public UserResponse(String answer) {
        this();
        this.answer = answer;
        this.attempted = true;
    }
    
    // Getter e Setter
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
        this.attempted = true;
        this.timestamp = System.currentTimeMillis();
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setCorrect(boolean correct) {
        this.isCorrect = correct;
    }
    
    public boolean isAttempted() {
        return attempted;
    }
    
    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }
    
    public boolean isComplete() {
        return attempted && answer != null && !answer.trim().isEmpty();
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    // Metodo per resettare la risposta
    public void reset() {
        this.answer = "";
        this.isCorrect = false;
        this.attempted = false;
        this.timestamp = System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return "UserResponse{" +
                "answer='" + answer + '\'' +
                ", isCorrect=" + isCorrect +
                ", attempted=" + attempted +
                '}';
    }
}