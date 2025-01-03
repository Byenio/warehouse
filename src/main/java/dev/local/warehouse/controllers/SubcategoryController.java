package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Subcategory;
import dev.local.warehouse.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategory")
public class SubcategoryController {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @PostMapping
    public Subcategory create(@RequestBody Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }
}
