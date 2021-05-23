package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;


    @Before
    public void  setUp() {
        itemController = new ItemController(null);
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal bigDecimal = BigDecimal.valueOf(2.99);
        item.setPrice(bigDecimal);
        item.setDescription("A widget that is round");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("Round Widget")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void verify_getItems(){
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItems();
        assertNotNull(listResponseEntity);
        assertEquals(200,listResponseEntity.getStatusCodeValue());
        List<Item> itemList = listResponseEntity.getBody();
        assertNotNull(itemList);
        assertEquals(1,itemList.size());
    }

        @Test
        public  void verify_getItemById(){
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        Item item = responseEntity.getBody();
        assertNotNull(item);

     }

          @Test
          public void verify_getItemByName(){
          ResponseEntity<List<Item>>  listResponseEntity = itemController.getItemsByName("Round Widget");
          assertNotNull(listResponseEntity);
          assertEquals(200,listResponseEntity.getStatusCodeValue());
          List<Item> itemList = listResponseEntity.getBody();
          assertNotNull(itemList);
          assertEquals(1,itemList.size());
     }

          @Test
          public void verify_getItemByIdInvalid(){
         ResponseEntity <Item> responseEntity = itemController.getItemById(3L);
         assertNotNull(responseEntity);
         assertEquals(404,responseEntity.getStatusCodeValue());

     }

     @Test
    public void verify_getItemByNameInvalid(){
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName(" test");
        assertNotNull(listResponseEntity);
        assertEquals(404,listResponseEntity.getStatusCodeValue());
     }


}
