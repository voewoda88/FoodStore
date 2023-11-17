package com.boots.service;

import com.boots.entity.Category;
import com.boots.entity.User;
import com.boots.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    public boolean saveCategory(Category category) {
        Category categoryFromBD = categoryRepository.findByCategory(category.getCategory());

        if(categoryFromBD != null) {
            return false;
        }

        categoryRepository.save(category);
        return true;
    }

    public boolean deleteCategory(Long categoryId) {
        if(categoryRepository.findById(categoryId).isPresent()) {
            categoryRepository.deleteById(categoryId);
            return true;
        }

        return false;
    }

    public Category findCategoryById(Long categoryId) {
        Optional<Category> categoryFromDb = categoryRepository.findById(categoryId);
        return categoryFromDb.orElse(new Category());
    }
}
