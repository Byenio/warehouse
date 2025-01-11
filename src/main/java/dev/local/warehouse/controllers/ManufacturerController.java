package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Country;
import dev.local.warehouse.models.Manufacturer;
import dev.local.warehouse.repositories.CountryRepository;
import dev.local.warehouse.repositories.ManufacturerRepository;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;
    private final CountryRepository countryRepository;


    public ManufacturerController(
            final ManufacturerRepository manufacturerRepository,
            final CountryRepository countryRepository
    ) {
        this.manufacturerRepository = manufacturerRepository;
        this.countryRepository = countryRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final Manufacturer manufacturer) {
        if (manufacturer.getName() == null || manufacturer.getName().isEmpty()) {
            return new ResponseEntity<>("Manufacturer name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (manufacturer.getDescription() == null || manufacturer.getDescription().isEmpty()) {
            return new ResponseEntity<>("Manufacturer description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (manufacturer.getCode() == null || manufacturer.getCode().isEmpty()) {
            return new ResponseEntity<>("Manufacturer code cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (manufacturer.getWebsite() == null || manufacturer.getWebsite().isEmpty()) {
            return new ResponseEntity<>("Manufacturer website cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (manufacturer.getLogoUrl() == null || manufacturer.getLogoUrl().isEmpty()) {
            return new ResponseEntity<>("Manufacturer logoURL cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);

        if (manufacturer.getCountryId() == null || manufacturer.getCountryId().isEmpty()) {
            manufacturer.setCountryId(null);
            return new ResponseEntity<>(savedManufacturer, HttpStatus.CREATED);
        }

        ObjectId countryId = new ObjectId(manufacturer.getCountryId());
        Optional<Country> countryEntity = countryRepository.findById(countryId);

        if (countryEntity.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country country = countryEntity.get();
        country.getManufacturersId().add(manufacturer.getCountryId());
        countryRepository.save(country);

        return new ResponseEntity<>(savedManufacturer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAll() {
        return new ResponseEntity<>(manufacturerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getById(@PathVariable final ObjectId id) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);

        if (manufacturer.isEmpty()) {
            return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
        }

        Manufacturer manufacturerEntity = manufacturer.get();

        return new ResponseEntity<>(manufacturerEntity, HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable final ObjectId id) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);

        if (manufacturer.isEmpty()) {
            return new ResponseEntity<>("Manufacturer not found", HttpStatus.NOT_FOUND);
        }

        manufacturerRepository.delete(manufacturer.get());

        return new ResponseEntity<>("Manufacturer deleted", HttpStatus.NO_CONTENT);
    }
}
