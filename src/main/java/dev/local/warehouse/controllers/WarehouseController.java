package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Product;
import dev.local.warehouse.models.Warehouse;
import dev.local.warehouse.repositories.ProductRepository;
import dev.local.warehouse.repositories.WarehouseRepository;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/warehouse"})
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    public WarehouseController(
            final WarehouseRepository warehouseRepository,
            final ProductRepository productRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final Warehouse warehouse) {
        if (warehouse.getName() == null || warehouse.getName().isEmpty()) {
            return new ResponseEntity<>("Warehouse name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (warehouse.getImageUrl() == null || warehouse.getImageUrl().isEmpty()) {
            return new ResponseEntity<>("Image url cannot be empty", HttpStatus.BAD_REQUEST);
        }

        warehouse.setProductsId(null);

        return new ResponseEntity<>(warehouseRepository.save(warehouse), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(warehouseRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getById(@PathVariable final ObjectId id) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);

        if (warehouse.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouseEntity = warehouse.get();

        return new ResponseEntity<>(warehouseEntity, HttpStatus.OK);
    }

    @GetMapping({"/{id}/products"})
    public ResponseEntity<?> getProducts(@PathVariable final ObjectId id) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);

        if (warehouse.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouseEntity = warehouse.get();

        return new ResponseEntity<>(warehouseEntity.getProductsId(), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable final ObjectId id) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);

        if (warehouse.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        warehouseRepository.delete(warehouse.get());

        return new ResponseEntity<>("Warehouse deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping({"/{id}/products"})
    @Transactional
    public ResponseEntity<?> addProducts(@PathVariable final ObjectId id, @RequestBody final List<String> productsId) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);

        if (warehouse.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouseEntity = warehouse.get();

        for (String productId : productsId) {
            Optional<Product> product = productRepository.findById(new ObjectId(productId));

            if (product.isEmpty()) {
                return new ResponseEntity<>(String.format("Product not found: %s", productId), HttpStatus.NOT_FOUND);
            }

            Product productEntity = product.get();

            productEntity.setWarehouseId(warehouseEntity.getId());
            productRepository.save(productEntity);

            if (!warehouseEntity.getProductsId().contains(productId)) {
                warehouseEntity.getProductsId().add(productId);
            }
        }

        return new ResponseEntity<>(warehouseRepository.save(warehouseEntity), HttpStatus.CREATED);
    }

    @DeleteMapping({"/{id}/products"})
    @Transactional
    public ResponseEntity<?> deleteProducts(@PathVariable final ObjectId id, @RequestBody final List<String> productsId) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(id);

        if (warehouse.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouseEntity = warehouse.get();

        for (String productId : productsId) {
            Optional<Product> product = productRepository.findById(new ObjectId(productId));

            if (product.isEmpty()) {
                return new ResponseEntity<>(String.format("Product not found: %s", productId), HttpStatus.NOT_FOUND);
            }

            Product productEntity = product.get();

            productEntity.setWarehouseId(warehouseEntity.getId());
            productRepository.save(productEntity);

            warehouseEntity.getProductsId().remove(productId);
        }

        return new ResponseEntity<>(warehouseRepository.save(warehouseEntity), HttpStatus.NO_CONTENT);
    }

}
