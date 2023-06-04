package com.example.springweb.repository;

import com.example.springweb.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
    Optional<Product> findByTitle(String title);
    List<Product> findProductsByTitleContainingIgnoreCase(String title);
    @Modifying
    @Query("SELECT p FROM products p WHERE p.user.id = :userId")
    List<Product> findProductsByUserId(Long userId);
}
