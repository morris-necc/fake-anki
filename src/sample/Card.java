package sample;

public class Card {
    private String question;
    private String answer;

    Card(String q, String a){
        setQuestion(q);
        setAnswer(a);
    }

    public String getQuestion(){
        return this.question;
    }

    public String getAnswer(){
        return this.answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
