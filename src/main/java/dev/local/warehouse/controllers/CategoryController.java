package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Category;
import dev.local.warehouse.models.Subcategory;
import dev.local.warehouse.repositories.CategoryRepository;
import dev.local.warehouse.repositories.SubcategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public CategoryController(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        if (category.getSubcategories() == null) {
            category.setSubcategories(List.of());
        }

        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Category> getById(@PathVariable ObjectId id) {
        return categoryRepository.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<String> delete(@PathVariable ObjectId id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return new ResponseEntity<String>("Successfully deleted category", HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/subcategories")
    public ResponseEntity<Category> addSubcategories(
            @PathVariable ObjectId id,
            @RequestBody List<ObjectId> subcategoryIds
    ) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();
        List<Subcategory> subcategories = subcategoryRepository.findAllById(subcategoryIds);
        categoryEntity.getSubcategories().addAll(subcategories);

        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.OK);
    }

    @PutMapping({"/{id}/subcategories/{subcategoryId}"})
    public ResponseEntity<Category> addSubcategory(@PathVariable ObjectId id, @PathVariable ObjectId subcategoryId) {
        Optional<Category> category = categoryRepository.findById(id);
        Optional<Subcategory> subcategory = subcategoryRepository.findById(subcategoryId);

        if (category.isEmpty() || subcategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();
        categoryEntity.getSubcategories().add(subcategory.get());

        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/subcategories/all")
    public ResponseEntity<String> deleteAllSubcategories(@PathVariable ObjectId id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();
        categoryEntity.getSubcategories().clear();

        return new ResponseEntity<>("Successfully deleted category", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/subcategories")
    public ResponseEntity<Category> removeSubcategories(
            @PathVariable ObjectId id,
            @RequestBody List<String> subcategoryIds
    ) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();
        categoryEntity.getSubcategories().removeIf(subcategory -> subcategoryIds.contains(subcategory.getId()));

        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}/subcategories/{subcategoryId}"})
    public ResponseEntity<Category> removeSubcategory(@PathVariable ObjectId id, @PathVariable ObjectId subcategoryId) {
        Optional<Category> category = categoryRepository.findById(id);
        Optional<Subcategory> subcategory = subcategoryRepository.findById(subcategoryId);

        if (category.isEmpty() || subcategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();
        categoryEntity.getSubcategories().remove(subcategory.get());

        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.OK);
    }

}
