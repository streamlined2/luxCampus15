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
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class ModifyProductServletTest extends AbstractServletTest {

	@Captor
	private ArgumentCaptor<Product> productCaptor;

	ModifyProductServletTest() {
		servlet = mock(ModifyProductServlet.class, Answers.CALLS_REAL_METHODS);
	}

	@Test
	void doGetSetsUpCorrectAttributesAndForwards() {
		try {
			final Product sample = Product.builder().id(10L).name("Most beautiful picture ever")
					.price(BigDecimal.valueOf(1_000_000.00D)).creationDate(LocalDate.now()).build();
			when(req.getParameter(AbstractServlet.ITEM_PARAMETER)).thenReturn("0");
			when(session.getAttribute(AbstractServlet.PRODUCTS_ATTRIBUTE)).thenReturn(List.of(sample));

			servlet.doGet(req, resp);

			verify(session).setAttribute(AbstractServlet.CREATE_PRODUCT_ATTRIBUTE, Boolean.FALSE);
			verify(session).setAttribute(eq(AbstractServlet.PRODUCT_ATTRIBUTE), productCaptor.capture());
			assertNotNull(productCaptor.getValue());
			assertSame(sample, productCaptor.getValue());

			verify(context).getRequestDispatcher(CreateProductServlet.REDIRECTION_RESOURCE);
			verify(requestDispatcher).forward(req, resp);

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

}
