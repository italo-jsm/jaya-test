package com.jaya.jayaproject.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertProductRequest {
    private String customerId;
    private String productId;
}
