<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register — ExamPortal</title>
    <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        :root {
            --bg: #0d0f1a; --panel: #13162a; --border: #1e2340;
            --accent: #4f6ef7; --accent2: #7c3aed;
            --text: #e8eaf6; --muted: #6b7280; --error: #f87171;
        }
        body {
            min-height: 100vh; background: var(--bg);
            font-family: 'DM Sans', sans-serif;
            display: flex; align-items: center; justify-content: center;
            position: relative; overflow: hidden;
        }
        body::before {
            content: ''; position: fixed; width: 600px; height: 600px;
            background: radial-gradient(circle, rgba(79,110,247,0.12) 0%, transparent 70%);
            top: -200px; left: -200px; pointer-events: none;
        }
        body::after {
            content: ''; position: fixed; width: 500px; height: 500px;
            background: radial-gradient(circle, rgba(124,58,237,0.10) 0%, transparent 70%);
            bottom: -150px; right: -150px; pointer-events: none;
        }
        .container { width: 100%; max-width: 420px; padding: 1.5rem; position: relative; z-index: 1; }
        .brand { text-align: center; margin-bottom: 2rem; }
        .brand-icon {
            width: 56px; height: 56px;
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            border-radius: 16px; display: inline-flex;
            align-items: center; justify-content: center;
            margin-bottom: 1rem; font-size: 1.5rem;
        }
        .brand h1 { font-family: 'Syne', sans-serif; font-size: 1.8rem; font-weight: 800; color: var(--text); letter-spacing: -0.03em; }
        .brand p  { color: var(--muted); font-size: 0.875rem; margin-top: 0.3rem; }
        .card { background: var(--panel); border: 1px solid var(--border); border-radius: 20px; padding: 2rem; }
        .card h2 { font-family: 'Syne', sans-serif; font-size: 1.2rem; font-weight: 700; color: var(--text); margin-bottom: 1.5rem; }
        .alert { padding: 0.75rem 1rem; border-radius: 10px; font-size: 0.875rem; margin-bottom: 1.25rem; }
        .alert-error { background: rgba(248,113,113,0.1); border: 1px solid rgba(248,113,113,0.3); color: var(--error); }
        .form-group { margin-bottom: 1.1rem; }
        label { display: block; font-size: 0.8rem; font-weight: 500; color: var(--muted); margin-bottom: 0.4rem; text-transform: uppercase; letter-spacing: 0.06em; }
        input {
            width: 100%; background: rgba(255,255,255,0.04); border: 1px solid var(--border);
            border-radius: 10px; padding: 0.75rem 1rem; color: var(--text);
            font-family: 'DM Sans', sans-serif; font-size: 0.95rem;
            transition: border-color 0.2s; outline: none;
        }
        input:focus { border-color: var(--accent); background: rgba(79,110,247,0.06); }
        input::placeholder { color: var(--muted); }
        .btn-primary {
            width: 100%; padding: 0.85rem;
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            color: #fff; border: none; border-radius: 10px;
            font-family: 'Syne', sans-serif; font-size: 0.95rem; font-weight: 700;
            cursor: pointer; margin-top: 0.5rem; transition: opacity 0.2s;
        }
        .btn-primary:hover { opacity: 0.9; }
        .card-footer { text-align: center; margin-top: 1.25rem; font-size: 0.85rem; color: var(--muted); }
        .card-footer a { color: var(--accent); text-decoration: none; font-weight: 500; }
    </style>
</head>
<body>
<div class="container">
    <div class="brand">
        <div class="brand-icon">📝</div>
        <h1>ExamPortal</h1>
        <p>Create your student account</p>
    </div>

    <div class="card">
        <h2>Student Registration</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">⚠ <%= request.getAttribute("error") %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/register" method="post">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" placeholder="Ram Sharma" required>
            </div>
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="you@example.com" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Min 6 characters" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Repeat password" required>
            </div>

            <button type="submit" class="btn-primary">Create Account →</button>
        </form>

        <div class="card-footer">
            Already have an account? <a href="<%= request.getContextPath() %>/login">Login here</a>
        </div>
    </div>
</div>
</body>
</html>
