package dev.local.warehouse.controllers;

import dev.local.warehouse.models.*;
import dev.local.warehouse.repositories.*;
import dev.local.warehouse.responseObjects.CountryInfo;
import dev.local.warehouse.responseObjects.ManufacturerInfo;
import dev.local.warehouse.responseObjects.ProductResponse;
import dev.local.warehouse.responseObjects.SubcategoryInfo;
import dev.local.warehouse.utils.EAN13Generator;
import dev.local.warehouse.utils.EAN13Validator;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/product"})
public class ProductController {

    private final ProductRepository productRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CountryRepository countryRepository;
    private final WarehouseRepository warehouseRepository;

    public ProductController(
            final ProductRepository productRepository,
            final SubcategoryRepository subcategoryRepository,
            final ManufacturerRepository manufacturerRepository,
            final CountryRepository countryRepository,
            final WarehouseRepository warehouseRepository
    ) {
        this.productRepository = productRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.countryRepository = countryRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody final Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            return new ResponseEntity<>("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
            return new ResponseEntity<>("Image Url cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (product.getVatPercentage() == null) {
            return new ResponseEntity<>("Vat percentage cannot be empty", HttpStatus.BAD_REQUEST);
        }

        boolean hasNetPrice = product.getNetPriceInCents() != null;
        boolean hasGrossPrice = product.getGrossPriceInCents() != null;

        if (hasNetPrice && hasGrossPrice) {
            return new ResponseEntity<>("You must provide only net price or only gross price", HttpStatus.BAD_REQUEST);
        }

        if (!hasNetPrice && !hasGrossPrice) {
            return new ResponseEntity<>("Either net price or gross price must be provided", HttpStatus.BAD_REQUEST);
        }

        if (hasNetPrice) {
            int grossPriceInCents = Math.round(product.getNetPriceInCents() * (1 + product.getVatPercentage() / 100));
            product.setGrossPriceInCents(grossPriceInCents);
        } else {
            int netPriceInCents = Math.round(product.getGrossPriceInCents() / (1 + product.getVatPercentage() / 100));
            product.setNetPriceInCents(netPriceInCents);
        }

        if (product.getStock() == null) {
            return new ResponseEntity<>("Stock cannot be null", HttpStatus.BAD_REQUEST);
        }

        Product savedProduct = productRepository.save(product);

        if (product.getSubcategoryId() == null || product.getSubcategoryId().isEmpty()) {
            product.setSubcategoryId(null);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }

        ObjectId subcategoryId = new ObjectId(product.getSubcategoryId());
        Optional<Subcategory> subcategoryEntity = subcategoryRepository.findById(subcategoryId);

        if (subcategoryEntity.isEmpty()) {
            return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
        }

        Subcategory subcategory = subcategoryEntity.get();
        subcategory.getProductsId().add(product.getId());
        subcategoryRepository.save(subcategory);

        ObjectId manufacturerId = new ObjectId(product.getManufacturerId());
        Optional<Manufacturer> manufacturerEntity = manufacturerRepository.findById(manufacturerId);

        if (manufacturerEntity.isEmpty()) {
            return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
        }

        Manufacturer manufacturer = manufacturerEntity.get();

        Optional<Country> countryEntity = countryRepository.findById(new ObjectId(manufacturer.getCountryId()));

        if (countryEntity.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country country = countryEntity.get();

        EAN13Generator generator = new EAN13Generator();
        String currentBarcode = product.getBarcode();
        if (currentBarcode == null || currentBarcode.isEmpty()) {
            currentBarcode = generator.GenerateBarcode(country.getCode(), manufacturer.getCode());
        }
        do {
            Optional<Product> productEntity = productRepository.findByBarcode(currentBarcode);

            if (productEntity.isEmpty()) {
                product.setBarcode(currentBarcode);
                break;
            }

            currentBarcode = generator.GenerateBarcode(country.getCode(), manufacturer.getCode());

        } while (true);

        productRepository.save(savedProduct);

        EAN13Validator validator = new EAN13Validator();

        if (!validator.ValidateBarcode(product.getBarcode(), country.getCode(), manufacturer.getCode())) {
            return new ResponseEntity<>("Barcode is invalid, please refer to EAN13", HttpStatus.BAD_REQUEST);
        }

        manufacturer.getProductsId().add(product.getId());
        manufacturerRepository.save(manufacturer);

        ObjectId warehouseId = new ObjectId(product.getWarehouseId());
        Optional<Warehouse> warehouseEntity = warehouseRepository.findById(warehouseId);

        if (warehouseEntity.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouse = warehouseEntity.get();
        warehouse.getProductsId().add(product.getId());
        warehouseRepository.save(warehouse);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = new ArrayList<>();

        for (Product product : products) {
            Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(product.getSubcategoryId()));

            if (subcategory.isEmpty()) {
                return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
            }

            Subcategory subcategoryEntity = subcategory.get();

            Optional<Manufacturer> manufacturer = manufacturerRepository.findById(new ObjectId(product.getManufacturerId()));

            if (manufacturer.isEmpty()) {
                return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
            }

            Manufacturer manufacturerEntity = manufacturer.get();

            Optional<Country> country = countryRepository.findById(new ObjectId(manufacturerEntity.getCountryId()));

            if (country.isEmpty()) {
                return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
            }

            Country countryEntity = country.get();

            SubcategoryInfo subcategoryInfo = new SubcategoryInfo(
                    subcategoryEntity.getId(),
                    subcategoryEntity.getName(),
                    subcategoryEntity.getDescription()
            );

            CountryInfo countryInfo = new CountryInfo(
                    countryEntity.getId(),
                    countryEntity.getName(),
                    countryEntity.getCode()
            );

            ManufacturerInfo manufacturerInfo = new ManufacturerInfo(
                    manufacturerEntity.getId(),
                    manufacturerEntity.getName(),
                    manufacturerEntity.getDescription(),
                    manufacturerEntity.getCode(),
                    manufacturerEntity.getLogoUrl(),
                    manufacturerEntity.getWebsite(),
                    countryInfo
            );

            response.add(new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getBarcode(),
                    product.getImageUrl(),
                    product.getNetPriceInCents(),
                    product.getGrossPriceInCents(),
                    product.getStock(),
                    subcategoryInfo,
                    manufacturerInfo,
                    product.getWarehouseId()
            ));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getById(@PathVariable final ObjectId id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        Product productEntity = product.get();

        Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(productEntity.getSubcategoryId()));

        if (subcategory.isEmpty()) {
            return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
        }

        Subcategory subcategoryEntity = subcategory.get();

        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(new ObjectId(productEntity.getManufacturerId()));

        if (manufacturer.isEmpty()) {
            return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
        }

        Manufacturer manufacturerEntity = manufacturer.get();

        Optional<Country> country = countryRepository.findById(new ObjectId(manufacturerEntity.getCountryId()));

        if (country.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country countryEntity = country.get();

        SubcategoryInfo subcategoryInfo = new SubcategoryInfo(
                subcategoryEntity.getId(),
                subcategoryEntity.getName(),
                subcategoryEntity.getDescription()
        );

        CountryInfo countryInfo = new CountryInfo(
                countryEntity.getId(),
                countryEntity.getName(),
                countryEntity.getCode()
        );

        ManufacturerInfo manufacturerInfo = new ManufacturerInfo(
                manufacturerEntity.getId(),
                manufacturerEntity.getName(),
                manufacturerEntity.getDescription(),
                manufacturerEntity.getCode(),
                manufacturerEntity.getLogoUrl(),
                manufacturerEntity.getWebsite(),
                countryInfo
        );

        ProductResponse response = new ProductResponse(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getBarcode(),
                productEntity.getImageUrl(),
                productEntity.getNetPriceInCents(),
                productEntity.getGrossPriceInCents(),
                productEntity.getStock(),
                subcategoryInfo,
                manufacturerInfo,
                productEntity.getWarehouseId()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable final ObjectId id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        productRepository.delete(product.get());

        return new ResponseEntity<>("Product deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping({"/{id}/subcategory"})
    public ResponseEntity<?> addSubcategory(@PathVariable final ObjectId id, @RequestBody final String subcategoryId) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        Product productEntity = product.get();

        Optional<Subcategory> subcategory = subcategoryRepository.findById(new ObjectId(subcategoryId));

        if (subcategory.isEmpty()) {
            return new ResponseEntity<>("Subcategory not found", HttpStatus.NOT_FOUND);
        }

        Subcategory subcategoryEntity = subcategory.get();

        productEntity.setSubcategoryId(subcategoryEntity.getId());
        productRepository.save(productEntity);

        subcategoryEntity.getProductsId().add(productEntity.getId());
        subcategoryRepository.save(subcategoryEntity);

        return new ResponseEntity<>(productEntity, HttpStatus.CREATED);
    }

    @PostMapping({"/{id}/manufacturer"})
    public ResponseEntity<?> addManufacturer(@PathVariable final ObjectId id, @RequestBody final String manufacturerId) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        Product productEntity = product.get();

        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(new ObjectId(manufacturerId));

        if (manufacturer.isEmpty()) {
            return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
        }

        Manufacturer manufacturerEntity = manufacturer.get();

        productEntity.setManufacturerId(manufacturerEntity.getId());
        productRepository.save(productEntity);

        manufacturerEntity.getProductsId().add(productEntity.getId());
        manufacturerRepository.save(manufacturerEntity);

        return new ResponseEntity<>(productEntity, HttpStatus.CREATED);
    }

    @PostMapping({"/{id}/warehouse"})
    public ResponseEntity<?> addWarehouse(@PathVariable final ObjectId id, @RequestBody final String warehouseId) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }

        Product productEntity = product.get();

        Optional<Warehouse> warehouse = warehouseRepository.findById(new ObjectId(warehouseId));

        if (warehouse.isEmpty()) {
            return new ResponseEntity<>("Warehouse not found", HttpStatus.NOT_FOUND);
        }

        Warehouse warehouseEntity = warehouse.get();

        productEntity.setWarehouseId(warehouseEntity.getId());
        productRepository.save(productEntity);

        warehouseEntity.getProductsId().add(productEntity.getId());
        warehouseRepository.save(warehouseEntity);

        return new ResponseEntity<>(productEntity, HttpStatus.CREATED);
    }

}
