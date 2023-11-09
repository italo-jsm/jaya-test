package com.jaya.jayaproject.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@Data
@Document
public class WishList {
    @Id
    private String id;

    private String ownerId;

    private List<String> productIds = new ArrayList<>();

    public WishList(String ownerId) {
        this.id = UUID.randomUUID().toString();
        this.ownerId = ownerId;
    }


}
