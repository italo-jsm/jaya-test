package com.jaya.jayaproject.controller;


import com.jaya.jayaproject.requests.InsertProductRequest;
import com.jaya.jayaproject.service.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wishlist")
@RestController
@AllArgsConstructor
public class WishListControllerController {

    private WishListService wishListService;
    @PostMapping()
    public ResponseEntity<?> insertProductInWishList(@RequestBody InsertProductRequest insertProductRequest){
        wishListService.insertProductOnCustomerWishList(insertProductRequest.getCustomerId(), insertProductRequest.getProductId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-products")
    public ResponseEntity<?> findProductsByOwnerId(@RequestParam String customerId){
        return wishListService
                .getWishList(customerId)
                .map(wishList -> ResponseEntity.ok(wishList.getProductIds()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product-exists")
    public ResponseEntity<?> isProductInWishList(@RequestParam String customerId, @RequestParam String productId){
        if(wishListService.isProductInWishList(productId, customerId)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/remove-product")
    public ResponseEntity<?> removeProductFromWishList(@RequestParam String customerId, @RequestParam String productId){
       wishListService.removeProduct(productId, customerId);
       return ResponseEntity.ok().build();
    }
}
