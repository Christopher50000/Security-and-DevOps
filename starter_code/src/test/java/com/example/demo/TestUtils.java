package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.lang.reflect.Field;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {


    public static void injectObjects(Object target, String fieldname, Object toinject) {

        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldname);

            if (!field.isAccessible()) {
                field.setAccessible(true);
                wasPrivate = true;

            }

            field.set(target, toinject);

            if (wasPrivate) {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
           e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    public static void TestStatusOkayGetRequest(MockMvc mockMvc, String URL,Object object) throws Exception{
        mockMvc.perform(
                        get(URL)
                                .content(getJsonStringFromObject(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static void TestStatusNotFoundGetRequest(MockMvc mockMvc,String URL, Object object) throws Exception{
        mockMvc.perform(
                        get(URL)
                                .content(getJsonStringFromObject(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    public static void TestStatusBadGetRequest(MockMvc mockMvc, String URL ,Object object) throws Exception{
        mockMvc.perform(
                        post(URL)
                                .content(getJsonStringFromObject(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }




    public static void TestStatusOkayPostRequest(MockMvc mockMvc, String URL,Object object) throws Exception{
        mockMvc.perform(
                        post(URL)
                                .content(getJsonStringFromObject(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static void TestStatusNotFoundPostRequest(MockMvc mockMvc,String URL, Object object) throws Exception{
        mockMvc.perform(
                        post(URL)
                                .content(getJsonStringFromObject(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    public static void TestStatusBadRequest(MockMvc mockMvc, String URL ,Object object) throws Exception{
        mockMvc.perform(
                        post(URL)
                                .content(getJsonStringFromObject(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    public static String getJsonStringFromObject(Object obj) {

        try{
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (JsonProcessingException jsonProcessingException){
            System.out.println("Error processing JSON string from object, Returning nothing");
            return "";
        }
    }
}
