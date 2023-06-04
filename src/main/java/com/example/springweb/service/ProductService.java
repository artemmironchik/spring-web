package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.exception.MainException;
import com.example.springweb.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public ProductService(ProductRepository graveRepository, UserService userService) {
        this.productRepository = graveRepository;
        this.userService = userService;
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product findById(Long id) throws MainException {
        Optional<Product> candidate = productRepository.findById(id);
        if(candidate.isEmpty()){
            throw new MainException("Product with such id doesn't exist");
        }
        return candidate.get();
    }

    public List<Product> findProductsBySearch(String title) throws MainException{
        List<Product> products = productRepository.findProductsByTitleContainingIgnoreCase(title);
        if(products.isEmpty()){
            throw new MainException("Nothing found");
        }
        return products;
    }

    public void saveProduct(Product product, Long userId) throws MainException {
        Optional<Product> candidate = productRepository.findByTitle(product.getTitle());
        List<Product> products = productRepository.findProductsByUserId(userId);
        if(candidate.isPresent()){
            if(products.contains(candidate.get()))
                throw new MainException("Product with this title already exist!");
        }
        userService.setUser(product, userId);
        productRepository.save(product);
    }

    public void buyProduct(Long id) {
        productRepository.deleteById(id);
    }
}
