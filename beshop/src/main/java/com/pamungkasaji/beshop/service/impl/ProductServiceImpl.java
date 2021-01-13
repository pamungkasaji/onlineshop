package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.exceptions.ProductServiceException;
import com.pamungkasaji.beshop.repository.ProductRepository;
import com.pamungkasaji.beshop.service.ProductService;
import com.pamungkasaji.beshop.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    Utils utils;

    public ProductServiceImpl(ProductRepository productRepository, Utils utils) {
        this.productRepository = productRepository;
        this.utils = utils;
    }

    @Override
    public HashMap<String, Object> getAllProducts(int page, int limit) {

        if(page>0) page = page-1;
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<ProductEntity> productsList = productRepository.findAll(pageableRequest);

        HashMap<String, Object> productsPaging = new HashMap<>();
        productsPaging.put("products", productsList.getContent());
        productsPaging.put("page", productsList.getNumber() + 1);
        productsPaging.put("pages", productsList.getTotalPages());
        productsPaging.put("totalProducts", productsList.getTotalElements());

        return productsPaging;
    }

//    @Override
//    public List<ProductEntity> getAllProducts(int page, int limit) {
//
//        if(page>0) page = page-1;
//        Pageable pageableRequest = PageRequest.of(page, limit);
//        Page<ProductEntity> productsPage = productRepository.findAll(pageableRequest);
//
//        return productsPage.getContent();
//    }

    @Override
    public Optional<ProductEntity> getProductByProductId(String id) {
        return productRepository.findByProductId(id);
    }

    @Override
    public ProductEntity createProduct(ProductEntity newProduct) {
        String generateUserId = utils.generateId(25);
        newProduct.setProductId(generateUserId);
        return productRepository.save(newProduct);
    }

    @Override
    public ProductEntity updateProduct(String id, ProductEntity productUpdate) {
//        if (!id.equals(productUpdate.getProductId())) {
//            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Product in URI does not match product id to update");
//        }

        Optional<ProductEntity> op = productRepository.findByProductId(id);
        if (op.isEmpty()) throw new ProductServiceException("Product with id (" + id + ") not found!");

        ProductEntity orginalProduct = op.get();
//        BeanUtils.copyProperties(productUpdate, orginalProduct);

        orginalProduct.setName(productUpdate.getName());
        orginalProduct.setBrand(productUpdate.getBrand());
        orginalProduct.setCountInStock(productUpdate.getCountInStock());
        orginalProduct.setDescription(productUpdate.getDescription());
        orginalProduct.setImage(productUpdate.getImage());
        orginalProduct.setPrice(productUpdate.getPrice());

        return productRepository.save(orginalProduct);
    }

    @Override
    public void deleteProduct(String id) {
        Optional<ProductEntity> product = productRepository.findByProductId(id);
        if (product.isEmpty()) {
            throw new ProductServiceException("Product with id (" + id + ") not found!");
        }

        productRepository.delete(product.get());
    }
}
