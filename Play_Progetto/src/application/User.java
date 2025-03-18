package application;

public class User {

    private String nome;
    private String password;
    private String cognome;
    private String nickname;

    public User(){
        this.nome="";
        this.cognome="";
        this.nickname="";
        this.password="";

    }
    public  void setNome(String n){
        this.nome=n;

    }
    public void setCognome(String c){
        this.cognome=c;
    }
    public void setNick(String n){
        this.nickname=n;
    } 
    
    public void setPassword(String p) {
        this.password=p;

    }
    public String toString(){
        String s = this.nome+","+this.cognome+","+this.nickname+","+this.password;
        return s;
    }
}


