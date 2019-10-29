package org.mongounit.demo.dao.mongo.repo;

import org.mongounit.demo.dao.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepo extends MongoRepository<Person, String> {

}
