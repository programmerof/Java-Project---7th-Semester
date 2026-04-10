<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Exam, model.Question" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Taking Exam — ExamPortal</title>

<link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet">
<style>
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
:root {
    --bg: #0d0f1a; --panel: #13162a; --panel2: #181c30;
    --border: #1e2340; --accent: #4f6ef7; --accent2: #7c3aed;
    --text: #e8eaf6; --muted: #6b7280; --error: #f87171;
    --success: #34d399; --warning: #fbbf24;
}
body { background: var(--bg); color: var(--text); font-family: 'DM Sans', sans-serif; min-height: 100vh; }
a { color: var(--accent); text-decoration: none; }
a:hover { text-decoration: underline; }

/* Navbar */
.navbar {
    display: flex; align-items: center; justify-content: space-between;
    padding: 1rem 2rem; background: var(--panel);
    border-bottom: 1px solid var(--border); position: sticky; top: 0; z-index: 100;
}
.nav-brand { font-family: 'Syne', sans-serif; font-size: 1.1rem; font-weight: 800; }
.nav-role { font-size: 0.65rem; background: var(--accent); color: #fff; padding: 2px 7px; border-radius: 20px; margin-left: 8px; vertical-align: middle; }
.nav-links { display: flex; align-items: center; gap: 1.5rem; font-size: 0.9rem; }
.nav-links a { color: var(--muted); transition: color 0.2s; }
.nav-links a:hover { color: var(--text); text-decoration: none; }
.nav-user { color: var(--success); font-size: 0.85rem; }
.btn-logout { background: rgba(248,113,113,0.1); color: var(--error) !important; padding: 0.3rem 0.8rem; border-radius: 8px; }

/* Main container */
.main { max-width: 1100px; margin: 0 auto; padding: 2rem 1.5rem; }
.page-header { margin-bottom: 2rem; }
.page-header h1 { font-family: 'Syne', sans-serif; font-size: 1.7rem; font-weight: 800; }
.page-header p  { color: var(--muted); margin-top: 0.3rem; }

/* Cards */
.card { background: var(--panel); border: 1px solid var(--border); border-radius: 16px; padding: 1.5rem; }
.card + .card { margin-top: 1rem; }

/* Stats row */
.stats-row { display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 1rem; margin-bottom: 2rem; }
.stat-card {
    background: var(--panel); border: 1px solid var(--border); border-radius: 14px;
    padding: 1.25rem; text-align: center;
}
.stat-number { font-family: 'Syne', sans-serif; font-size: 2rem; font-weight: 800; color: var(--accent); }
.stat-label  { color: var(--muted); font-size: 0.8rem; margin-top: 0.2rem; }

/* Table */
.table-wrap { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
th { background: var(--panel2); color: var(--muted); font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em; padding: 0.75rem 1rem; text-align: left; }
td { padding: 0.9rem 1rem; border-bottom: 1px solid var(--border); font-size: 0.9rem; }
tr:last-child td { border-bottom: none; }
tr:hover td { background: rgba(255,255,255,0.02); }

/* Badges */
.badge { display: inline-block; padding: 3px 10px; border-radius: 20px; font-size: 0.75rem; font-weight: 600; }
.badge-active   { background: rgba(52,211,153,0.15); color: var(--success); }
.badge-inactive { background: rgba(107,114,128,0.15); color: var(--muted); }
.badge-a  { background: rgba(52,211,153,0.15); color: var(--success); }
.badge-b  { background: rgba(79,110,247,0.15);  color: var(--accent); }
.badge-c  { background: rgba(251,191,36,0.15);  color: var(--warning); }
.badge-f  { background: rgba(248,113,113,0.15); color: var(--error); }

/* Buttons */
.btn { display: inline-block; padding: 0.5rem 1.1rem; border-radius: 9px; font-size: 0.875rem; font-weight: 600; cursor: pointer; border: none; font-family: 'DM Sans', sans-serif; transition: opacity 0.2s; }
.btn:hover { opacity: 0.85; }
.btn-primary  { background: linear-gradient(135deg, var(--accent), var(--accent2)); color: #fff; }
.btn-success  { background: rgba(52,211,153,0.15); color: var(--success); border: 1px solid rgba(52,211,153,0.3); }
.btn-danger   { background: rgba(248,113,113,0.1); color: var(--error); border: 1px solid rgba(248,113,113,0.3); }
.btn-warning  { background: rgba(251,191,36,0.1); color: var(--warning); border: 1px solid rgba(251,191,36,0.3); }
.btn-sm { padding: 0.3rem 0.75rem; font-size: 0.8rem; }
.btn-block { width: 100%; text-align: center; padding: 0.85rem; }

/* Form */
.form-group { margin-bottom: 1.1rem; }
label { display: block; font-size: 0.8rem; font-weight: 500; color: var(--muted); margin-bottom: 0.4rem; text-transform: uppercase; letter-spacing: 0.06em; }
input[type=text], input[type=number], input[type=email], textarea, select {
    width: 100%; background: rgba(255,255,255,0.04); border: 1px solid var(--border);
    border-radius: 10px; padding: 0.7rem 1rem; color: var(--text);
    font-family: 'DM Sans', sans-serif; font-size: 0.9rem; outline: none;
    transition: border-color 0.2s;
}
input:focus, textarea:focus, select:focus { border-color: var(--accent); }
textarea { resize: vertical; min-height: 80px; }
select option { background: var(--panel); }

/* Alert */
.alert { padding: 0.75rem 1rem; border-radius: 10px; font-size: 0.875rem; margin-bottom: 1.25rem; }
.alert-error   { background: rgba(248,113,113,0.1); border: 1px solid rgba(248,113,113,0.3); color: var(--error); }
.alert-success { background: rgba(52,211,153,0.1);  border: 1px solid rgba(52,211,153,0.3);  color: var(--success); }
.alert-info    { background: rgba(79,110,247,0.08); border: 1px solid rgba(79,110,247,0.25);  color: #a5b4fc; }

/* Exam grid */
.exam-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1rem; }
.exam-card {
    background: var(--panel); border: 1px solid var(--border); border-radius: 16px;
    padding: 1.5rem; display: flex; flex-direction: column; gap: 0.75rem;
    transition: border-color 0.2s;
}
.exam-card:hover { border-color: var(--accent); }
.exam-card-title { font-family: 'Syne', sans-serif; font-size: 1rem; font-weight: 700; }
.exam-card-meta  { color: var(--muted); font-size: 0.82rem; display: flex; gap: 1rem; }
.exam-card-actions { margin-top: auto; display: flex; gap: 0.5rem; }

/* Radio option cards */
.option-group { display: grid; grid-template-columns: 1fr 1fr; gap: 0.5rem; }
.option-label {
    display: flex; align-items: center; gap: 0.6rem;
    background: rgba(255,255,255,0.03); border: 1px solid var(--border);
    border-radius: 10px; padding: 0.7rem 1rem; cursor: pointer;
    transition: border-color 0.2s, background 0.2s; font-size: 0.9rem;
}
.option-label:hover { border-color: var(--accent); background: rgba(79,110,247,0.06); }
input[type=radio]:checked + .option-label,
.option-label:has(input[type=radio]:checked) { border-color: var(--accent); background: rgba(79,110,247,0.12); }
.option-label input[type=radio] { accent-color: var(--accent); }

/* Answer review */
.q-block { background: var(--panel); border: 1px solid var(--border); border-radius: 14px; padding: 1.25rem; margin-bottom: 1rem; }
.q-block.correct { border-color: rgba(52,211,153,0.4); background: rgba(52,211,153,0.04); }
.q-block.wrong   { border-color: rgba(248,113,113,0.4); background: rgba(248,113,113,0.04); }
.q-block.skipped { border-color: rgba(251,191,36,0.3);  background: rgba(251,191,36,0.03); }
.q-text { font-weight: 500; margin-bottom: 0.75rem; }
.q-opts { display: grid; gap: 0.4rem; font-size: 0.875rem; }
.opt-row { padding: 0.4rem 0.7rem; border-radius: 7px; }
.opt-correct { background: rgba(52,211,153,0.15); color: var(--success); }
.opt-wrong   { background: rgba(248,113,113,0.15); color: var(--error); }
</style>

<style>
.exam-header {
    position: sticky; top: 64px; z-index: 50;
    background: var(--panel); border-bottom: 1px solid var(--border);
    padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center;
}
.timer {
    font-family: 'Syne', sans-serif; font-size: 1.5rem; font-weight: 800;
    color: var(--success); display: flex; align-items: center; gap: 0.5rem;
}
.timer.warning { color: var(--warning); }
.timer.danger  { color: var(--error); animation: pulse 1s infinite; }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.5} }

.question-card {
    background: var(--panel); border: 1px solid var(--border); border-radius: 16px;
    padding: 1.75rem; margin-bottom: 1.25rem;
}
.question-num { color: var(--muted); font-size: 0.78rem; text-transform: uppercase; letter-spacing: 0.08em; margin-bottom: 0.5rem; }
.question-text { font-size: 1.05rem; font-weight: 500; margin-bottom: 1.25rem; line-height: 1.5; }
.options { display: grid; gap: 0.6rem; }
.opt {
    display: flex; align-items: center; gap: 0.75rem;
    background: rgba(255,255,255,0.03); border: 1px solid var(--border);
    border-radius: 10px; padding: 0.8rem 1rem; cursor: pointer;
    transition: border-color 0.15s, background 0.15s;
}
.opt:hover { border-color: var(--accent); background: rgba(79,110,247,0.07); }
.opt input[type=radio] { accent-color: var(--accent); width:16px; height:16px; flex-shrink:0; }
.opt:has(input:checked) { border-color: var(--accent); background: rgba(79,110,247,0.12); }

.submit-bar {
    position: sticky; bottom: 0;
    background: var(--panel); border-top: 1px solid var(--border);
    padding: 1rem 2rem; text-align: right;
}
.progress-dots { display: flex; gap: 5px; flex-wrap: wrap; margin-bottom: 0.75rem; }
.dot { width:10px; height:10px; border-radius:50%; background: var(--border); transition: background 0.2s; }
.dot.answered { background: var(--accent); }
</style>
</head>
<body>
<jsp:include page="/jsp/student/navbar.jsp"/>

<%
    Exam exam = (Exam) request.getAttribute("exam");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    int totalSeconds = exam.getDurationMins() * 60;
%>

<div class="exam-header">
    <div>
        <div style="font-family:'Syne',sans-serif; font-weight:800; font-size:1rem;"><%= exam.getTitle() %></div>
        <div style="color:var(--muted); font-size:0.82rem;"><%= questions.size() %> questions</div>
    </div>
    <div class="timer" id="timer">&#128337; <%= exam.getDurationMins() %>:00</div>
</div>

<div class="main" style="padding-top:1.5rem;">
    <form id="examForm" method="post" action="${pageContext.request.contextPath}/student/submit">
        <input type="hidden" name="examId" value="<%= exam.getId() %>">

        <% int num = 1; for (Question q : questions) { %>
        <div class="question-card" id="qcard_<%= q.getId() %>">
            <div class="question-num">Question <%= num++ %> of <%= questions.size() %></div>
            <div class="question-text"><%= q.getQuestionText() %></div>
            <div class="options">
                <% String[] opts = {q.getOptA(), q.getOptB(), q.getOptC(), q.getOptD()};
                   String[] keys = {"A","B","C","D"};
                   for (int i = 0; i < 4; i++) { %>
                <label class="opt">
                    <input type="radio" name="answer_<%= q.getId() %>" value="<%= keys[i] %>"
                           onchange="markAnswered(<%= q.getId() %>)">
                    <span><strong><%= keys[i] %>.</strong> <%= opts[i] %></span>
                </label>
                <% } %>
            </div>
        </div>
        <% } %>

        <div class="submit-bar">
            <div class="progress-dots" id="progressDots">
                <% for (Question q : questions) { %>
                    <div class="dot" id="dot_<%= q.getId() %>"></div>
                <% } %>
            </div>
            <button type="submit" class="btn btn-primary"
                    onclick="return confirm('Submit exam? You cannot change answers after submission.')">
                Submit Exam &#10003;
            </button>
        </div>
    </form>
</div>

<script>
// ── Countdown Timer ────────────────────────────────────────────
let totalSecs = <%= totalSeconds %>;
const timerEl = document.getElementById('timer');

const tick = () => {
    const m = Math.floor(totalSecs / 60);
    const s = totalSecs % 60;
    timerEl.textContent = '\u23F1 ' + String(m).padStart(2,'0') + ':' + String(s).padStart(2,'0');

    timerEl.className = 'timer';
    if (totalSecs <= 60)  timerEl.classList.add('danger');
    else if (totalSecs <= 300) timerEl.classList.add('warning');

    if (totalSecs <= 0) {
        clearInterval(interval);
        alert('Time is up! Your exam will be submitted now.');
        document.getElementById('examForm').submit();
        return;
    }
    totalSecs--;
};

tick();
const interval = setInterval(tick, 1000);

// Warn on accidental close
window.onbeforeunload = () => 'Your exam is in progress. Are you sure you want to leave?';
document.getElementById('examForm').addEventListener('submit', () => {
    window.onbeforeunload = null;
    clearInterval(interval);
});

// ── Progress dots ──────────────────────────────────────────────
function markAnswered(qId) {
    document.getElementById('dot_' + qId).classList.add('answered');
}
</script>
</body>
</html>
