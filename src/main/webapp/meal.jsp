<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<div>
    <h3>Edit meal</h3>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}"><br>
        <label for="dateTime">DateTime:</label>
        <input id="dateTime" type="datetime-local" name="dateTime" value="${meal.dateTime}" required><br>
        <label for="description">Description:</label>
        <input id="description" type="text" name="description" size=50 value="${meal.description}" required><br>
        <label for="calories">Calories:</label>
        <input id="calories" type="number" name="calories" size=50 value="${meal.calories}" required><br>

        <button type="submit">Submit</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</div>
</body>
</html>
