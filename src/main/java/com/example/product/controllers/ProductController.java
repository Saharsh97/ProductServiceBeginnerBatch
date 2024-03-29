package com.example.product.controllers;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.ProductWrapper;
import com.example.product.exceptions.ProductDoesNotExistException;
import com.example.product.models.Category;
import com.example.product.models.Product;
import com.example.product.services.FakeStoreProductService;
import com.example.product.services.IProductService;
import com.example.product.exceptions.InvalidProductIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {


    private IProductService productService;


    @Autowired
    public ProductController(@Qualifier("selfProductService") IProductService productService, FakeStoreProductService fakeStoreProductService){
        this.productService = productService;
    }

    // Get all the products
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get a product with Id
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductWrapper> getSingleProduct(@PathVariable("id") Long id) throws InvalidProductIdException {

        ResponseEntity<ProductWrapper> response;
        Product singleProduct = productService.getSingleProduct(id);

        ProductWrapper productWrapper = new ProductWrapper(singleProduct, "Successfully retried the data");
        response = new ResponseEntity<>(productWrapper, HttpStatus.OK);
        return response;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getTitle());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setImage(productRequestDto.getImage());
        product.setCategory(new Category());
        product.getCategory().setName(productRequestDto.getCategory());

        Product savedProduct = productService.addProduct(product);
        return savedProduct;
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable("id") Long id,
                                 @RequestBody ProductRequestDto productRequestDto) throws ProductDoesNotExistException {

        Product product = new Product();
        product.setId(id);
        product.setName(productRequestDto.getTitle());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setImage(productRequestDto.getImage());
        product.setCategory(new Category());
        product.getCategory().setName(productRequestDto.getCategory());

        return productService.updateProduct(id, product);
    }


    @DeleteMapping("/products/{id}")
    public boolean deleteProduct(@PathVariable("id") Long id) {
        return true;
    }


}
