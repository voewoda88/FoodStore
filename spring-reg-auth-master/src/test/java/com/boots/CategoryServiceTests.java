package com.boots;

import com.boots.entity.Category;
import com.boots.repository.CategoryRepository;
import com.boots.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTests{

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testAllCategories() {
        List<Category> categories = Arrays.asList(
                new Category(),
                new Category()
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.allCategories();

        assertEquals(categories.size(), result.size());

        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    public void testSaveCategory_CategoryDoesNotExist() {
        Category newCategory = new Category();

        when(categoryRepository.findByCategory(newCategory.getCategory())).thenReturn(null);

        boolean result = categoryService.saveCategory(newCategory);

        assertTrue(result);

        Mockito.verify(categoryRepository).findByCategory(newCategory.getCategory());

        Mockito.verify(categoryRepository).save(newCategory);
    }

    @Test
    public void testSaveCategory_CategoryExists() {
        Category existingCategory = new Category();

        when(categoryRepository.findByCategory(existingCategory.getCategory())).thenReturn(existingCategory);

        boolean result = categoryService.saveCategory(existingCategory);

        assertFalse(result);

        Mockito.verify(categoryRepository).findByCategory(existingCategory.getCategory());

        Mockito.verify(categoryRepository, Mockito.never()).save(existingCategory);
    }

    @Test
    public void testDeleteCategory_CategoryExists() {
        Long categoryIdToDelete = 1L;

        when(categoryRepository.findById(categoryIdToDelete)).thenReturn(Optional.of(new Category()));

        boolean result = categoryService.deleteCategory(categoryIdToDelete);

        assertTrue(result);

        Mockito.verify(categoryRepository).findById(categoryIdToDelete);

        Mockito.verify(categoryRepository).deleteById(categoryIdToDelete);
    }

    @Test
    public void testDeleteCategory_CategoryDoesNotExist() {
        Long categoryIdToDelete = 1L;

        when(categoryRepository.findById(categoryIdToDelete)).thenReturn(Optional.empty());

        boolean result = categoryService.deleteCategory(categoryIdToDelete);

        assertFalse(result);

        Mockito.verify(categoryRepository).findById(categoryIdToDelete);

        Mockito.verify(categoryRepository, Mockito.never()).deleteById(categoryIdToDelete);
    }

    @Test
    public void testFindCategoryById_CategoryExists() {
        Long categoryIdToFind = 1L;

        Category foundCategory = new Category();

        when(categoryRepository.findById(categoryIdToFind)).thenReturn(Optional.of(foundCategory));

        Category result = categoryService.findCategoryById(categoryIdToFind);

        assertEquals(foundCategory, result);

        Mockito.verify(categoryRepository).findById(categoryIdToFind);
    }

    @Test
    public void testFindCategoryById_CategoryDoesNotExist() {
        Long categoryIdToFind = 1L;

        when(categoryRepository.findById(categoryIdToFind)).thenReturn(Optional.empty());

        Category result = categoryService.findCategoryById(categoryIdToFind);

        assertEquals(new Category(), result);

        Mockito.verify(categoryRepository).findById(categoryIdToFind);
    }


}