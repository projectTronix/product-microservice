package com.mayank.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String productId;
    private String title;
    private Integer quantityAvailable;
    private String description;
    private String categoryID;
    private List<Specification> specifications;
    private Integer price;
    private String imageUrl;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Specification {
        private String name;
        private String value;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", title='" + title + '\'' +
                ", quantityAvailable=" + quantityAvailable +
                ", description='" + description + '\'' +
                ", categoryID='" + categoryID + '\'' +
                ", specifications=" + specifications +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
