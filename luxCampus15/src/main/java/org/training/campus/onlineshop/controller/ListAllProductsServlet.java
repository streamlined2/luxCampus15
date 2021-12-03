package org.training.campus.onlineshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ListAllProductsServlet", description = "lists all products", loadOnStartup = 1, urlPatterns = {
		"/products", "/" })
public class ListAllProductsServlet extends AbstractServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		fetchProducts(req);
		getServletContext().getRequestDispatcher("/WEB-INF/pages/product-list.jsp").forward(req, resp);
	}

}
