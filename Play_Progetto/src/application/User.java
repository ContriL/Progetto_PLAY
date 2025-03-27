package application;

public class User {
    private String nome;
    private String password;
    private String cognome;
    private String nickname;

    public User() {
        this.nome = "";
        this.cognome = "";
        this.nickname = "";
        this.password = "";
    }

    // Add validation methods
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2;
    }

    public boolean isValidPassword(String password) {
        // Example password requirements:
        // - At least 8 characters
        // - Contains at least one uppercase, one lowercase, and one number
        return password != null &&
                password.length() >= 8 &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    }

    public boolean isValidNickname(String nickname) {
        return nickname != null &&
                !nickname.trim().isEmpty() &&
                nickname.length() >= 3 &&
                nickname.length() <= 20 &&
                nickname.matches("^[a-zA-Z0-9_]+$");
    }

    // Setters with validation
    public void setNome(String n) {
        if (isValidName(n)) {
            this.nome = n.trim();
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public void setCognome(String c) {
        if (isValidName(c)) {
            this.cognome = c.trim();
        } else {
            throw new IllegalArgumentException("Invalid surname");
        }
    }

    public void setNick(String n) {
        if (isValidNickname(n)) {
            this.nickname = n.trim();
        } else {
            throw new IllegalArgumentException("Invalid nickname");
        }
    }

    public void setPassword(String p) {
        if (isValidPassword(p)) {
            this.password = hashPassword(p);
        } else {
            throw new IllegalArgumentException("Password does not meet requirements");
        }
    }

    // Simple password hashing (not cryptographically secure, for educational purposes)
    private String hashPassword(String password) {
        return String.valueOf(password.hashCode());
    }

    @Override
    public String toString() {
        return String.join(",", nome, cognome, nickname, password);
    }

    // Getters (optional, but useful for future extensions)
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getNickname() { return nickname; }
}