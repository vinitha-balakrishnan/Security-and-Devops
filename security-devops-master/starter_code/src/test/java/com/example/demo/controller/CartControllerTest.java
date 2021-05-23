package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void  setUp() {
        cartController = new CartController(null, null, null);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("Password");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void verify_addToCart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        Cart cart = responseEntity.getBody();
        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(2.99), cart.getTotal());
    }



    @Test
    public void verify_removeFromCart(){
        ModifyCartRequest cardRequest = new ModifyCartRequest();
        cardRequest.setItemId(1L);
        cardRequest.setQuantity(2);
        cardRequest.setUsername("test");
        ResponseEntity<Cart> responseEntity = cartController.addTocart(cardRequest);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        cardRequest = new ModifyCartRequest();
        cardRequest.setItemId(1L);
        cardRequest.setQuantity(1);
        cardRequest.setUsername("test");
       responseEntity = cartController.removeFromcart(cardRequest);
       assertNotNull(responseEntity);
       assertEquals(200,responseEntity.getStatusCodeValue());
       Cart cart= responseEntity.getBody();
       assertNotNull(cart);
       assertEquals(BigDecimal.valueOf(2.99),cart.getTotal());

    }


    @Test
    public void verify_removeFromCartInvalid(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(2L);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("Invalid");
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);
        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCodeValue());
    }


    @Test
    public void Verify_addToCartInvalid(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(2L);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("Invalid");
        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);
        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCodeValue());

    }

}
