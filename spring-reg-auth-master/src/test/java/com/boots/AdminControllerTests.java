package com.boots;

import com.boots.controller.AdminController;
import com.boots.entity.Category;
import com.boots.entity.Product;
import com.boots.entity.User;
import com.boots.service.CategoryService;
import com.boots.service.ProductService;
import com.boots.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTests {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Test
    public void testGetUsers() {
        List<User> userList = new ArrayList<>();
        when(userService.allUsers()).thenReturn(userList);

        Model model = Mockito.mock(Model.class);

        String result = adminController.getUsers(model);

        assertEquals("displayUsers", result);
        verify(model).addAttribute(eq("users"), eq(userList));
    }

    @Test
    public void testDeleteSelectedUsers() {
        List<Long> selectedUsers = Arrays.asList(1L, 2L, 3L);

        when(userService.deleteUser(Mockito.anyLong())).thenReturn(true);

        String result = adminController.deleteSelectedUsers(selectedUsers);

        assertEquals("redirect:/admin/users", result);

        for (Long userId : selectedUsers) {
            verify(userService).deleteUser(userId);
        }
    }

    @Test
    public void testGetCategories() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        when(categoryService.allCategories()).thenReturn(categories);

        Model model = Mockito.mock(Model.class);
        String result = adminController.getCategories(model);

        assertEquals("categories", result);

        verify(model).addAttribute("categories", categories);
    }

    @Test
    public void testAddCategory() {
        String categoryName = "TestCategory";

        Category category = new Category();
        category.setCategory(categoryName);

        when(categoryService.saveCategory(any(Category.class))).thenReturn(true);

        String result = adminController.addCategory(categoryName);

        assertEquals("redirect:/admin/categories", result);
    }

    @Test
    public void testUpdateCategory() {
        Long categoryId = 1L;
        String categoryName = "UpdatedCategory";

        when(categoryService.saveCategory(any(Category.class))).thenReturn(true);

        String result = adminController.updateCategory(categoryId, categoryName);

        assertEquals("redirect:/admin/categories", result);
    }

    @Test
    public void testDeleteCategory() {
        Long categoryId = 1L;

        String result = adminController.deleteCategory(categoryId);

        assertEquals("redirect:/admin/categories", result);

        verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    public void testGetProducts() {
        List<Product> products = Arrays.asList(new Product(), new Product());

        when(productService.allProducts()).thenReturn(products);

        Model model = Mockito.mock(Model.class);
        String result = adminController.getProducts(model);

        assertEquals("products", result);

        verify(model).addAttribute("products", products);
    }

    @Test
    public void testGetProductInfo() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryService.allCategories()).thenReturn(categories);

        Model model = Mockito.mock(Model.class);
        String result = adminController.getProductInfo(model);

        assertEquals("productAdd", result);

        verify(model).addAttribute("categories", categories);
    }

    @Test
    public void testAddProduct() {
        Product product = new Product();

        String result = adminController.addProduct(product);

        assertEquals("redirect:/admin/products", result);

        verify(productService).saveProduct(product);
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        String result = adminController.deleteProduct(productId);

        assertEquals("redirect:/admin/products", result);

        verify(productService).deleteProduct(productId);
    }

    @Test
    public void testGetInfoAboutUpdateProduct() {
        Long productId = 1L;
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(productService.findProductById(productId)).thenReturn(new Product());
        when(categoryService.allCategories()).thenReturn(categories);

        Model model = Mockito.mock(Model.class);
        String result = adminController.getInfoAboutUpdateProduct(productId, model);

        assertEquals("productUpdate", result);

        verify(model).addAttribute(eq("product"), any(Product.class));
        verify(model).addAttribute(eq("categories"), eq(categories));
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product updatedProduct = new Product();

        when(productService.saveProduct(updatedProduct)).thenReturn(true);

        String result = adminController.updateProduct(productId, updatedProduct);

        assertEquals("redirect:/admin/products", result);

        verify(productService).saveProduct(updatedProduct);
    }
}
