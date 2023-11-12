package com.heeratsingh.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeratsingh.productservice.dto.ProductRequest;
import com.heeratsingh.productservice.dto.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static MongoDBContainer mongoDBContainer=new MongoDBContainer("mongo:4.0.10");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
        //dynamicPropertyRegistry.add("server.port","");
    }


    @Test
    void testCreateProduct() throws Exception {
        final ProductRequest productRequest = getProductRequest();
        final String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestString))
                .andExpect(status().isCreated());
    }

    /*
    @Test
    void testGetProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk());
    }
    */

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Roku TV")
                .description("55 inch")
                .price(BigDecimal.valueOf(208))
                .build();
    }

}
