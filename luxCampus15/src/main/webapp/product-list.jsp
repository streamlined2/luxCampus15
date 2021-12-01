<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<title>List of products</title>
</head>
<body>
	<h3>Products by name</h3>
	<table>
		<thead>
			<th>Name</th>
			<th>Price</th>
			<th>Created</th>
		</thead>
		<tbody>
			<c:forEach items="${products}" var="product">
				<tr>
					<td>${product.name}</td>
					<td>${product.price}</td>
					<td>${product.creationDate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>