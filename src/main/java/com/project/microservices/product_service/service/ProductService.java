package com.project.microservices.product_service.service;

import com.project.microservices.product_service.dto.ProductRequest;
import com.project.microservices.product_service.dto.ProductResponse;
import com.project.microservices.product_service.model.Product;
import com.project.microservices.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    public ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRequest == null)
            throw new IllegalArgumentException("Product must not be null");

        Product product = Product.builder().
                id(productRequest.id())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price()).build();
        productRepository.save(product);
        log.info("Product created successfully");
        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice());
    }
    public List<ProductResponse> getProducts()
    {
        return productRepository.findAll().stream().map(product-> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        )).toList();
    }

}
