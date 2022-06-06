<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<style>
    table, th, td {
        border: 1px solid black;
    }
    td {
        text-align: center;
    }
</style>
<head>
    <title>meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table style="width:100%">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${listMealTo}">
        <tr style="color: ${meal.excess ? 'red' :'green'}">
            <td>${DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(meal.getDateTime())}
            </td>
            <td>${meal.description}
            </td>
            <td >${meal.calories}
            </td>
            <td><a href="index.html">Update
            </a></td>
            <td><a href="index.html">Delete
            </a></td>
        </tr>
        <p></p>
    </c:forEach>
</table>

</body>
</html>