package one.superstack.controllable.repository;

import one.superstack.controllable.model.PropertyValueLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyValueLogRepository extends MongoRepository<PropertyValueLog, String> {

}
