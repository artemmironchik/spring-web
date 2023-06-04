package com.example.springweb.controller;

import com.example.springweb.dto.UserDto;
import com.example.springweb.entity.Product;
import com.example.springweb.exception.MainException;
import com.example.springweb.service.FileService;
import com.example.springweb.service.ProductService;
import com.example.springweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class ProductController {
    private ProductService productService;
    private UserService userService;
    private FileService fileService;

    @GetMapping("/cart")
    public String getCart(Model model, HttpServletRequest request){
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        model.addAttribute("user", userDto);
        log.info("user", userDto);
        List<Product> products = userService.findUserProducts(userDto.getId());
        model.addAttribute("products", products);
        log.info("products", products);
        return "cart";
    }

    @GetMapping("/products/create")
    public String getProductCreatorPage(Model model, HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("product", new Product());
        log.info("Get create product page");
        return "createProduct";
    }

    @PostMapping("/products/create")
    public String createProduct(@RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("photo") MultipartFile photo,
                                HttpServletRequest request,
                                Model model) throws MainException {
        String filename = fileService.saveFile(photo);
        Product product = new Product(title, description, filename);
        log.info("Product: ", product);
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        try {
            productService.saveProduct(product, userDto.getId());
            log.info("Product is saved");
        } catch (MainException e){
            model.addAttribute("error", e.getMessage());
            log.info("Error: ", e.getMessage());
            return "createProduct";
        }
        return "redirect:/cart";
    }

    @GetMapping("/products/{id}/buy")
    public String buyProduct(@PathVariable("id") Long id) {
        productService.buyProduct(id);
        log.info("Product has been successfully bought");
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String getProductsPage(Model model, HttpServletRequest request) {
        log.info("Get products page");
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        List<Product> products = productService.getProducts();
        log.info("Products: ", products);
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping("/products")
    public String searchProducts(@RequestParam("search") String search, Model model, HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        List<Product> allProducts;
        try {
            allProducts = search.isEmpty() ? productService.getProducts() : productService.findProductsBySearch(search);
            log.info("Searched products: ", allProducts);
            model.addAttribute("products", allProducts);
        } catch (MainException e){
            model.addAttribute("error", e.getMessage());
            log.info("Error: ", e.getMessage());
        }
        return "products";
    }

    @GetMapping("products/{id}")
    public String getProductPage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Model model) {
        log.info("Get product page");
        try {
            Product product = productService.findById(id);
            log.info("Product: ", product);
            redirectAttributes.addFlashAttribute("product", product);
        } catch (MainException e){
            model.addAttribute("error", e.getMessage());
            log.info("Error: ", e.getMessage());
        }
        return "redirect:/product";
    }
}
