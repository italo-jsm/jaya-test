package com.jaya.jayaproject.repository;

import com.jaya.jayaproject.domain.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishListRepository extends MongoRepository<WishList, String> {
    Optional<WishList> findWishListByOwnerId(String ownerId);
}
