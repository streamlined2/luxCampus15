<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<title>List of products</title>
<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}
th, td {
	padding: 5px;
}
</style>
</head>
<body>
	<table>
		<caption>
			<h3>Products by name</h3>
		</caption>
		<thead>
			<tr>
				<th>Name</th>
				<th>Price</th>
				<th>Created</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${products}" var="product" varStatus="status">
				<tr>
					<td>${product.name}</td>
					<td>${product.price}</td>
					<td>${product.creationDate}</td>
					<td><a href=products/edit?item=${status.index}>Edit</a></td>
					<td><a href=products/delete?item=${status.index}>Delete</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<a href=products/add>Create new</a>
</body>
</html>