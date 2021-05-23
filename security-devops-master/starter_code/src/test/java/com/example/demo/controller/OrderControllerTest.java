package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderControllerTest {

    @InjectMocks
     private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    

    @Before
    public void  setUp(){
        orderController = new OrderController(null,null);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal bigDecimal = BigDecimal.valueOf(2.99);
        item.setPrice(bigDecimal);
        item.setDescription("A widget that is round");
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(itemList);
        BigDecimal bigDecimalPrice = BigDecimal.valueOf(2.99);
        cart.setTotal(bigDecimalPrice);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findByUsername("test1")).thenReturn(null);
    }

    @Test
    public void verify_submit(){
        ResponseEntity<UserOrder> responseEntity = orderController.submit("test");
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        UserOrder userOrder = responseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(1,userOrder.getItems().size());

    }
    @Test
        public void verify_getOrdersForUser(){
        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser("test");
        assertNotNull(listResponseEntity);
        assertEquals(200,listResponseEntity.getStatusCodeValue());
        List<UserOrder> userOrderList = listResponseEntity.getBody();
        assertNotNull(userOrderList);

}
   @Test
    public void verify_submitInvalid(){
        ResponseEntity<UserOrder> responseEntity = orderController.submit("test1");
        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCodeValue());


}
   @Test
    public void verify_getOrderForUserInvalid(){
        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser("test1");
        assertNotNull(listResponseEntity);
        assertEquals(404,listResponseEntity.getStatusCodeValue());
   }

}
