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
@RequestMapping("/api/subcategory")
public class SubcategoryController {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubcategoryController(
            final SubcategoryRepository subcategoryRepository,
            final CategoryRepository categoryRepository
    ) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final Subcategory subcategory) {
        if (subcategory.getName() == null || subcategory.getName().isEmpty()) {
            return new ResponseEntity<>("Subcategory name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (subcategory.getDescription() == null || subcategory.getDescription().isEmpty()) {
            return new ResponseEntity<>("Subcategory description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);

        if (subcategory.getCategoryId() == null || subcategory.getCategoryId().isEmpty()) {
            subcategory.setCategoryId(null);
            return new ResponseEntity<>(savedSubcategory, HttpStatus.CREATED);
        }

        ObjectId categoryId = new ObjectId(subcategory.getCategoryId());
        Optional<Category> categoryEntity = categoryRepository.findById(categoryId);

        if (categoryEntity.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        Category category = categoryEntity.get();
        category.getSubcategoriesId().add(subcategory.getCategoryId());
        categoryRepository.save(category);

        return new ResponseEntity<>(savedSubcategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Subcategory>> getAll() {
        return new ResponseEntity<>(subcategoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getById(@PathVariable final ObjectId id) {
        Optional<Subcategory> subcategory = subcategoryRepository.findById(id);

        if (subcategory.isEmpty()) {
            return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
        }

        Subcategory subcategoryEntity = subcategory.get();

        return new ResponseEntity<>(subcategoryEntity, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable final ObjectId id) {
        Optional<Subcategory> subcategory = subcategoryRepository.findById(id);

        if (subcategory.isEmpty()) {
            return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
        }

        subcategoryRepository.delete(subcategory.get());

        return new ResponseEntity<>("Subcategory deleted", HttpStatus.NO_CONTENT);
    }

}
