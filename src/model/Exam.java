package model;

import java.sql.Timestamp;

public class Exam {
    private int       id;
    private String    title;
    private String    description;
    private int       durationMins;
    private boolean   isActive;
    private int       createdBy;
    private Timestamp createdAt;
    private int       questionCount; // helper field (not a column)

    public Exam() {}

    public int       getId()            { return id; }
    public String    getTitle()         { return title; }
    public String    getDescription()   { return description; }
    public int       getDurationMins()  { return durationMins; }
    public boolean   isActive()         { return isActive; }
    public int       getCreatedBy()     { return createdBy; }
    public Timestamp getCreatedAt()     { return createdAt; }
    public int       getQuestionCount() { return questionCount; }

    public void setId(int id)                       { this.id = id; }
    public void setTitle(String title)              { this.title = title; }
    public void setDescription(String description)  { this.description = description; }
    public void setDurationMins(int durationMins)   { this.durationMins = durationMins; }
    public void setActive(boolean active)           { isActive = active; }
    public void setCreatedBy(int createdBy)         { this.createdBy = createdBy; }
    public void setCreatedAt(Timestamp createdAt)   { this.createdAt = createdAt; }
    public void setQuestionCount(int count)         { this.questionCount = count; }
}
