<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, java.text.*" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>

<%!
    String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        return sdf.format(new Date());
    }
%>
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
<h3><a href="index.html">my new page</a></h3>
<hr>

<i>моё имя ${name}Сегодня <%= getFormattedDate() %>
</i>
<table style="width:100%">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <% List<MealTo> meals = (List<MealTo>) request.getAttribute("list");
        for (MealTo meal : meals) {
            String color = meal.isExcess() ? "red" : "green";%>
    <tr>
        <td style="color: <%= color %>;"><%= meal.getDateTime() %>
        </td>
        <td style="color: <%= color %>;"><%= meal.getDescription() %>
        </td>
        <td style="color: <%= color %>;"><%= meal.getCalories() %>
        </td>
        <td><a><%= "Update" %>
        </a></td>
        <td><a><%= "Delete" %>
        </a></td>
    </tr>
    <% } %>


</table>

</body>
</html>