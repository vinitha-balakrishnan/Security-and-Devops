package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository =  mock(UserRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private CartRepository cartRepository = mock(CartRepository.class);



    @Before
    public void setUp(){
        userController = new UserController(null, null, null);
        TestUtils.injectObjects(userController,"userRepository",userRepository);
        TestUtils.injectObjects(userController,"cartRepository",cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);
        Cart cart = new Cart();
        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("test1")).thenReturn(null);


    }

    @Test
    public void createUserHappyPath(){
        when(bCryptPasswordEncoder.encode("password")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("password");
        userRequest.setConfirmPassword("password");
        ResponseEntity<User> responseEntity = userController.createUser(userRequest);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed",user.getPassword());
    }



@Test
    public void verify_findById(){
        ResponseEntity<User> responseEntity = userController.findById(0L);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0,user.getId());

}

@Test
    public void verify_findByName(){
        ResponseEntity<User> responseEntity= userController.findByUserName("test");
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());


}

    @Test
    public void verify_findByNameInvalid(){
        ResponseEntity <User> responseEntity = userController.findByUserName("test1");
        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCodeValue());
}

@Test
    public void verify_findByIdInvalid(){
    ResponseEntity <User> responseEntity = userController.findById(1L);
    assertNotNull(responseEntity);
    assertEquals(404,responseEntity.getStatusCodeValue());

}

}
