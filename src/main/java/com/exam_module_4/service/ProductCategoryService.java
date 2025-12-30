package com.exam_module_4.service;

import com.exam_module_4.model.ProductCategory;
import com.exam_module_4.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    public Optional<ProductCategory> findById(Long id) {
        return productCategoryRepository.findById(id);
    }

    public ProductCategory save(ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    public void deleteById(Long id) {
        productCategoryRepository.deleteById(id);
    }
}

