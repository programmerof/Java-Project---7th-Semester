package model;

public class Question {
    private int    id;
    private int    examId;
    private String questionText;
    private String optA, optB, optC, optD;
    private String correctOpt;
    private String chosenOpt;   // helper: student's answer (not a DB column here)

    public Question() {}

    public int    getId()           { return id; }
    public int    getExamId()       { return examId; }
    public String getQuestionText() { return questionText; }
    public String getOptA()         { return optA; }
    public String getOptB()         { return optB; }
    public String getOptC()         { return optC; }
    public String getOptD()         { return optD; }
    public String getCorrectOpt()   { return correctOpt; }
    public String getChosenOpt()    { return chosenOpt; }

    public void setId(int id)                       { this.id = id; }
    public void setExamId(int examId)               { this.examId = examId; }
    public void setQuestionText(String t)           { this.questionText = t; }
    public void setOptA(String optA)                { this.optA = optA; }
    public void setOptB(String optB)                { this.optB = optB; }
    public void setOptC(String optC)                { this.optC = optC; }
    public void setOptD(String optD)                { this.optD = optD; }
    public void setCorrectOpt(String correctOpt)    { this.correctOpt = correctOpt; }
    public void setChosenOpt(String chosenOpt)      { this.chosenOpt = chosenOpt; }

    public boolean isCorrect() {
        return correctOpt != null && correctOpt.equals(chosenOpt);
    }
}
