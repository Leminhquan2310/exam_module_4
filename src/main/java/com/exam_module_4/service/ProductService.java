package com.exam_module_4.service;

import com.exam_module_4.model.Product;
import com.exam_module_4.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String name, Long categoryId, Double price, Pageable pageable) {
        return productRepository.searchProducts(name, categoryId, price, pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            productRepository.deleteById(id);
        }
    }

    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }
}
