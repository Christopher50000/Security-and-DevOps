package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;


@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class CartControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean //to stop Spring Security from trying to build a real authentication system during the test context startup.
    private AuthenticationManager authenticationManager;

    private User user;

    private Cart cart;

    private List<Item> item;

    public static final String ADD_TO_CART_URL = "/api/cart/addToCart";

    public static final String REMOVE_FROM_CART_URL = "/api/cart/removeFromCart";



    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");

        Item item = new Item();
        item.setId(3L);
        item.setName("item");
        item.setPrice(BigDecimal.valueOf(123));

        cart = new Cart();
        cart.setId(2L);
        cart.setItems(new ArrayList<>(Arrays.asList(item)));
        user.setCart(cart);

    }

    @Test
    public void addingToCart() throws Exception {
        Item item = new Item();
        item.setId(5L);
        item.setName("item2");
        item.setPrice(BigDecimal.valueOf(1245));

        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(5L);
        modifyCartRequest.setQuantity(1);

        doReturn(user).when(userRepository).findByUsername(modifyCartRequest.getUsername());
        doReturn(Optional.of(item)).when(itemRepository).findById(modifyCartRequest.getItemId());


       TestUtils.TestStatusOkayPostRequest(mockMvc,ADD_TO_CART_URL,modifyCartRequest);

        assertEquals(2,cart.getItems().size());
    }

    @Test
    public void testWithNoUserFound() throws Exception{

        Item item = new Item();
        item.setId(5L);
        item.setName("item2");
        item.setPrice(BigDecimal.valueOf(1245));

        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(5L);
        modifyCartRequest.setQuantity(1);


        doReturn(null).when(userRepository).findByUsername(modifyCartRequest.getUsername());


        TestUtils.TestStatusNotFoundPostRequest(mockMvc,ADD_TO_CART_URL,modifyCartRequest);

    }

    @Test
    public void testWithItemNotPresent() throws Exception {


        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(5L);
        modifyCartRequest.setQuantity(1);

        doReturn(user).when(userRepository).findByUsername(modifyCartRequest.getUsername());
        doReturn(Optional.empty()).when(itemRepository).findById(modifyCartRequest.getItemId());


        TestUtils.TestStatusNotFoundPostRequest(mockMvc,ADD_TO_CART_URL,modifyCartRequest);
    }


    @Test
    public void RemovingFromCart() throws Exception {
        Item item = new Item();
        item.setId(5L);
        item.setName("item2");
        item.setPrice(BigDecimal.valueOf(1245));

        cart.getItems().add(item);

        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(5L);
        modifyCartRequest.setQuantity(1);

        doReturn(user).when(userRepository).findByUsername(modifyCartRequest.getUsername());
        doReturn(Optional.of(item)).when(itemRepository).findById(modifyCartRequest.getItemId());


        TestUtils.TestStatusOkayPostRequest(mockMvc,REMOVE_FROM_CART_URL,modifyCartRequest);

        assertEquals(1,cart.getItems().size());
    }



    @Test
    public void RemovingFromCartWithUserNotFound() throws Exception {
        Item item = new Item();
        item.setId(5L);
        item.setName("item2");
        item.setPrice(BigDecimal.valueOf(1245));

        cart.getItems().add(item);

        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(5L);
        modifyCartRequest.setQuantity(1);

        doReturn(null).when(userRepository).findByUsername(modifyCartRequest.getUsername());
        doReturn(Optional.of(item)).when(itemRepository).findById(modifyCartRequest.getItemId());

        TestUtils.TestStatusNotFoundPostRequest(mockMvc,REMOVE_FROM_CART_URL,modifyCartRequest);

    }


    @Test
    public void RemovingFromCartWithItemNotFound() throws Exception {


        ModifyCartRequest modifyCartRequest= new ModifyCartRequest();
        modifyCartRequest.setUsername("username");
        modifyCartRequest.setItemId(5L);
        modifyCartRequest.setQuantity(1);

        doReturn(user).when(userRepository).findByUsername(modifyCartRequest.getUsername());
        doReturn(Optional.empty()).when(itemRepository).findById(modifyCartRequest.getItemId());

        TestUtils.TestStatusNotFoundPostRequest(mockMvc,REMOVE_FROM_CART_URL,modifyCartRequest);

    }












}
