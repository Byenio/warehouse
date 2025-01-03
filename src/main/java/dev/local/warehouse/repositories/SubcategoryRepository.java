package dev.local.warehouse.repositories;

import dev.local.warehouse.models.Subcategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepository extends MongoRepository<Subcategory, String> {
}
