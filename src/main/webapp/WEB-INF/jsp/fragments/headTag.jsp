<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="app.title"/></title>
    <base href="${pageContext.request.contextPath}/"/>

    <link rel="stylesheet" href="resources/css/style.css?v=2">
    <link rel="stylesheet" href="webjars/bootstrap/4.6.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="webjars/noty/3.1.4/demo/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="webjars/datatables/1.11.4/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" href="webjars/noty/3.1.4/lib/noty.css"/>
    <link rel="shortcut icon" href="resources/images/icon-meal.png">

    <%--http://stackoverflow.com/a/24070373/548473--%>
    <script src="webjars/jquery/3.6.0/jquery.min.js" defer></script>
    <script src="webjars/bootstrap/4.6.1/js/bootstrap.min.js" defer></script>
    <script src="webjars/datatables/1.11.4/js/jquery.dataTables.min.js" defer></script>
    <script src="webjars/datatables/1.11.4/js/dataTables.bootstrap4.min.js" defer></script>
    <script src="webjars/noty/3.1.4/lib/noty.min.js" defer></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.8.4/moment.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/plug-ins/1.10.19/dataRender/datetime.js"></script>
</head>