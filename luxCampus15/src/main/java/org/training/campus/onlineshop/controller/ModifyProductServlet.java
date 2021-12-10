package org.training.campus.onlineshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ModifyProductServlet", description = "modifies product", loadOnStartup = 1, urlPatterns = "/products/edit")
public class ModifyProductServlet extends AbstractServlet {

	protected static final String REDIRECTION_RESOURCE = "/WEB-INF/pages/new-edit-product.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().setAttribute(CREATE_PRODUCT_ATTRIBUTE, Boolean.FALSE);
		req.getSession().setAttribute(PRODUCT_ATTRIBUTE, getProductFromList(req));
		getServletContext().getRequestDispatcher(REDIRECTION_RESOURCE).forward(req, resp);
	}

}
