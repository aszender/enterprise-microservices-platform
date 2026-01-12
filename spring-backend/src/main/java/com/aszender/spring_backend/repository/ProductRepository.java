package com.aszender.spring_backend.repository;

import com.aszender.spring_backend.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

}


/* Mental Model (Lock This In)
Entity → “What is stored”
Repository → “How it is stored”
Service → “What we do with it”
Controller → “How clients access it” */