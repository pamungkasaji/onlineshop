package com.pamungkasaji.beshop.controller;

import com.pamungkasaji.beshop.dto.UserDto;
import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.exceptions.ProductServiceException;
import com.pamungkasaji.beshop.model.response.GenericResponse;
import com.pamungkasaji.beshop.model.response.user.UserDetailResponse;
import com.pamungkasaji.beshop.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "limit", defaultValue = "15") int limit) {
        HashMap<String, Object> productList = productService.getAllProducts(page, limit);

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<HashMap<String, Object>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page,
//                                                                  @RequestParam(value = "limit", defaultValue = "10") int limit) {
//        List<ProductEntity> productList = productService.getAllProducts(page, limit);
////        if (productList.isEmpty()) {
////            throw new ProductServiceException("Product not found");
////        }
//
//        return new ResponseEntity<>(productList, HttpStatus.OK);
//    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductEntity> getProductById(@PathVariable String id) {

        Optional<ProductEntity> product = productService.getProductByProductId(id);
        if (product.isEmpty()) {
            throw new ProductServiceException("Product with id (" + id + ") not found!");
        }
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductEntity> createProduct(@Valid @RequestBody ProductEntity newProduct) {

        return new ResponseEntity<>(productService.createProduct(newProduct), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable String id, @Valid @RequestBody ProductEntity updateProduct) {

        return new ResponseEntity<>(productService.updateProduct(id, updateProduct), HttpStatus.ACCEPTED);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> deleteProduct(@PathVariable String id) {
//
//        productService.deleteProduct(id);
//        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    GenericResponse deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new GenericResponse("Product deleted");
    }
}
