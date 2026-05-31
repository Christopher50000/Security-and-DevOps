package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.example.demo.TestUtils.*;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class OrderControllerTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean //to stop Spring Security from trying to build a real authentication system during the test context startup.
    private AuthenticationManager authenticationManager;

    private String getAllOrdersUrl = "/api/order";

    private String PostOrderByUserName = "/api/order/submit/";

    private String getOrderByNameUrl = "/api/order/history/";

    User user;
    UserOrder userOrder;
    Cart cart;

    Item item;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setPrice(BigDecimal.valueOf(123));

        cart= new Cart();
        cart.setId(1L);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(100L));
        cart.setUser(user);
        user.setCart(cart);

        userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
    }

    @Test
    public void testOrderSubmit() throws Exception {
        doReturn(user).when(userRepository).findByUsername(user.getUsername());
        doReturn(userOrder).when(orderRepository).save(Mockito.any());

        TestStatusOkayPostRequest(mockMvc,PostOrderByUserName+"username",user.getUsername());

    }

    @Test
    public void testOrderSubmitWithNullUser() throws Exception{
        doReturn(null).when(userRepository).findByUsername(user.getUsername());
        TestStatusNotFoundPostRequest(mockMvc,PostOrderByUserName+"username", user.getUsername());
    }


    @Test
    public void testGetOrderHistory() throws Exception{
        doReturn(user).when(userRepository).findByUsername(user.getUsername());
        doReturn(Arrays.asList(userOrder)).when(orderRepository).findByUser(user);
        TestStatusOkayGetRequest(mockMvc,getOrderByNameUrl+"username",user.getUsername());
    }

    @Test
    public void testGetOrderHistoryWhenUserIsNotFound() throws Exception{
        doReturn(null).when(userRepository).findByUsername(user.getUsername());
        doReturn(Arrays.asList(userOrder)).when(orderRepository).findByUser(user);
        TestStatusNotFoundGetRequest(mockMvc,getOrderByNameUrl+"username",user.getUsername());
    }
}
