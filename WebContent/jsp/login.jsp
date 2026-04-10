<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — ExamPortal</title>
    <link href="https://fonts.googleapis.com/css2?family=Syne:wght@400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        :root {
            --bg:       #0d0f1a;
            --panel:    #13162a;
            --border:   #1e2340;
            --accent:   #4f6ef7;
            --accent2:  #7c3aed;
            --text:     #e8eaf6;
            --muted:    #6b7280;
            --error:    #f87171;
            --success:  #34d399;
        }

        body {
            min-height: 100vh;
            background: var(--bg);
            font-family: 'DM Sans', sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }

        /* Ambient background blobs */
        body::before {
            content: '';
            position: fixed;
            width: 600px; height: 600px;
            background: radial-gradient(circle, rgba(79,110,247,0.12) 0%, transparent 70%);
            top: -200px; left: -200px;
            pointer-events: none;
        }
        body::after {
            content: '';
            position: fixed;
            width: 500px; height: 500px;
            background: radial-gradient(circle, rgba(124,58,237,0.10) 0%, transparent 70%);
            bottom: -150px; right: -150px;
            pointer-events: none;
        }

        .container {
            width: 100%;
            max-width: 420px;
            padding: 1.5rem;
            position: relative;
            z-index: 1;
        }

        /* Brand header */
        .brand {
            text-align: center;
            margin-bottom: 2.5rem;
        }
        .brand-icon {
            width: 56px; height: 56px;
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            border-radius: 16px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1rem;
            font-size: 1.5rem;
        }
        .brand h1 {
            font-family: 'Syne', sans-serif;
            font-size: 1.8rem;
            font-weight: 800;
            color: var(--text);
            letter-spacing: -0.03em;
        }
        .brand p {
            color: var(--muted);
            font-size: 0.875rem;
            margin-top: 0.3rem;
        }

        /* Card */
        .card {
            background: var(--panel);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 2rem;
        }

        .card h2 {
            font-family: 'Syne', sans-serif;
            font-size: 1.2rem;
            font-weight: 700;
            color: var(--text);
            margin-bottom: 1.5rem;
        }

        /* Alert */
        .alert {
            padding: 0.75rem 1rem;
            border-radius: 10px;
            font-size: 0.875rem;
            margin-bottom: 1.25rem;
        }
        .alert-error   { background: rgba(248,113,113,0.1); border: 1px solid rgba(248,113,113,0.3); color: var(--error); }
        .alert-success { background: rgba(52,211,153,0.1);  border: 1px solid rgba(52,211,153,0.3);  color: var(--success); }

        /* Form */
        .form-group { margin-bottom: 1.1rem; }

        label {
            display: block;
            font-size: 0.8rem;
            font-weight: 500;
            color: var(--muted);
            margin-bottom: 0.4rem;
            text-transform: uppercase;
            letter-spacing: 0.06em;
        }

        input[type="email"],
        input[type="password"],
        input[type="text"] {
            width: 100%;
            background: rgba(255,255,255,0.04);
            border: 1px solid var(--border);
            border-radius: 10px;
            padding: 0.75rem 1rem;
            color: var(--text);
            font-family: 'DM Sans', sans-serif;
            font-size: 0.95rem;
            transition: border-color 0.2s, background 0.2s;
            outline: none;
        }
        input:focus {
            border-color: var(--accent);
            background: rgba(79,110,247,0.06);
        }
        input::placeholder { color: var(--muted); }

        /* Button */
        .btn-primary {
            width: 100%;
            padding: 0.85rem;
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            color: #fff;
            border: none;
            border-radius: 10px;
            font-family: 'Syne', sans-serif;
            font-size: 0.95rem;
            font-weight: 700;
            letter-spacing: 0.02em;
            cursor: pointer;
            margin-top: 0.5rem;
            transition: opacity 0.2s, transform 0.1s;
        }
        .btn-primary:hover  { opacity: 0.9; }
        .btn-primary:active { transform: scale(0.99); }

        /* Footer */
        .card-footer {
            text-align: center;
            margin-top: 1.25rem;
            font-size: 0.85rem;
            color: var(--muted);
        }
        .card-footer a {
            color: var(--accent);
            text-decoration: none;
            font-weight: 500;
        }
        .card-footer a:hover { text-decoration: underline; }

        /* Demo hint */
        .demo-hint {
            margin-top: 1.25rem;
            padding: 0.75rem 1rem;
            background: rgba(79,110,247,0.06);
            border: 1px solid rgba(79,110,247,0.2);
            border-radius: 10px;
            font-size: 0.78rem;
            color: var(--muted);
            line-height: 1.6;
        }
        .demo-hint strong { color: var(--text); }
    </style>
</head>
<body>

<div class="container">
    <div class="brand">
        <div class="brand-icon">📝</div>
        <h1>ExamPortal</h1>
        <p>Online Examination System</p>
    </div>

    <div class="card">
        <h2>Sign in to your account</h2>

        <%-- Error message --%>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">⚠ <%= request.getAttribute("error") %></div>
        <% } %>

        <%-- Registration success message --%>
        <% if ("true".equals(request.getParameter("registered"))) { %>
            <div class="alert alert-success">✓ Account created! Please log in.</div>
        <% } %>

        <form action="<%= request.getContextPath() %>/login" method="post">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email"
                       placeholder="you@example.com"
                       value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                       required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-primary">Sign In →</button>
        </form>

        <div class="card-footer">
            Don't have an account? <a href="<%= request.getContextPath() %>/register">Register here</a>
        </div>

        <div class="demo-hint">
            <strong>Demo credentials</strong><br>
            Admin: admin@exam.com / admin123<br>
            Student: ram@student.com / student123
        </div>
    </div>
</div>

</body>
</html>
