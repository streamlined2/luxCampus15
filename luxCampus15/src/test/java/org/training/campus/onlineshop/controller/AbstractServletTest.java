package org.training.campus.onlineshop.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class AbstractServletTest {

	@Mock
	protected HttpServletRequest req;
	@Mock
	protected HttpServletResponse resp;
	@Mock
	protected HttpSession session;
	@Mock
	protected ServletContext context;
	@Mock
	protected ServletConfig config;
	@Mock
	protected RequestDispatcher requestDispatcher;

	protected AbstractServlet servlet;
	
	AbstractServletTest(Class<? extends AbstractServlet> cl){
		servlet = mock(cl, Answers.CALLS_REAL_METHODS);		
	}

	@BeforeEach
	void setUp() throws Exception {
		when(req.getSession()).thenReturn(session);
		when(config.getServletContext()).thenReturn(context);
		when(servlet.getServletConfig()).thenReturn(config);
		when(servlet.getServletContext()).thenReturn(context);
		lenient().when(context.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
	}

}
