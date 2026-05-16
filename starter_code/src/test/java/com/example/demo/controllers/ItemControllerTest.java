package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.TestStatusNotFoundGetRequest;
import static com.example.demo.TestUtils.TestStatusOkayGetRequest;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class ItemControllerTest {


    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean //to stop Spring Security from trying to build a real authentication system during the test context startup.
    private AuthenticationManager authenticationManager;

    List<Item> items;

    private String getAllItemsUrl = "/api/item";

    private String getItemsByIdUrl = "/api/item/";

    private String getItemsByNameUrl = "/api/item/name/";

    @BeforeEach
    public void setUp() {

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item");
        item1.setPrice(BigDecimal.valueOf(123));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item");
        item2.setPrice(BigDecimal.valueOf(123));


        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("item");
        item3.setPrice(BigDecimal.valueOf(123));

        items = Arrays.asList(item1, item2, item3);

    }


    @Test
    public void testFindAllItems() throws Exception{
        doReturn(Arrays.asList(items.get(0))).when(itemRepository).findAll();

        TestStatusOkayGetRequest(mockMvc,getAllItemsUrl,null);
    }

    @Test
    public void testFindItemById() throws Exception{

        Item item = items.get(0);
        int id = item.getId().intValue();

        doReturn(Optional.of(item)).when(itemRepository).findById(Long.valueOf(id));

        TestStatusOkayGetRequest(mockMvc, getItemsByIdUrl+id,item.getId());
    }

    @Test
    public void testGetItemsByName() throws Exception {
        doReturn(items).when(itemRepository).findByName("item");
        TestStatusOkayGetRequest(mockMvc, getItemsByNameUrl+"item","item");
    }

    @Test
    public void testNotFoundGetItemsByName() throws Exception {
        doReturn(null).when(itemRepository).findByName("item");
        TestStatusNotFoundGetRequest(mockMvc, getItemsByNameUrl+"item","item");
    }

}
