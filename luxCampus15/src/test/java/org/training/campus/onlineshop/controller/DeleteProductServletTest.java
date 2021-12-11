package org.training.campus.onlineshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class DeleteProductServletTest extends AbstractDAOServletTest {

	@Captor
	private ArgumentCaptor<? extends List<Product>> productListCaptor;
	@Captor
	private ArgumentCaptor<String> urlCaptor;

	DeleteProductServletTest() {
		super(DeleteProductServlet.class);
	}

	@Test
	void doGetFetchesProductListAndForwards() {
		try {
			Product p0, p1, p2;
			List<Product> sampleList = new LinkedList<>(List.of(
					p0 = new Product(1L, "Spacesuit", BigDecimal.valueOf(1_000_000.00D), LocalDate.of(1999, 07, 10)),
					p1 = new Product(2L, "Meteorite", BigDecimal.valueOf(100_000.00D), LocalDate.of(1980, 05, 15)),
					p2 = new Product(3L, "MarsTesla", BigDecimal.valueOf(10_000_000.00D), LocalDate.of(2020, 01, 01))));
			when(req.getParameter(AbstractServlet.ITEM_PARAMETER)).thenReturn("1");
			when(session.getAttribute(AbstractServlet.PRODUCTS_ATTRIBUTE)).thenReturn(sampleList);
			doAnswer(invocation -> {
				Product p = (Product) invocation.getArgument(0);
				sampleList.remove(p);
				return null;
			}).when(daoMock).remove(any(Product.class));
			when(daoMock.getAll()).thenReturn(sampleList);

			servlet.doGet(req, resp);

			verify(session).setAttribute(eq(AbstractServlet.PRODUCTS_ATTRIBUTE), productListCaptor.capture());
			assertNotNull(productListCaptor.getValue());
			assertEquals(2, productListCaptor.getValue().size());
			assertSame(p0, productListCaptor.getValue().get(0));
			assertSame(p2, productListCaptor.getValue().get(1));

			verify(resp).sendRedirect(urlCaptor.capture());
			assertNotNull(urlCaptor.getValue());
			assertTrue(urlCaptor.getValue().endsWith(DeleteProductServlet.REDIRECTION_RESOURCE));

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

}
