package com.pamungkasaji.beshop.controller;

import com.pamungkasaji.beshop.entity.Product;
import com.pamungkasaji.beshop.exceptions.ProductServiceException;
import com.pamungkasaji.beshop.model.response.GenericResponse;
import com.pamungkasaji.beshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "limit", defaultValue = "15") int limit,
                                                                  @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        if (keyword.isEmpty()){
            HashMap<String, Object> productList = productService.getAllProducts(page, limit);
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } else {
            HashMap<String, Object> productList = productService.getSearchProducts(keyword, page, limit);
//            List<Product> productList = productService.getSearchProducts(keyword);
            return new ResponseEntity<>(productList, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@PathVariable String id) {

        Optional<Product> product = productService.getProductByProductId(id);
        if (!product.isPresent()) {
            throw new ProductServiceException("Product with id (" + id + ") not found!");
        }
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product newProduct) {

        return new ResponseEntity<>(productService.createProduct(newProduct), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @Valid @RequestBody Product updateProduct) {

        return new ResponseEntity<>(productService.updateProduct(id, updateProduct), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    GenericResponse deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new GenericResponse("Product deleted");
    }
}
