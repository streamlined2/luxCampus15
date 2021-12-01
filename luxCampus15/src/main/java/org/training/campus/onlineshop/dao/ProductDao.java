package org.training.campus.onlineshop.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.training.campus.onlineshop.entity.Product;

public class ProductDao {

	private DataSource dataSource;
	private ProductRowMapper mapper;

	public ProductDao(DataSource dataSource) {
		this.dataSource = dataSource;
		mapper = new ProductRowMapper();
	}

	public List<Product> getAll() {
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt
						.executeQuery("select id, name, price, creation_date from product order by name asc")) {//\"online-shop\".
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

}
