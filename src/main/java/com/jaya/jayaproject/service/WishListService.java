package com.jaya.jayaproject.service;

import com.jaya.jayaproject.domain.WishList;
import com.jaya.jayaproject.exceptions.FullWishListException;
import com.jaya.jayaproject.exceptions.ProductExistsException;
import com.jaya.jayaproject.exceptions.ProductNotFoundException;
import com.jaya.jayaproject.exceptions.WishListNotFoundException;
import com.jaya.jayaproject.repository.WishListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class WishListService {
    public static final int MAX_PRODUCTS = 30;
    private WishListRepository wishListRepository;

    public void insertProductOnCustomerWishList(String customerId, String productId){
        WishList wishList = wishListRepository.findWishListByOwnerId(customerId)
                .orElse(new WishList(customerId));
        verifyWishListFull(wishList);
        verifyProductExistsInWishList(wishList, productId);
        wishList.getProductIds().add(productId);
        wishListRepository.save(wishList);
    }

    public Optional<WishList> getWishList(String ownerId){
        return wishListRepository.findWishListByOwnerId(ownerId);
    }

    public boolean isProductInWishList(String productId, String ownerId){
        return wishListRepository.findWishListByOwnerId(ownerId)
                .map(wishList -> wishList.getProductIds()
                        .stream()
                        .anyMatch(existingProductId -> Objects.equals(productId, existingProductId)))
                .orElse(false);
    }

    public void removeProduct(String productId, String ownerId){
        wishListRepository.findWishListByOwnerId(ownerId)
            .map(wishList -> {
                verifyProductNotExistsInWishList(wishList, productId);
                wishList.getProductIds()
                        .remove(productId);
                return wishListRepository.save(wishList);
            }).orElseThrow(WishListNotFoundException::new);
    }

    private void verifyProductExistsInWishList(WishList wishList, String productId){
        if (wishList.getProductIds().stream().anyMatch(existingProductId -> Objects.equals(productId, existingProductId))){
            throw new ProductExistsException("Product is already in wishlist");
        }
    }

    private void verifyProductNotExistsInWishList(WishList wishList, String productId){
        if (wishList.getProductIds().stream().noneMatch(existingProductId -> Objects.equals(productId, existingProductId))){
            throw new ProductNotFoundException("Product Not Found in WishList");
        }
    }

    private void verifyWishListFull(WishList wishList){
        if(wishList.getProductIds().size() == MAX_PRODUCTS){
            throw new FullWishListException("Maximum number of products in wish list is " + MAX_PRODUCTS);
        }
    }
}
