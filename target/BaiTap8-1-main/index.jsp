<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn"  uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css" type="text/css"/>
</head>
<body>

<%-- Lấy sqlStatement từ session; nếu trống thì set mặc định --%>
<c:choose>
    <c:when test="${not empty sessionScope.sqlStatement}">
        <c:set var="sqlStatement" value="${sessionScope.sqlStatement}" scope="page"/>
    </c:when>
    <c:otherwise>
        <c:set var="sqlStatement" value="SELECT * FROM users" scope="page"/>
    </c:otherwise>
</c:choose>

<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the Execute button.</p>

<p><b>SQL statement:</b></p>
<form action="${pageContext.request.contextPath}/sqlgateway" method="post">

    <textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea><br>
    <input type="submit" value="Execute">
</form>

<p><b>SQL result:</b></p>
<%-- sqlResult là HTML table; đừng escape --%>
<c:out value="${sessionScope.sqlResult}" escapeXml="false"/>

</body>
</html>
