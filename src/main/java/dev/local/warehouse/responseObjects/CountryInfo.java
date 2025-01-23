package dev.local.warehouse.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryInfo {

    @Id
    private String id;
    private String name;
    private String code;

}