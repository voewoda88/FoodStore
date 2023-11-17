package com.boots.controller;

import com.boots.entity.Category;
import com.boots.entity.Product;
import com.boots.entity.User;
import com.boots.service.CategoryService;
import com.boots.service.ProductService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> listOfUsers = userService.allUsers();
        model.addAttribute("users", listOfUsers);

        return "displayUsers";
    }

    @PostMapping("/deleteUsers")
    public String deleteSelectedUsers(@RequestParam("selectedUsers") List<Long> selectedUsers) {
       for(Long userId : selectedUsers) {
           userService.deleteUser(userId);
       }

       return "redirect:/admin/users";
    }

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> listOfCategories = categoryService.allCategories();
        model.addAttribute("categories", listOfCategories);

        return "categories";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam("categoryName") String categoryName) {
        Category category = new Category();
        category.setCategory(categoryName);
        if(!categoryService.saveCategory(category)) {
            System.out.println("No");
        }

        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/update")
    public String updateCategory(@RequestParam Long categoryId, @RequestParam String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setCategory(categoryName);
        if(!categoryService.saveCategory(category)) {
            System.out.println("No");
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete")
    public String deleteCategory(@RequestParam("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return "redirect:/admin/categories";
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.allProducts();
        model.addAttribute("products", products);

        return "products";
    }

    @GetMapping("/products/add")
    public String getProductInfo(Model model) {
        List<Category> categories = categoryService.allCategories();
        model.addAttribute("categories", categories);

        Product product = new Product();
        product.setId(productService.getLastProductId() + 1);
        model.addAttribute("product", product);

        return "productAdd";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete")
    public String deleteProduct(@RequestParam("productId") Long productId) {
        productService.deleteProduct(productId);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/update/{id}")
    public String getInfoAboutUpdateProduct(@PathVariable("id") Long productId, Model model) {
        Product product = productService.findProductById(productId);
        List<Category> categories = categoryService.allCategories();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "productUpdate";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long productId, @ModelAttribute Product updatedProduct) {
        if(!productService.saveProduct(updatedProduct)){
            System.out.println("No");
        }

        return "redirect:/admin/products";
    }
}
