package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TestUtils {


    public static void  injectObjects(Object target, String filedName, Object injectObject) {

       boolean wasPrivate = false;

       try {
           Field field = target.getClass().getDeclaredField(filedName);
           if(!field.isAccessible()){
               field.setAccessible(true);
               wasPrivate = true;
           }

           field.set(target, injectObject);
           if(wasPrivate){
               field.setAccessible(false);
           }
       } catch (NoSuchFieldException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }

   }



}
