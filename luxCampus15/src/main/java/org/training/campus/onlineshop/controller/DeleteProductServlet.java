package org.training.campus.onlineshop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.training.campus.onlineshop.entity.Product;

@WebServlet(name = "DeleteProductServlet", description = "deletes selected product", loadOnStartup = 1, urlPatterns = "/products/delete")
public class DeleteProductServlet extends AbstractServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			deleteProduct(req);
			fetchProducts(req.getSession());
			resp.sendRedirect(req.getContextPath()+"/products");
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void deleteProduct(HttpServletRequest req) throws ServletException {
		String itemParameter = req.getParameter(ITEM_PARAMETER);
		if (itemParameter == null)
			throw new ServletException("missing item parameter for product to delete");

		List<Product> products = (List<Product>) req.getSession().getAttribute(PRODUCTS_ATTRIBUTE);
		if (products == null)
			throw new ServletException("missing product list attribute");

		int index = Integer.parseInt(itemParameter);
		if (index < 0 || index > products.size())
			throw new ServletException(
					String.format("wrong index %d, should be within [0,%d]", index, products.size() - 1));

		Product toDelete = products.get(index);
		getProductDao().remove(toDelete);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
