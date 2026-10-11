
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="farit.dev.dto.Payment" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Налог</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Расчёт налога</h2>
<p>Текущая ставка:
    <b id="percent">${percent != null ? percent : 'нет данных'}${percent != null ? '%' : ''}</b>
</p>
<% if (request.getAttribute("error") != null) { %>
<p class="error">${error}</p>
<% } %>
<form method="post" action="${pageContext.request.contextPath}/tax">
    <label>Сколько денег получили:</label>
    <input type="text" name="income" required>
    <button type="submit">Посчитать налог</button>
</form>
<h3>История оплат</h3>
<table>
    <tr>
        <th>ID</th>
        <th>Доход</th>
        <th>Ставка %</th>
        <th>Налог</th>
        <th>Дата</th>
    </tr>
    <%
        List<Payment> payments =
                (List<Payment>) request.getAttribute("payments");

        if (payments != null) {
            for (Payment p : payments) {
    %>
    <tr>
        <td><%= p.id() %></td>
        <td><%= p.income() %></td>
        <td><%= p.percent() %></td>
        <td><%= p.tax() %></td>
        <td><%= p.createdAt() %></td>
    </tr>
    <%
            }
        }
    %>
</table>
<script>
    window.APP_CONTEXT_PATH = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/tax.js"></script>
</body>
</html>
