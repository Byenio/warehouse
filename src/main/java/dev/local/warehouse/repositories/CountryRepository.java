package dev.local.warehouse.repositories;

import dev.local.warehouse.models.Country;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends MongoRepository<Country, ObjectId> {
}
