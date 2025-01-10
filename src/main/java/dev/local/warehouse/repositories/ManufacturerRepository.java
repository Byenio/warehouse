package dev.local.warehouse.repositories;

import dev.local.warehouse.models.Manufacturer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends MongoRepository<Manufacturer, ObjectId> {
}
