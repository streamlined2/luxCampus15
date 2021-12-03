<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create/modify product</title>
</head>
<body>
	<form>
		<fieldset>
			<c:choose>
				<c:when test="${createProduct}">
					<legend>Create new product</legend>
				</c:when>
				<c:otherwise>
					<legend>Modify product</legend>
				</c:otherwise>
			</c:choose>
			<table>
				<tr>
					<td><label for="name">Name</label></td>
					<td><input type="text" id="name" name="name"
						value="${product.name}" placeholder="Enter product name" size="60"
						pattern="(\w|\s){2,100}" title="Name of product" required /></td>
				</tr>
				<tr>
					<td><label for="price">Price</label></td>
					<td><input type="number" id="price" name="price"
						value="${product.price}" placeholder="Enter product price" min="0"
						size="15" title="Price of product" required /></td>
				</tr>
				<tr>
					<td><label for="creationDate">Produced</label></td>
					<td><input type="date" id="creationDate" name="creationDate"
						value="${product.creationDate}"
						placeholder="Enter date of production" title="Date of production"
						required /></td>
				</tr>
			</table>
			<hr>
			<c:choose>
				<c:when test="${createProduct}">
					<input type="submit" value="Create"
						formaction=<c:url value="/saveproduct"/> formmethod="post" />
				</c:when>
				<c:otherwise>
					<input type="submit" value="Save"
						formaction=<c:url value="/saveproduct"/> formmethod="post" />
				</c:otherwise>
			</c:choose>
			<input type="reset" value="Reset" /> <input type="submit"
				value="Cancel" formaction=<c:url value="/products"/>
				formmethod="post" formnovalidate />
		</fieldset>
	</form>
</body>
</html>