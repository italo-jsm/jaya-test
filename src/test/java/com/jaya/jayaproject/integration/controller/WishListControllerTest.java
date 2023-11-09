package com.jaya.jayaproject.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaya.jayaproject.repository.WishListRepository;
import com.jaya.jayaproject.requests.InsertProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.jaya.jayaproject.utils.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WishListControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WishListRepository wishListRepository;

    @BeforeEach
    void clearDatabase(){
        wishListRepository.deleteAll();
    }

    @Test
    void shouldCreateNewWishListWhenNoOneExists() throws Exception {
        var existingWishList = wishListRepository.findWishListByOwnerId("customerId");
        assertFalse(existingWishList.isPresent());
        mvc.perform(post("/wishlist")
                        .content(asJsonString(new InsertProductRequest("customerId", "productId")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        var createdWishList = wishListRepository.findWishListByOwnerId("customerId");
        assertTrue(createdWishList.isPresent());
        assertEquals(createdWishList.get().getOwnerId(), "customerId");
        assertTrue(createdWishList.get().getProductIds().contains("productId"));
    }

    @Test
    void shouldIncludeProductInExistingWishList() throws Exception {
        wishListRepository.save(createExistingWishListWithOneProduct());
        mvc.perform(post("/wishlist")
                        .content(asJsonString(new InsertProductRequest("ownerId", "Product1")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        var createdWishList = wishListRepository.findWishListByOwnerId("ownerId");
        assertTrue(createdWishList.isPresent());
        assertEquals(createdWishList.get().getOwnerId(), "ownerId");
        assertTrue(createdWishList.get().getProductIds().contains("productId"));
        assertTrue(createdWishList.get().getProductIds().contains("Product1"));
    }

    @Test
    void shouldReturn400WhenWishListIsFull() throws Exception {
        wishListRepository.save(createFullWishList());
        mvc.perform(post("/wishlist")
                        .content(asJsonString(new InsertProductRequest("ownerId", "productId")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenProductIsAlreadyInWishList() throws Exception {
        wishListRepository.save(createExistingWishListWithOneProduct());
        mvc.perform(post("/wishlist")
                        .content(asJsonString(new InsertProductRequest("ownerId", "productId")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindProductsByOwnerId() throws Exception{
        wishListRepository.save(createExistingWishListWithOneProduct());
        var result = mvc.perform(get("/wishlist/find-products")
                        .param("customerId", "ownerId"))
                .andExpect(status().isOk())
                .andReturn();
        var r = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
        assertEquals("productId", r.get(0));
    }

    @Test
    void findProductsByOwnerIdShouldReturn404WhenWishListDoesNotExist() throws Exception {
        wishListRepository.save(createExistingWishListWithOneProduct());
        mvc.perform(get("/wishlist/find-products")
                        .param("customerId", "ownerId1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeProductFromWishListShouldRemoveProductFromWishListWhenProductExists() throws Exception {
        wishListRepository.save(createFullWishList());
        mvc.perform(put("/wishlist/remove-product")
                        .param("customerId", "ownerId")
                        .param("productId", "Product10"))
                .andExpect(status().isOk());
        var updatedWishList = wishListRepository.findWishListByOwnerId("ownerId");
        assertTrue(updatedWishList.isPresent());
        assertFalse(updatedWishList.get().getProductIds().contains("Product10"));
    }

    @Test
    void removeProductFromWishListShouldReturn404WhenProductNotExists() throws Exception {
        wishListRepository.save(createFullWishList());
        mvc.perform(put("/wishlist/remove-product")
                        .param("customerId", "ownerId")
                        .param("productId", "Product100"))
                .andExpect(status().isNotFound());
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> asStringList(String content) throws JsonProcessingException {
        return new ObjectMapper().readValue(content, new TypeReference<List<String>>(){});
    }

}

