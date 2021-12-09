package org.training.campus.onlineshop.dao;

import java.util.List;

import org.training.campus.onlineshop.entity.Product;

public interface ProductDao {
	
	List<Product> getAll();
	void persist(Product product);
	void merge(Product product);
	void remove(Product product);
	void remove(long id);

}
