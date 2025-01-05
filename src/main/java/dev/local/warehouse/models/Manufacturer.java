package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "manufacturer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    private String id;
    private String name;
    private String description;
    private String logoUrl;
    private String website;

    @DocumentReference
    private Country countryId;

}
