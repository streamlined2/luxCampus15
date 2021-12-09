package org.training.campus.onlineshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.training.campus.onlineshop.entity.Product;

public class JdbcProductDao implements ProductDao {

	private DataSource dataSource;
	private ProductRowMapper mapper;

	public JdbcProductDao(DataSource dataSource) {
		this.dataSource = dataSource;
		mapper = new ProductRowMapper();
	}

	public List<Product> getAll() {
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt
						.executeQuery("select id, name, price, creation_date from product order by name asc")) {

			List<Product> products = new ArrayList<>();
			while (resultSet.next()) {
				products.add(mapper.mapRowToProduct(resultSet));
			}
			return products;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	public void persist(Product product) {
		try(Connection conn = dataSource.getConnection()){
			conn.setAutoCommit(false);			
			try (PreparedStatement stmt = conn.prepareStatement(
					"insert into product (name, price, creation_date) values(?,?,?)",
					Statement.RETURN_GENERATED_KEYS)) {

				mapper.fillInStatementValues(stmt, product);
				stmt.executeUpdate();
				try (ResultSet keySet = stmt.getGeneratedKeys()) {
					if (keySet.next()) {
						product.setId(keySet.getLong(1));
						conn.commit();
					} else {
						throw new DataAccessException("no primary key for new tuple");
					}
				}
			} finally {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	public void merge(Product product) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("update product set name=?, price=?, creation_date=? where id=?")) {

			final int index = mapper.fillInStatementValues(stmt, product);
			stmt.setLong(index, product.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	public void remove(Product product) {
		remove(product.getId());
	}

	public void remove(long id) {
		try (Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement("delete from product where id=?")) {

			stmt.setLong(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	public ProductRowMapper getMapper() {
		return mapper;
	}

}
