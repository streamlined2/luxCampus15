package org.training.campus.onlineshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.IOException;

import javax.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class CreateProductServletTest extends AbstractServletTest {

	@Captor
	private ArgumentCaptor<Product> productCaptor;

	CreateProductServletTest() {
		servlet = mock(CreateProductServlet.class, Answers.CALLS_REAL_METHODS);
	}

	@Test
	void doGetSetsUpCorrectAttributesAndForwards() {
		try {
			servlet.doGet(req, resp);

			verify(session).setAttribute(AbstractServlet.CREATE_PRODUCT_ATTRIBUTE, Boolean.TRUE);

			verify(session).setAttribute(eq(AbstractServlet.PRODUCT_ATTRIBUTE), productCaptor.capture());
			assertNotNull(productCaptor.getValue());
			assertEquals(0L, productCaptor.getValue().getId());
			assertNull(productCaptor.getValue().getName());
			assertNull(productCaptor.getValue().getPrice());
			assertNull(productCaptor.getValue().getCreationDate());

			verify(context).getRequestDispatcher(CreateProductServlet.REDIRECTION_RESOURCE);
			verify(requestDispatcher).forward(req, resp);

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

}
