package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.Optional;

import static com.example.demo.TestUtils.injectObjects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository =mock(UserRepository.class);

    private CartRepository cartRepository =mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder =mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        injectObjects(userController, "userRepository", userRepository);
        injectObjects(userController, "cartRepository", cartRepository);
        injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

    }

    @Test
    public void create_user_happy_path_forCreateUserRequest() {


        when(bCryptPasswordEncoder.encode("password")).thenReturn("thisIsHashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("username");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }


    @Test
    public void create_user_badRequest_forCreateUserRequest() {


        when(bCryptPasswordEncoder.encode("password")).thenReturn("thisIsHashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("username");
        createUserRequest.setPassword("passwo");
        createUserRequest.setConfirmPassword("passwo");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertNull(response.getBody());

    }


    @Test
    public void testWithFindByUsername() {

        String name="Paul";
        User user = new User();
        user.setUsername(name);
        user.setPassword("password");
        user.setCart(new Cart());
        doReturn(user).when(userRepository).findByUsername(name);
        final ResponseEntity<User> responseEntity = userController.findByUserName(name);
        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());

    }

    @Test
    public void testWithFindByUsernameNotFound() {
        doReturn(null).when(userRepository).findByUsername("username");
        final ResponseEntity<User> responseEntity = userController.findByUserName("username");
        assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1L);

        doReturn(Optional.of(user)).when(userRepository).findById(1L);

        final ResponseEntity<User> responseEntity = userController.findById(1L);
        assertEquals(HttpStatusCode.valueOf(200),responseEntity.getStatusCode());
    }

    @Test void testFindByIdNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(1L);
        final ResponseEntity<User> responseEntity = userController.findById(1L);
        assertEquals(HttpStatusCode.valueOf(404), responseEntity.getStatusCode());
    }

}
