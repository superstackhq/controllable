package one.superstack.controllable.repository;

import one.superstack.controllable.model.PropertyValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyValueRepository extends MongoRepository<PropertyValue, String> {

}
