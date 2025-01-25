package dev.local.warehouse.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @Id
    private String id;
    private String name;
    private String description;

    private String barcode;
    private String imageUrl;
    private Integer netPriceInCents;
    private Integer grossPriceInCents;
    private Integer stock;

    private SubcategoryInfo subcategory;
    private ManufacturerInfo manufacturer;
    private String warehouseId;

}
