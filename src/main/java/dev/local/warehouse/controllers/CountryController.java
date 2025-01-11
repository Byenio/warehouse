package dev.local.warehouse.controllers;

import dev.local.warehouse.models.Country;
import dev.local.warehouse.models.Manufacturer;
import dev.local.warehouse.repositories.CountryRepository;
import dev.local.warehouse.repositories.ManufacturerRepository;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/country"})
public class CountryController {

    private final CountryRepository countryRepository;
    private final ManufacturerRepository manufacturerRepository;

    public CountryController(
            final CountryRepository countryRepository,
            final ManufacturerRepository manufacturerRepository
    ) {
        this.countryRepository = countryRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody final Country country) {
        if (country.getName() == null || country.getName().isEmpty()) {
            return new ResponseEntity<>("Country name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (country.getCode() == null || country.getCode().isEmpty()) {
            return new ResponseEntity<>("Country code cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Country savedCountry = countryRepository.save(country);

        for (String manufacturerId : country.getManufacturersId()) {
            ObjectId manufacturerIdObj = new ObjectId(manufacturerId);
            Optional<Manufacturer> manufacturerEntity = manufacturerRepository.findById(manufacturerIdObj);

            if (manufacturerEntity.isEmpty()) {
                return new ResponseEntity<>(String.format("Manufacturer not found: %s", manufacturerId), HttpStatus.NOT_FOUND);
            }

            Manufacturer manufacturer = manufacturerEntity.get();
            manufacturer.setCountryId(savedCountry.getId());
            manufacturerRepository.save(manufacturer);
        }

        return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getById(@PathVariable final ObjectId id) {
        Optional<Country> country = countryRepository.findById(id);

        if (country.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country countryEntity = country.get();

        return new ResponseEntity<>(countryEntity.getManufacturersId(), HttpStatus.OK);
    }

    @GetMapping({"/{id}/manufacturers"})
    public ResponseEntity<?> getManufacturers(@PathVariable final ObjectId id) {
        Optional<Country> country = countryRepository.findById(id);

        if (country.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country countryEntity = country.get();

        return new ResponseEntity<>(countryEntity.getManufacturersId(), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable final ObjectId id) {
        Optional<Country> country = countryRepository.findById(id);

        if (country.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        countryRepository.delete(country.get());

        return new ResponseEntity<>("Country deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping({"/{id}/manufacturers"})
    public ResponseEntity<?> addManufacturers(@PathVariable final ObjectId id, @RequestBody final List<String> manufacturersId) {
        Optional<Country> country = countryRepository.findById(id);

        if (country.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country countryEntity = country.get();

        for (String manufacturerID : manufacturersId) {
            Optional<Manufacturer> manufacturer = manufacturerRepository.findById(new ObjectId(manufacturerID));

            if (manufacturer.isEmpty()) {
                return new ResponseEntity<>(String.format("Manufacturer not found: %s", manufacturerID), HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(countryRepository.save(countryEntity), HttpStatus.CREATED);

    }

    @DeleteMapping({"/{id}/manufacturers"})
    @Transactional
    public ResponseEntity<?> deleteManufacturers(@PathVariable final ObjectId id, @RequestBody final List<String> manufacturersId) {
        Optional<Country> country = countryRepository.findById(id);

        if (country.isEmpty()) {
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);
        }

        Country countryEntity = country.get();

        for (String manufacturerId : manufacturersId) {
            Optional<Manufacturer> manufacturer = manufacturerRepository.findById(new ObjectId(manufacturerId));

            if (manufacturer.isEmpty()) {
                return new ResponseEntity<>(String.format("Manufacturer not found: %s", manufacturerId), HttpStatus.NOT_FOUND);
            }

            Manufacturer manufacturerEntity = manufacturer.get();

            manufacturerEntity.setCountryId(null);
            manufacturerRepository.save(manufacturerEntity);

            countryEntity.getManufacturersId().remove(manufacturerId);
        }

        return new ResponseEntity<>(countryRepository.save(countryEntity), HttpStatus.NO_CONTENT);
    }
}
