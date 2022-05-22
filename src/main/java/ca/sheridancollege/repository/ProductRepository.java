package ca.sheridancollege.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.groceryapp.beans.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{}
