package com.mayank.product.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String productId;
    private String title;
    private Integer quantityAvailable;
    private String description;
    private String categoryID;
    private String categoryTitle;
    private List<Specification> specifications;
    private Integer price;
    private String imageUrl;


    @Data
    public static class Specification {
        private String name;
        private String value;
    }

}
