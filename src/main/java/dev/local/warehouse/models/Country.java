package dev.local.warehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "country")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @Id
    private String id;
    private String name;
    private String code;

    private List<String> manufacturersId;

}
