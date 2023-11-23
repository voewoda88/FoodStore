package com.boots;

import com.boots.entity.User;
import com.boots.repository.RoleRepository;
import com.boots.repository.UserRepository;
import com.boots.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    private EntityManager em;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testLoadUserByUsername_ExistingUser() {
        User mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);

        assertEquals(mockUser, userService.loadUserByUsername("testUser"));
    }

    @Test
    public void testLoadUserByUsername_NonExistingUser() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistingUser"));
    }

    @Test
    public void testFindUserById_ExistingUser() {
        User mockUser = new User();
        mockUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        assertEquals(mockUser, userService.findUserById(1L));
    }

    @Test
    public void testFindUserById_NonExistingUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertEquals(new User(), userService.findUserById(2L));
    }

    @Test
    public void testAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(mockUsers);

        assertEquals(mockUsers, userService.allUsers());
    }

    @Test
    public void testSaveUser_NewUser() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");

        assertTrue(userService.saveUser(newUser));
        verify(userRepository).save(newUser);
    }

    @Test
    public void testSaveUser_ExistingUser() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("password");

        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);

        assertFalse(userService.saveUser(existingUser));
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    public void testSaveUpdatedUser() {
        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newPassword");

        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        assertTrue(userService.saveUpdatedUser(updatedUser));
        verify(userRepository).save(updatedUser);
    }

    @Test
    public void testDeleteUser_ExistingUser() {
        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertTrue(userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testDeleteUser_NonExistingUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertFalse(userService.deleteUser(2L));
        verify(userRepository, never()).deleteById(2L);
    }

    @Test
    public void testGetAuthentificateUser_AuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("authenticatedUser");

        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

        assertEquals("authenticatedUser", userService.getAuthentificateUser());
    }

    @Test
    public void testGetAuthentificateUser_UnauthenticatedUser() {
        SecurityContextHolder.clearContext();

        assertNull(userService.getAuthentificateUser());
    }

}
