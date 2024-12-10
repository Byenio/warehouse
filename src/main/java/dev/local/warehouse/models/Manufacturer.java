package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "manufacturers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    private ObjectId id;
    private String name;
    private String description;
    private String logoUrl;
    private String website;

    @DocumentReference
    private Country countryId;

}
