package org.training.campus.onlineshop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.training.campus.onlineshop.entity.Product;

public class ProductRowMapper {

	public Product mapRowToProduct(ResultSet rowSet) throws SQLException {
		return Product.builder().
				id(rowSet.getLong("id")).
				name(rowSet.getString("name")).
				price(rowSet.getBigDecimal("price")).
				creationDate(LocalDate.from(rowSet.getDate("creation_date").toLocalDate())).
				build();
	}

}
