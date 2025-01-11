package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "manufacturer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    private String id;
    private String name;
    private String description;
    private String code;
    private String logoUrl;
    private String website;

    private String countryId;
    private List<String> productsId;

}
