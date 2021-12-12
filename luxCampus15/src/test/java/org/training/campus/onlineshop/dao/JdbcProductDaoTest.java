package org.training.campus.onlineshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.training.campus.onlineshop.entity.Product;

@ExtendWith(MockitoExtension.class)
class JdbcProductDaoTest {

	@Mock
	private DataSource dataSource;
	@Mock
	private Connection conn;
	@Mock
	private PreparedStatement prepStmt;
	@Mock
	private Statement stmt;
	@Mock
	private ResultSet resultSet;

	@InjectMocks
	private JdbcProductDao dao;

	@BeforeEach
	void setUp() throws SQLException {
		when(dataSource.getConnection()).thenReturn(conn);
		lenient().when(conn.createStatement()).thenReturn(stmt);
		lenient().when(conn.prepareStatement(anyString())).thenReturn(prepStmt);
		lenient().when(conn.prepareStatement(anyString(), anyInt())).thenReturn(prepStmt);
	}

	@Test
	void testGetAll_ResultContainsOneProduct() throws SQLException {
		when(stmt.executeQuery(JdbcProductDao.FETCH_ALL_STATEMENT)).thenReturn(resultSet);

		final Product product = Product.builder().id(1L).name("Car").price(BigDecimal.valueOf(100_000D))
				.creationDate(LocalDate.of(2020, 1, 1)).build();

		when(resultSet.next()).thenReturn(true, false);
		lenient().when(resultSet.getLong("id")).thenReturn(product.getId());
		lenient().when(resultSet.getString("name")).thenReturn(product.getName());
		lenient().when(resultSet.getBigDecimal("price")).thenReturn(product.getPrice());
		lenient().when(resultSet.getDate("creation_date")).thenReturn(Date.valueOf(product.getCreationDate()));

		List<Product> productList = dao.getAll();

		assertEquals(1, productList.size());
		assertEquals(List.of(product), productList);

	}

	@Test
	void testGetAll_ResultIsEmpty() throws SQLException {
		when(stmt.executeQuery(JdbcProductDao.FETCH_ALL_STATEMENT)).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		List<Product> productList = dao.getAll();

		assertTrue(productList.isEmpty());

	}

	@Test
	void testGetAll_ExceptionThrownWhileExecutingQuery() throws SQLException {
		when(stmt.executeQuery(JdbcProductDao.FETCH_ALL_STATEMENT)).thenThrow(SQLException.class);

		assertThrows(DataAccessException.class, () -> {
			dao.getAll();
		});

	}

	@Test
	void testGetAll_ExceptionThrownWhileFetchingFirstEntity() throws SQLException {
		when(stmt.executeQuery(JdbcProductDao.FETCH_ALL_STATEMENT)).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, false);
		when(resultSet.getLong("id")).thenThrow(SQLException.class);

		assertThrows(DataAccessException.class, () -> {
			dao.getAll();
		});

	}

	@Test
	void testPersist_EntitySavedSuccessfully() throws SQLException {
		final long id = 1000L;
		final String name = "Rocket";
		final BigDecimal price = BigDecimal.valueOf(500_000.00D);
		final LocalDate creationDate = LocalDate.of(1980, 1, 1);
		final Product product = Product.builder().name(name).price(price).creationDate(creationDate).build();

		assertEquals(0, product.getId());

		ResultSet keySet = mock(ResultSet.class);
		when(prepStmt.getGeneratedKeys()).thenReturn(keySet);
		when(keySet.next()).thenReturn(true, false);

		doAnswer(invocation -> {
			product.setId(id);
			return null;
		}).when(conn).commit();

		dao.persist(product);

		InOrder inOrder = inOrder(conn, prepStmt, keySet);
		inOrder.verify(conn).setAutoCommit(false);
		inOrder.verify(prepStmt).executeUpdate();
		inOrder.verify(prepStmt).getGeneratedKeys();
		inOrder.verify(keySet).next();
		inOrder.verify(conn).commit();

		assertEquals(id, product.getId());
		assertEquals(name, product.getName());
		assertEquals(price, product.getPrice());
		assertEquals(creationDate, product.getCreationDate());

	}

	@Test
	void testPersist_NoPrimaryKeyForEntityGenerated() throws SQLException {
		final String name = "Rocket";
		final BigDecimal price = BigDecimal.valueOf(500_000.00D);
		final LocalDate creationDate = LocalDate.of(1980, 1, 1);
		final Product product = Product.builder().name(name).price(price).creationDate(creationDate).build();

		assertEquals(0, product.getId());

		ResultSet keySet = mock(ResultSet.class);
		when(prepStmt.getGeneratedKeys()).thenReturn(keySet);
		when(keySet.next()).thenReturn(false);

		assertThrows(DataAccessException.class, () -> {
			dao.persist(product);
		});

		InOrder inOrder = inOrder(conn, prepStmt, keySet);
		inOrder.verify(conn).setAutoCommit(false);
		inOrder.verify(prepStmt).executeUpdate();
		inOrder.verify(prepStmt).getGeneratedKeys();
		inOrder.verify(keySet).next();

		assertEquals(name, product.getName());
		assertEquals(price, product.getPrice());
		assertEquals(creationDate, product.getCreationDate());

	}

	@Test
	void testPersist_ExceptionThrownWhileFetchingKeys() throws SQLException {
		final String name = "Rocket";
		final BigDecimal price = BigDecimal.valueOf(500_000.00D);
		final LocalDate creationDate = LocalDate.of(1980, 1, 1);
		final Product product = Product.builder().name(name).price(price).creationDate(creationDate).build();

		assertEquals(0, product.getId());

		when(prepStmt.getGeneratedKeys()).thenThrow(SQLException.class);

		assertThrows(DataAccessException.class, () -> {
			dao.persist(product);
		});

		InOrder inOrder = inOrder(conn, prepStmt);
		inOrder.verify(conn).setAutoCommit(false);
		inOrder.verify(prepStmt).executeUpdate();
		inOrder.verify(prepStmt).getGeneratedKeys();

		assertEquals(name, product.getName());
		assertEquals(price, product.getPrice());
		assertEquals(creationDate, product.getCreationDate());

	}

	@Test
	void testPersist_ExceptionThrownWhileRollbackingTransaction() throws SQLException {
		final long id = 1000L;
		final String name = "Rocket";
		final BigDecimal price = BigDecimal.valueOf(500_000.00D);
		final LocalDate creationDate = LocalDate.of(1980, 1, 1);
		final Product product = Product.builder().name(name).price(price).creationDate(creationDate).build();

		assertEquals(0, product.getId());

		ResultSet keySet = mock(ResultSet.class);
		when(prepStmt.getGeneratedKeys()).thenReturn(keySet);
		when(keySet.next()).thenReturn(true, false);

		doAnswer(invocation -> {
			product.setId(id);
			return null;
		}).when(conn).commit();
		doThrow(SQLException.class).when(conn).rollback();

		dao.persist(product);

		InOrder inOrder = inOrder(conn, prepStmt, keySet);
		inOrder.verify(conn).setAutoCommit(false);
		inOrder.verify(prepStmt).executeUpdate();
		inOrder.verify(prepStmt).getGeneratedKeys();
		inOrder.verify(keySet).next();
		inOrder.verify(conn).commit();

		assertEquals(id, product.getId());
		assertEquals(name, product.getName());
		assertEquals(price, product.getPrice());
		assertEquals(creationDate, product.getCreationDate());

	}

}
