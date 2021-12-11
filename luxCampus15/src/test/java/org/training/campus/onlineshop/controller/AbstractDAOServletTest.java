package org.training.campus.onlineshop.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.dao.JdbcProductDao;

@ExtendWith(MockitoExtension.class)
abstract class AbstractDAOServletTest extends AbstractServletTest {

	@Mock
	protected JdbcProductDao daoMock;

	AbstractDAOServletTest(Class<? extends AbstractServlet> cl) {
		super(cl);
	}

	@BeforeEach
	void setUp() throws Exception {
		super.setUp();
		when(context.getAttribute(AbstractServlet.PRODUCT_DAO)).thenReturn(daoMock);
	}

}
