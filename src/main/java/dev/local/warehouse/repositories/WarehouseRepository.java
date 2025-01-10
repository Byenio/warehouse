package dev.local.warehouse.repositories;

import dev.local.warehouse.models.Warehouse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, ObjectId> {
}
