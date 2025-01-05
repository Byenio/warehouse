package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Subcategory;
import dev.local.warehouse.repositories.SubcategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
public class SubcategoryController {

    private final SubcategoryRepository subcategoryRepository;

    public SubcategoryController(SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }

    @PostMapping
    public ResponseEntity<Subcategory> create(@RequestBody Subcategory subcategory) {
        return new ResponseEntity<>(subcategoryRepository.save(subcategory), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Subcategory>> getAll() {
        return new ResponseEntity<>(subcategoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Subcategory> getById(@PathVariable ObjectId id) {
        return subcategoryRepository.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<String> delete(@PathVariable ObjectId id) {
        return subcategoryRepository.findById(id)
                .map(subcategory -> {
                    subcategoryRepository.delete(subcategory);
                    return new ResponseEntity<String>("Successfully deleted subcategory", HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
