package com.boots.controller;

import com.boots.entity.Product;
import com.boots.entity.User;
import com.boots.service.CategoryService;
import com.boots.service.ProductService;
import com.boots.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError error : fieldErrors){
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();

                if(fieldName.equals("username")){
                    model.addAttribute("usernameError", errorMessage);
                    return "registration";
                }

                if(fieldName.equals("password")){
                    model.addAttribute("passwordError", errorMessage);
                    return "registration";
                }
            }
        }

        if(!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordConfirmError", "Пароли не совпадают");
            return "registration";
        }

        if(userForm.getUsername().equals(userForm.getPassword())) {
            model.addAttribute("passwordError", "Имя не должно совпадать с паролем");
            return "registration";
        }

        if(!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfileInfo(Model model) {
        User user = (User)userService.loadUserByUsername(userService.getAuthentificateUser());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("id", user.getId());

        return "updateProfile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user) {
        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            return "redirect:/profile";
        }

        if(user.getPassword().equals(user.getUsername())) {
            return "redirect:/profile";
        }

        if(!userService.saveUpdatedUser(user)){
            return "redirect:/profile";
        }

        return "redirect:/";
    }

    @GetMapping("/products")
    public String getUserProducts(Model model) {
        List<Product> productList = productService.allProducts();
        model.addAttribute("products", productList);

        return "userProducts";
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("username", userService.getAuthentificateUser());
        List<Product> products = productService.allProducts();
        model.addAttribute("products", products);

        return "home";
    }
}

