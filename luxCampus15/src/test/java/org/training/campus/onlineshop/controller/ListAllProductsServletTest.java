package org.training.campus.onlineshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.dao.JdbcProductDao;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class ListAllProductsServletTest extends AbstractServletTest {

	@Mock
	private JdbcProductDao daoMock;

	@Captor
	private ArgumentCaptor<Product> productListCaptor;

	ListAllProductsServletTest() {
		servlet = mock(ListAllProductsServlet.class, Answers.CALLS_REAL_METHODS);
	}

	@Test
	void doGetFetchesProductListAndForwards() {
		try {
			List<Product> sampleList = List.of(
					new Product(1L, "Spacesuit", BigDecimal.valueOf(1_000_000.00D), LocalDate.of(1999, 07, 10)),
					new Product(2L, "Meteorite", BigDecimal.valueOf(100_000.00D), LocalDate.of(1980, 05, 15)),
					new Product(3L, "MarsTesla", BigDecimal.valueOf(10_000_000.00D), LocalDate.of(2020, 01, 01)));
			List<Product> copyList = List.copyOf(sampleList);
			when(daoMock.getAll()).thenReturn(sampleList);
			when(context.getAttribute(AbstractServlet.PRODUCT_DAO)).thenReturn(daoMock);

			servlet.doGet(req, resp);

			verify(session).setAttribute(eq(AbstractServlet.PRODUCTS_ATTRIBUTE), productListCaptor.capture());
			assertNotNull(productListCaptor.getValue());
			assertSame(sampleList, productListCaptor.getValue());
			assertEquals(copyList, productListCaptor.getValue());

			verify(context).getRequestDispatcher(ListAllProductsServlet.REDIRECTION_RESOURCE);
			verify(requestDispatcher).forward(req, resp);

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

}
