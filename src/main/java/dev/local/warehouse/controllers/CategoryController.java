package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Category;
import dev.local.warehouse.models.Subcategory;
import dev.local.warehouse.repositories.CategoryRepository;
import dev.local.warehouse.repositories.SubcategoryRepository;
import dev.local.warehouse.responseObjects.CategoryResponse;
import dev.local.warehouse.responseObjects.SubcategoryInfo;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/category"})
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    public CategoryController(
            final CategoryRepository categoryRepository,
            final SubcategoryRepository subcategoryRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody final Category category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            return new ResponseEntity<>("Category name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (category.getDescription() == null || category.getDescription().isEmpty()) {
            return new ResponseEntity<>("Category description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Category savedCategory = categoryRepository.save(category);

        for (String subcategoryId : category.getSubcategoriesId()) {
            ObjectId subcategoryIdObj = new ObjectId(subcategoryId);
            Optional<Subcategory> subcategoryEntity = subcategoryRepository.findById(subcategoryIdObj);

            if (subcategoryEntity.isEmpty()) {
                return new ResponseEntity<>(String.format("Subcategory not found: %s", subcategoryId), HttpStatus.NOT_FOUND);
            }

            Subcategory subcategory = subcategoryEntity.get();
            subcategory.setCategoryId(savedCategory.getId());
            subcategoryRepository.save(subcategory);
        }

        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> response = new ArrayList<>();

        for (Category category : categories) {
            List<SubcategoryInfo> subcategories = new ArrayList<>();

            for (String subcategoryId : category.getSubcategoriesId()) {
                Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(subcategoryId));

                if (subcategory.isEmpty()) {
                    return new ResponseEntity<>(String.format("Subcategory not found: %s", subcategoryId), HttpStatus.NOT_FOUND);
                }

                Subcategory subcategoryEntity = subcategory.get();

                subcategories.add(new SubcategoryInfo(
                        subcategoryEntity.getId(),
                        subcategoryEntity.getName(),
                        subcategoryEntity.getDescription()
                ));
            }

            response.add(new CategoryResponse(
                    category.getId(),
                    category.getName(),
                    category.getDescription(),
                    subcategories
            ));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getById(@PathVariable final ObjectId id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();

        List<SubcategoryInfo> subcategories = new ArrayList<>();

        for (String subcategoryId : categoryEntity.getSubcategoriesId()) {
            Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(subcategoryId));

            if (subcategory.isEmpty()) {
                return new ResponseEntity<>(String.format("Subcategory not found: %s", subcategoryId), HttpStatus.NOT_FOUND);
            }

            Subcategory subcategoryEntity = subcategory.get();

            subcategories.add(new SubcategoryInfo(
                    subcategoryEntity.getId(),
                    subcategoryEntity.getName(),
                    subcategoryEntity.getDescription()
            ));
        }

        CategoryResponse response = new CategoryResponse(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getDescription(),
                subcategories
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/{id}/subcategories"})
    public ResponseEntity<?> getSubcategories(@PathVariable final ObjectId id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();

        return new ResponseEntity<>(categoryEntity.getSubcategoriesId(), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable final ObjectId id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        categoryRepository.delete(category.get());

        return new ResponseEntity<>("Category deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping({"/{id}/subcategories"})
    @Transactional
    public ResponseEntity<?> addSubcategories(@PathVariable final ObjectId id, @RequestBody final List<String> subcategoriesId) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();

        for (String subcategoryId : subcategoriesId) {
            Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(subcategoryId));

            if (subcategory.isEmpty()) {
                return new ResponseEntity<>(String.format("Subcategory not found: %s", subcategoryId), HttpStatus.NOT_FOUND);
            }

            Subcategory subcategoryEntity = subcategory.get();

            subcategoryEntity.setCategoryId(categoryEntity.getId());
            subcategoryRepository.save(subcategoryEntity);

            if (!categoryEntity.getSubcategoriesId().contains(subcategoryId)) {
                categoryEntity.getSubcategoriesId().add(subcategoryId);
            }
        }

        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.CREATED);
    }

    @DeleteMapping({"/{id}/subcategories"})
    @Transactional
    public ResponseEntity<?> deleteSubcategories(@PathVariable final ObjectId id, @RequestBody final List<String> subcategoriesId) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        Category categoryEntity = category.get();

        for (String subcategoryId : subcategoriesId) {
            Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(subcategoryId));

            if (subcategory.isEmpty()) {
                return new ResponseEntity<>(String.format("Subcategory not found: %s", subcategoryId), HttpStatus.NOT_FOUND);
            }

            Subcategory subcategoryEntity = subcategory.get();

            subcategoryEntity.setCategoryId(null);
            subcategoryRepository.save(subcategoryEntity);

            categoryEntity.getSubcategoriesId().remove(subcategoryId);
        }

        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.NO_CONTENT);
    }

}
