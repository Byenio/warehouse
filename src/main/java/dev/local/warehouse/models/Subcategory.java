package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subcategories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subcategory {

    @Id
    private ObjectId id;
    private String name;
    private String description;

}
