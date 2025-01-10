package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    private String name;
    private String barcode;             // [0-2] country | [3-7] manufacturer | [8-12] product | [13] control
    private String description;
    private String imageUrl;
    private Integer netPriceInCents;
    private Integer vatPercentage;
    private Integer grossPriceInCents;
    private Integer stock;

    private String subcategoryId;
    private String manufacturerId;
    private String warehouseId;

}
