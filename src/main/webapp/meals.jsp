<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ page import="ru.javawebinar.topjava.web.SecurityUtil" %>

<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <form action="meals" method="get">
        <input type="hidden" name="action" value="login">
        <select name="role">
            <option value="1">User</option>
            <option ${SecurityUtil.authUserId() == '2' ? 'selected' : ''} value="2">Admin</option>
        </select>
        <input type="submit" value="login">
    </form>
    <h2>Meals</h2>
    <div>
        <form action="meals" method="get">
            <input type="hidden" name="action" value="filter">
            <label for="dateFrom">С</label>
            <input type="date" name="dateFrom" id="dateFrom" value="${mealFilter.dateFrom}"/>
            <label for="dateTo">По</label>
            <input type="date" name="dateTo" id="dateTo" value="${mealFilter.dateTo}"/><br>
            <label for="timeFrom">С</label>
            <input type="time" name="timeFrom" id="timeFrom" value="${mealFilter.timeFrom}"/>
            <label for="timeTo">По</label>
            <input type="time" name="timeTo" id="timeTo" value="${mealFilter.timeTo}"/>
            <button type="submit">Отфильтровать</button>
        </form>
    </div>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>