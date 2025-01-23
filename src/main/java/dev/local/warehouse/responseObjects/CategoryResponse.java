package dev.local.warehouse.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    @Id
    private String id;
    private String name;
    private String description;

    private List<SubcategoryInfo> subcategories;

}
