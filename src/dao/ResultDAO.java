package dao;

import model.Question;
import model.Result;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultDAO {

    /**
     * Submit exam: save result + each answer.
     * answers = Map<questionId, chosenOpt>  (chosenOpt may be null = skipped)
     * Returns the result ID, or -1 on failure.
     */
    public int submitExam(int studentId, int examId,
                          List<Question> questions,
                          Map<Integer, String> answers) {

        // Calculate score
        int score = 0;
        for (Question q : questions) {
            String chosen = answers.get(q.getId());
            if (q.getCorrectOpt().equals(chosen)) score++;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Insert result row
            String resSql = "INSERT INTO results (student_id, exam_id, score, total) VALUES (?, ?, ?, ?)";
            PreparedStatement rps = con.prepareStatement(resSql, Statement.RETURN_GENERATED_KEYS);
            rps.setInt(1, studentId);
            rps.setInt(2, examId);
            rps.setInt(3, score);
            rps.setInt(4, questions.size());
            rps.executeUpdate();

            ResultSet keys = rps.getGeneratedKeys();
            if (!keys.next()) { con.rollback(); return -1; }
            int resultId = keys.getInt(1);

            // Insert each answer
            String ansSql = "INSERT INTO answers (result_id, question_id, chosen_opt) VALUES (?, ?, ?)";
            PreparedStatement aps = con.prepareStatement(ansSql);
            for (Question q : questions) {
                String chosen = answers.get(q.getId());
                aps.setInt(1, resultId);
                aps.setInt(2, q.getId());
                if (chosen != null) aps.setString(3, chosen);
                else                aps.setNull(3, Types.VARCHAR);
                aps.addBatch();
            }
            aps.executeBatch();
            con.commit();
            return resultId;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Student already attempted this exam
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -2; // special code: already attempted
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -1;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /** Check if student already attempted an exam */
    public boolean hasAttempted(int studentId, int examId) {
        String sql = "SELECT id FROM results WHERE student_id = ? AND exam_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, examId);
            return ps.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Get all results for a student */
    public List<Result> getResultsByStudent(int studentId) {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT r.*, e.title AS exam_title " +
                     "FROM results r JOIN exams e ON r.exam_id = e.id " +
                     "WHERE r.student_id = ? ORDER BY r.submitted_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResult(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Get all results (admin view) with student and exam names */
    public List<Result> getAllResults() {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS student_name, e.title AS exam_title " +
                     "FROM results r " +
                     "JOIN users u ON r.student_id = u.id " +
                     "JOIN exams e ON r.exam_id  = e.id " +
                     "ORDER BY r.submitted_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Result res = mapResult(rs);
                res.setStudentName(rs.getString("student_name"));
                list.add(res);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Get a single result with full answer details */
    public Result getResultDetail(int resultId) {
        String sql = "SELECT r.*, u.full_name AS student_name, e.title AS exam_title " +
                     "FROM results r JOIN users u ON r.student_id = u.id " +
                     "JOIN exams e ON r.exam_id = e.id WHERE r.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            Result result = mapResult(rs);
            result.setStudentName(rs.getString("student_name"));

            // Load questions with chosen answers
            String qSql = "SELECT q.*, a.chosen_opt FROM questions q " +
                          "JOIN answers a ON q.id = a.question_id " +
                          "WHERE a.result_id = ? ORDER BY q.id";
            PreparedStatement qps = con.prepareStatement(qSql);
            qps.setInt(1, resultId);
            ResultSet qrs = qps.executeQuery();
            List<Question> questions = new ArrayList<>();
            while (qrs.next()) {
                Question q = new Question();
                q.setId(qrs.getInt("id"));
                q.setQuestionText(qrs.getString("question_text"));
                q.setOptA(qrs.getString("opt_a"));
                q.setOptB(qrs.getString("opt_b"));
                q.setOptC(qrs.getString("opt_c"));
                q.setOptD(qrs.getString("opt_d"));
                q.setCorrectOpt(qrs.getString("correct_opt"));
                q.setChosenOpt(qrs.getString("chosen_opt"));
                questions.add(q);
            }
            result.setQuestions(questions);
            return result;
        } catch (SQLException e) { e.printStackTrace(); return null; }
    }

    private Result mapResult(ResultSet rs) throws SQLException {
        Result r = new Result();
        r.setId(rs.getInt("id"));
        r.setStudentId(rs.getInt("student_id"));
        r.setExamId(rs.getInt("exam_id"));
        r.setScore(rs.getInt("score"));
        r.setTotal(rs.getInt("total"));
        r.setSubmittedAt(rs.getTimestamp("submitted_at"));
        r.setExamTitle(rs.getString("exam_title"));
        return r;
    }
}
