package org.training.campus.onlineshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class SaveProductServletTest extends AbstractDAOServletTest {

	@Captor
	private ArgumentCaptor<String> urlCaptor;

	SaveProductServletTest() {
		super(SaveProductServlet.class);
	}

	@Test
	void doGetSavesNewProductAndForwards() {
		try {
			final long id = 100L;
			final String name = "Car";
			final BigDecimal price = BigDecimal.valueOf(100_000D);
			final LocalDate date = LocalDate.of(2020, 01, 01);
			final Product product = new Product();
			when(session.getAttribute(AbstractServlet.CREATE_PRODUCT_ATTRIBUTE)).thenReturn(Boolean.TRUE);
			when(session.getAttribute(AbstractServlet.PRODUCT_ATTRIBUTE)).thenReturn(product);
			when(req.getParameter(AbstractServlet.PRODUCT_NAME_PARAMETER)).thenReturn(name);
			when(req.getParameter(AbstractServlet.PRODUCT_PRICE_PARAMETER)).thenReturn(price.toString());
			when(req.getParameter(AbstractServlet.PRODUCT_CREATION_DATE_PARAMETER)).thenReturn(date.toString());
			doAnswer(invocation -> {
				Product p = (Product) invocation.getArgument(0);
				p.setId(id);
				return null;
			}).when(daoMock).persist(any(Product.class));

			servlet.doGet(req, resp);

			assertEquals(id, product.getId());
			assertEquals(name, product.getName());
			assertEquals(price, product.getPrice());
			assertEquals(date, product.getCreationDate());

			verify(resp).sendRedirect(urlCaptor.capture());
			assertNotNull(urlCaptor.getValue());
			assertTrue(urlCaptor.getValue().endsWith(SaveProductServlet.REDIRECTION_RESOURCE));

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

	@Test
	void doGetUpdatesExistingProductAndForwards() {
		try {
			final long id = 100L;
			final Product product = Product.builder().id(id).name("Lorry").price(BigDecimal.valueOf(200_000D))
					.creationDate(LocalDate.of(2019, 01, 01)).build();
			final String name = "Car";
			final BigDecimal price = BigDecimal.valueOf(100_000D);
			final LocalDate date = LocalDate.of(2020, 01, 01);
			when(session.getAttribute(AbstractServlet.CREATE_PRODUCT_ATTRIBUTE)).thenReturn(Boolean.FALSE);
			when(session.getAttribute(AbstractServlet.PRODUCT_ATTRIBUTE)).thenReturn(product);
			when(req.getParameter(AbstractServlet.PRODUCT_NAME_PARAMETER)).thenReturn(name);
			when(req.getParameter(AbstractServlet.PRODUCT_PRICE_PARAMETER)).thenReturn(price.toString());
			when(req.getParameter(AbstractServlet.PRODUCT_CREATION_DATE_PARAMETER)).thenReturn(date.toString());
			doAnswer(invocation -> {
				Product p = (Product) invocation.getArgument(0);
				p.setName(name);
				p.setPrice(price);
				p.setCreationDate(date);
				return null;
			}).when(daoMock).merge(any(Product.class));

			servlet.doGet(req, resp);

			assertEquals(id, product.getId());
			assertEquals(name, product.getName());
			assertEquals(price, product.getPrice());
			assertEquals(date, product.getCreationDate());

			verify(resp).sendRedirect(urlCaptor.capture());
			assertNotNull(urlCaptor.getValue());
			assertTrue(urlCaptor.getValue().endsWith(SaveProductServlet.REDIRECTION_RESOURCE));

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

}
