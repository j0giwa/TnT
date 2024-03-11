<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Suchergebnisse</title>
    </head>
    <body>
        <h1>Suchergebnisse</h1>

        <table>
            <tr>
                <th>Title</th>
                <th>Category</th>
                <th>Content</th>
            </tr>
            <c:forEach items="${matchingNotes}" var="note">
                <tr>
                    <td>${note.title}</td>
                    <td>${note.category}</td>
                    <td>${note.content}</td>
                </tr>
            </c:forEach>
        </table>

    </body>
</html>