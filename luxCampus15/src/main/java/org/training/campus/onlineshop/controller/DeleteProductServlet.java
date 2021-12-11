package org.training.campus.onlineshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.training.campus.onlineshop.entity.Product;

@WebServlet(name = "DeleteProductServlet", description = "deletes selected product", loadOnStartup = 1, urlPatterns = "/products/delete")
public class DeleteProductServlet extends AbstractServlet {
	
	protected static final String REDIRECTION_RESOURCE = "/products";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			deleteProduct(req);
			fetchProducts(req);
			resp.sendRedirect(req.getContextPath() + REDIRECTION_RESOURCE);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void deleteProduct(HttpServletRequest req) throws ServletException {
		Product toDelete = getProductFromList(req);
		getProductDao().remove(toDelete);
	}

}
