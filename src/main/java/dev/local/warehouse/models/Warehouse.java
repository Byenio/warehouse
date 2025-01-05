package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "warehouse")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    private String id;
    private String name;
    private String imageUrl;

    @DocumentReference
    private List<Product> products;

}
