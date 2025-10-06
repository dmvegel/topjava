<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<%@ page import="ru.javawebinar.topjava.web.MealServlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<div>
    <h3>Meals</h3>
    <a href="?action=${MealServlet.ACTION_CREATE}">Add meal</a>
</div>
<div>
    <table border="1">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach var="meal" items="${mealsTo}">
            <tr style="color: ${meal.excess ? 'red' : 'green'}">
                <td>${MealsUtil.formatDate(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="?action=${MealServlet.ACTION_EDIT}&id=${meal.id}">Update</a></td>
                <td><a href="?action=${MealServlet.ACTION_DELETE}&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
