<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<jsp:useBean id="mealTo" type="ru.javawebinar.topjava.to.MealTo" scope="request"/>
<c:set var="isCreate" value="${mealTo.id == null}"/>
<section>
    <h2><spring:message code="${isCreate ? 'meal.add' : 'meal.edit'}"/></h2>
    <form method="post" action="meals/${isCreate ? 'create' : 'update'}">
        <input type="hidden" name="id" value="${mealTo.id}">
        <dl>
            <dt><spring:message code="meal.datetime"/>:</dt>
            <dd><input type="datetime-local" value="${mealTo.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${mealTo.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${mealTo.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="button.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="button.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
