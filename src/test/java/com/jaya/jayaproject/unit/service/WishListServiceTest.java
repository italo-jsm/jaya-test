package com.jaya.jayaproject.unit.service;

import com.jaya.jayaproject.domain.WishList;
import com.jaya.jayaproject.exceptions.FullWishListException;
import com.jaya.jayaproject.exceptions.ProductExistsException;
import com.jaya.jayaproject.exceptions.ProductNotFoundException;
import com.jaya.jayaproject.exceptions.WishListNotFoundException;
import com.jaya.jayaproject.repository.WishListRepository;
import com.jaya.jayaproject.service.WishListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static com.jaya.jayaproject.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    @Captor
    ArgumentCaptor<WishList> wishListCaptor;

    @InjectMocks
    private WishListService underTest;

    @Test
    public void insertProductOnCustomerWishListShouldCreateNewWishListWhenWishListNotFound(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.empty());
        when(wishListRepository.save(wishListCaptor.capture())).thenReturn(createWishList());
        underTest.insertProductOnCustomerWishList("costumerId", "productId");
        var wishListCaptured = wishListCaptor.getValue();
        assertTrue(isUUID(wishListCaptured.getId()));
    }

    @Test
    public void insertProductOnCustomerWishListShouldNotCreateNewWishListWhenWishListExists(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createExistingWishList()));
        when(wishListRepository.save(wishListCaptor.capture())).thenReturn(createWishList());
        underTest.insertProductOnCustomerWishList("costumerId", "productId");
        var wishListCaptured = wishListCaptor.getValue();
        assertEquals(wishListCaptured.getId(), "WishListId");
        assertEquals(wishListCaptured.getOwnerId(), "ownerId");
    }

    @Test
    public void insertProductOnCustomerWishListShouldThrowFullWishListExceptionWhenWishListIsFull(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createFullWishList()));
        try{
            underTest.insertProductOnCustomerWishList("costumerId", "productId");
            fail("Should Throw FullWishListException");
        }catch (Exception e){
            assertTrue(e instanceof FullWishListException);
        }
    }

    @Test
    public void insertProductOnCustomerWishListShouldThrowProductExistsExceptionWhenProductIsInWishList(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createExistingWishListWithOneProduct()));
        try{
            underTest.insertProductOnCustomerWishList("costumerId", "productId");
            fail("Should Throw ProductExistsException");
        }catch (Exception e){
            assertTrue(e instanceof ProductExistsException);
        }
    }

    @Test
    public void isProductInWishListShouldReturnTrueWhenProductIsInWishList(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createFullWishList()));
        var result = underTest.isProductInWishList("Product1", "ownerId");
        assertTrue(result);
    }

    @Test
    public void isProductInWishListShouldReturnFalseWhenProductIsNotInWishList(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createFullWishList()));
        var result = underTest.isProductInWishList("Product100", "ownerId");
        assertFalse(result);
    }

    @Test
    public void isProductInWishListShouldReturnFalseWhenWishListNotFound(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.empty());
        var result = underTest.isProductInWishList("Product100", "ownerId");
        assertFalse(result);
    }

    @Test
    public void removeProductShouldRemoveProduct(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createFullWishList()));
        when(wishListRepository.save(wishListCaptor.capture())).thenReturn(createWishList());
        underTest.removeProduct("Product1", "ownerId");
        var captured = wishListCaptor.getValue();
        assertFalse(
                captured.getProductIds()
                        .stream()
                        .anyMatch(existingProductId -> Objects.equals("Product1", existingProductId))
        );
    }

    @Test
    public void removeProductShouldThrowWishListNotFoundExceptionWhenWishListNotFound(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.empty());
        try{
            underTest.removeProduct("Product100", "ownerId");
            fail("Should Throw WishListNotFoundException");
        }catch (Exception e){
            assertTrue(e instanceof WishListNotFoundException);
        }
    }

    @Test
    public void removeProductShouldThrowProductNotFoundExceptionWhenProductIsNotInWishList(){
        when(wishListRepository.findWishListByOwnerId(anyString())).thenReturn(Optional.of(createFullWishList()));
        try{
            underTest.removeProduct("Product100", "ownerId");
            fail("Should Throw ProductNotFoundException");
        }catch (Exception e){
            assertTrue(e instanceof ProductNotFoundException);
        }
    }
}
