package org.training.campus.onlineshop.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.training.campus.onlineshop.entity.Product;

public class ProductRowMapper {

	public Product mapRowToProduct(ResultSet rowSet) throws SQLException {
		return Product.builder().id(rowSet.getLong("id")).name(rowSet.getString("name"))
				.price(rowSet.getBigDecimal("price"))
				.creationDate(rowSet.getDate("creation_date").toLocalDate()).build();
	}

	public int fillInStatementValues(PreparedStatement stmt, Product product) throws SQLException {
		int index = 1;
		stmt.setString(index++, product.getName());
		stmt.setBigDecimal(index++, product.getPrice());
		stmt.setDate(index++, Date.valueOf(product.getCreationDate()));
		return index;
	}

	public void update(Product product, String name, BigDecimal price, LocalDate creationDate) {
		product.setName(name);
		product.setPrice(price);
		product.setCreationDate(creationDate);
	}

}
