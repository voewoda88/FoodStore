package com.boots;

import com.boots.controller.UserController;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Test
    public void testRegistrationPage() {
        Model model = Mockito.mock(Model.class);
        String result = userController.registration(model);

        assertEquals("registration", result);
    }


    @Test
    public void testAddUser_WithValidationErrors() {
        Model model = Mockito.mock(Model.class);
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "userForm");

        bindingResult.rejectValue("username", "error.username", "Username error message");

        String result = userController.addUser(new User(), bindingResult, model);

        assertEquals("registration", result);
        Mockito.verify(model).addAttribute("usernameError", "Username error message");
    }

    @Test
    public void testAddUser_UsernameError_ReturnRegistrationPage() {
        User userForm = new User();
        userForm.setUsername("testUser");
        userForm.setPassword("password");
        userForm.setPasswordConfirm("password");

        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "userForm");
        bindingResult.rejectValue("username", "error.username", "Username error message");

        Model model = Mockito.mock(Model.class);

        String result = userController.addUser(userForm, bindingResult, model);

        assertEquals("registration", result);
        Mockito.verify(model).addAttribute("usernameError", "Username error message");
    }

    @Test
    public void testAddUser_PasswordAndConfirmPasswordMismatch_ReturnRegistrationPage() {
        User userForm = new User();
        userForm.setUsername("testUser");
        userForm.setPassword("password");
        userForm.setPasswordConfirm("mismatch");

        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "userForm");
        Model model = Mockito.mock(Model.class);

        String result = userController.addUser(userForm, bindingResult, model);

        assertEquals("registration", result);
        Mockito.verify(model).addAttribute("passwordConfirmError", "Пароли не совпадают");
    }

    @Test
    public void testAddUser_UsernameEqualsPassword_ReturnRegistrationPage() {
        User userForm = new User();
        userForm.setUsername("testUser");
        userForm.setPassword("testUser");
        userForm.setPasswordConfirm("testUser");

        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "userForm");
        Model model = Mockito.mock(Model.class);

        String result = userController.addUser(userForm, bindingResult, model);

        assertEquals("registration", result);
        Mockito.verify(model).addAttribute("passwordError", "Имя не должно совпадать с паролем");
    }

    @Test
    public void testUpdateProfile_PasswordsMatch_ReturnRedirectToHome() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setPasswordConfirm("password");

        String result = userController.updateProfile(user);

        assertEquals("redirect:/profile", result);
        Mockito.verify(userService).saveUpdatedUser(user);
    }

    @Test
    public void testUpdateProfile_PasswordsMismatch_ReturnRedirectToProfile() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setPasswordConfirm("mismatch");

        String result = userController.updateProfile(user);

        assertEquals("redirect:/profile", result);
    }

    @Test
    public void testUpdateProfile_UsernameEqualsPassword_ReturnRedirectToProfile() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testUser");
        user.setPasswordConfirm("testUser");

        String result = userController.updateProfile(user);

        assertEquals("redirect:/profile", result);
    }

    @Test
    public void testUpdateProfile_SaveUserFails_ReturnRedirectToProfile() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setPasswordConfirm("password");

        Mockito.when(userService.saveUpdatedUser(user)).thenReturn(false);

        String result = userController.updateProfile(user);

        assertEquals("redirect:/profile", result);
        Mockito.verify(userService).saveUpdatedUser(user);
    }

    @Test
    public void testGetUserProducts() {
        Model model = Mockito.mock(Model.class);
        List<Product> productList = Arrays.asList(new Product(), new Product());
        when(productService.allProducts()).thenReturn(productList);

        String result = userController.getUserProducts(model);

        assertEquals("userProducts", result);
        Mockito.verify(model).addAttribute("products", productList);
    }

    @Test
    public void testGetHomePage() {
        Model model = Mockito.mock(Model.class);
        when(userService.getAuthentificateUser()).thenReturn("testUser");
        List<Product> productList = Arrays.asList(new Product(), new Product());
        when(productService.allProducts()).thenReturn(productList);

        String result = userController.getHomePage(model);

        assertEquals("home", result);
        Mockito.verify(model).addAttribute("username", "testUser");
        Mockito.verify(model).addAttribute("products", productList);
    }

}
