package model;

import java.sql.Timestamp;
import java.util.List;

public class Result {
    private int            id;
    private int            studentId;
    private int            examId;
    private int            score;
    private int            total;
    private Timestamp      submittedAt;
    // helper fields joined from other tables
    private String         studentName;
    private String         examTitle;
    private List<Question> questions;  // with chosenOpt filled in

    public Result() {}

    public int       getId()           { return id; }
    public int       getStudentId()    { return studentId; }
    public int       getExamId()       { return examId; }
    public int       getScore()        { return score; }
    public int       getTotal()        { return total; }
    public Timestamp getSubmittedAt()  { return submittedAt; }
    public String    getStudentName()  { return studentName; }
    public String    getExamTitle()    { return examTitle; }
    public List<Question> getQuestions() { return questions; }

    public void setId(int id)                       { this.id = id; }
    public void setStudentId(int studentId)         { this.studentId = studentId; }
    public void setExamId(int examId)               { this.examId = examId; }
    public void setScore(int score)                 { this.score = score; }
    public void setTotal(int total)                 { this.total = total; }
    public void setSubmittedAt(Timestamp t)         { this.submittedAt = t; }
    public void setStudentName(String name)         { this.studentName = name; }
    public void setExamTitle(String title)          { this.examTitle = title; }
    public void setQuestions(List<Question> q)      { this.questions = q; }

    /** Percentage score, rounded */
    public int getPercentage() {
        if (total == 0) return 0;
        return (int) Math.round((score * 100.0) / total);
    }

    /** Grade label based on percentage */
    public String getGrade() {
        int p = getPercentage();
        if (p >= 90) return "A+";
        if (p >= 80) return "A";
        if (p >= 70) return "B";
        if (p >= 60) return "C";
        if (p >= 50) return "D";
        return "F";
    }
}
