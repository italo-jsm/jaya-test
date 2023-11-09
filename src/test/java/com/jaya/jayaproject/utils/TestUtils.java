package com.jaya.jayaproject.utils;

import com.jaya.jayaproject.domain.WishList;

import java.util.UUID;

public class TestUtils {
    public static WishList createWishList(){
        return new WishList(UUID.randomUUID().toString());
    }

    public static WishList createExistingWishList(){
        WishList wishList = new WishList();
        wishList.setId("WishListId");
        wishList.setOwnerId("ownerId");
        return wishList;
    }

    public static WishList createExistingWishListWithOneProduct(){
        WishList wishList = new WishList();
        wishList.setId("WishListId");
        wishList.setOwnerId("ownerId");
        wishList.getProductIds().add("productId");
        return wishList;
    }

    public static boolean isUUID(String uuid){
        return UUID.fromString(uuid).toString().equals(uuid);
    }

    public static WishList createFullWishList(){
        WishList wishList = new WishList();
        wishList.setId("WishListId");
        wishList.setOwnerId("ownerId");
        for(int i = 0 ; i < 30 ; i++){
            wishList.getProductIds().add("Product" + i);
        }
        return wishList;
    }
}
