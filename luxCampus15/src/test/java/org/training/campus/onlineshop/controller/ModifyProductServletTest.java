package org.training.campus.onlineshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class ModifyProductServletTest {

	@Mock
	private HttpServletRequest req;
	@Mock
	private HttpServletResponse resp;
	@Mock
	private HttpSession session;
	@Mock
	private ServletContext context;
	@Mock
	private ServletConfig config;
	@Mock
	private RequestDispatcher requestDispatcher;
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private ModifyProductServlet servlet;
	@Captor
	private ArgumentCaptor<Product> productCaptor;

	@BeforeEach
	void setUp() throws Exception {
		when(req.getSession()).thenReturn(session);
		when(servlet.getServletConfig()).thenReturn(config);
		when(config.getServletContext()).thenReturn(context);
		when(servlet.getServletContext()).thenReturn(context);
		when(context.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
	}

	@Test
	void doGetSetsUpCorrectAttributesAndForwards() {
		try {
			final long id = 10L;
			final var name = "Most beautiful picture ever";
			final var price = BigDecimal.valueOf(1_000_000.00D);
			final var creationDate = LocalDate.now();
			final Product sample = Product.builder().id(id).name(name).price(price).creationDate(creationDate).build();
			when(req.getParameter(AbstractServlet.ITEM_PARAMETER)).thenReturn("0");
			when(session.getAttribute(AbstractServlet.PRODUCTS_ATTRIBUTE)).thenReturn(List.of(sample));

			servlet.doGet(req, resp);

			verify(session).setAttribute(AbstractServlet.CREATE_PRODUCT_ATTRIBUTE, Boolean.FALSE);
			verify(session).setAttribute(eq(AbstractServlet.PRODUCT_ATTRIBUTE), productCaptor.capture());
			assertNotNull(productCaptor.getValue());
			assertSame(productCaptor.getValue(), sample);

			verify(context).getRequestDispatcher(CreateProductServlet.REDIRECTION_RESOURCE);
			verify(requestDispatcher).forward(req, resp);

		} catch (ServletException | IOException e) {
			e.printStackTrace();
			fail(String.format("test exception: %s", e.getMessage()));
		}
	}

}
