<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <h2>Signup</h2>
    <form action="RegisterServlet" method="post">
        <label>Username:</label> <input type="text" name="username" required><br>
        <label>Email:</label> <input type="email" name="email" required><br>
        <label>Password:</label> <input type="password" name="password" required><br>
        <input type="submit" value="Sign Up">
    </form>
</body>
</html>
