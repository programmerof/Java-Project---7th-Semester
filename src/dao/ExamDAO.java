package dao;

import model.Exam;
import model.Question;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {

    // ── EXAM CRUD ──────────────────────────────────────────────────────

    /** Get all exams (with question count) */
    public List<Exam> getAllExams() {
        List<Exam> list = new ArrayList<>();
        String sql = "SELECT e.*, COUNT(q.id) AS question_count " +
                     "FROM exams e LEFT JOIN questions q ON e.id = q.exam_id " +
                     "GROUP BY e.id ORDER BY e.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapExam(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Get only active exams (for student dashboard) */
    public List<Exam> getActiveExams() {
        List<Exam> list = new ArrayList<>();
        String sql = "SELECT e.*, COUNT(q.id) AS question_count " +
                     "FROM exams e LEFT JOIN questions q ON e.id = q.exam_id " +
                     "WHERE e.is_active = TRUE " +
                     "GROUP BY e.id ORDER BY e.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapExam(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Get a single exam by ID */
    public Exam getExamById(int id) {
        String sql = "SELECT e.*, COUNT(q.id) AS question_count " +
                     "FROM exams e LEFT JOIN questions q ON e.id = q.exam_id " +
                     "WHERE e.id = ? GROUP BY e.id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapExam(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /** Create a new exam, return generated ID */
    public int createExam(String title, String description, int durationMins, int createdBy) {
        String sql = "INSERT INTO exams (title, description, duration_mins, created_by) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setInt(3, durationMins);
            ps.setInt(4, createdBy);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    /** Toggle exam active/inactive */
    public boolean toggleActive(int examId) {
        String sql = "UPDATE exams SET is_active = NOT is_active WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Delete an exam (cascades to questions, results, answers) */
    public boolean deleteExam(int examId) {
        String sql = "DELETE FROM exams WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── QUESTION CRUD ──────────────────────────────────────────────────

    /** Get all questions for an exam */
    public List<Question> getQuestionsByExam(int examId) {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE exam_id = ? ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapQuestion(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Add a question to an exam */
    public boolean addQuestion(int examId, String text,
                               String optA, String optB, String optC, String optD,
                               String correctOpt) {
        String sql = "INSERT INTO questions (exam_id, question_text, opt_a, opt_b, opt_c, opt_d, correct_opt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examId);
            ps.setString(2, text);
            ps.setString(3, optA);
            ps.setString(4, optB);
            ps.setString(5, optC);
            ps.setString(6, optD);
            ps.setString(7, correctOpt);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Delete a single question */
    public boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Helpers ────────────────────────────────────────────────────────

    private Exam mapExam(ResultSet rs) throws SQLException {
        Exam e = new Exam();
        e.setId(rs.getInt("id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setDurationMins(rs.getInt("duration_mins"));
        e.setActive(rs.getBoolean("is_active"));
        e.setCreatedBy(rs.getInt("created_by"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setQuestionCount(rs.getInt("question_count"));
        return e;
    }

    private Question mapQuestion(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setId(rs.getInt("id"));
        q.setExamId(rs.getInt("exam_id"));
        q.setQuestionText(rs.getString("question_text"));
        q.setOptA(rs.getString("opt_a"));
        q.setOptB(rs.getString("opt_b"));
        q.setOptC(rs.getString("opt_c"));
        q.setOptD(rs.getString("opt_d"));
        q.setCorrectOpt(rs.getString("correct_opt"));
        return q;
    }
}
