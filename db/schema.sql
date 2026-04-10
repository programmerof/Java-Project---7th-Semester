-- ============================================
--  Online Exam System - Database Schema
--  Run this in MySQL: source schema.sql
-- ============================================

CREATE DATABASE IF NOT EXISTS online_exam_db;
USE online_exam_db;

-- ── Users (Admin + Students) ──────────────────
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,       -- store hashed in production
    role        ENUM('admin', 'student') NOT NULL DEFAULT 'student',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Exams ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS exams (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    duration_mins   INT NOT NULL DEFAULT 30,
    is_active       BOOLEAN NOT NULL DEFAULT FALSE,
    created_by      INT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- ── Questions ─────────────────────────────────
CREATE TABLE IF NOT EXISTS questions (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    exam_id         INT NOT NULL,
    question_text   TEXT NOT NULL,
    opt_a           VARCHAR(300) NOT NULL,
    opt_b           VARCHAR(300) NOT NULL,
    opt_c           VARCHAR(300) NOT NULL,
    opt_d           VARCHAR(300) NOT NULL,
    correct_opt     ENUM('A','B','C','D') NOT NULL,
    FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE
);

-- ── Results ───────────────────────────────────
CREATE TABLE IF NOT EXISTS results (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    exam_id         INT NOT NULL,
    score           INT NOT NULL DEFAULT 0,
    total           INT NOT NULL DEFAULT 0,
    submitted_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (exam_id)    REFERENCES exams(id),
    UNIQUE KEY unique_attempt (student_id, exam_id)   -- one attempt per exam
);

-- ── Answers (per submission) ───────────────────
CREATE TABLE IF NOT EXISTS answers (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    result_id       INT NOT NULL,
    question_id     INT NOT NULL,
    chosen_opt      ENUM('A','B','C','D'),             -- NULL = skipped
    FOREIGN KEY (result_id)   REFERENCES results(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- ── Seed Data ─────────────────────────────────
-- Default Admin  (password: admin123)
INSERT INTO users (full_name, email, password, role) VALUES
('Administrator', 'admin@exam.com', 'admin123', 'admin');

-- Sample Student (password: student123)
INSERT INTO users (full_name, email, password, role) VALUES
('Ram Sharma', 'ram@student.com', 'student123', 'student');

-- Sample Exam
INSERT INTO exams (title, description, duration_mins, is_active, created_by) VALUES
('Java Basics Quiz', 'Test your knowledge of Java fundamentals.', 10, TRUE, 1);

-- Sample Questions for Exam 1
INSERT INTO questions (exam_id, question_text, opt_a, opt_b, opt_c, opt_d, correct_opt) VALUES
(1, 'Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B'),
(1, 'What is the size of an int in Java?', '2 bytes', '4 bytes', '8 bytes', 'Depends on OS', 'B'),
(1, 'Which of these is NOT a Java access modifier?', 'public', 'private', 'protected', 'friend', 'D'),
(1, 'What does JVM stand for?', 'Java Virtual Memory', 'Java Variable Method', 'Java Virtual Machine', 'Java Verified Module', 'C'),
(1, 'Which interface must be implemented for multithreading?', 'Serializable', 'Runnable', 'Cloneable', 'Iterable', 'B');
