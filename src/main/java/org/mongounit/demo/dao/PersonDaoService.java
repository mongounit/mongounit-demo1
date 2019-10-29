package org.mongounit.demo.dao;

import org.mongounit.demo.dao.model.CreatePersonRequest;
import org.mongounit.demo.dao.model.Person;
import org.mongounit.demo.dao.model.UpdatePersonRequest;

public interface PersonDaoService {

  Person createPerson(CreatePersonRequest createPersonRequest);

  Person getPerson(String id);

  Person updatePerson(UpdatePersonRequest updatePersonRequest);

  void deletePerson(String id);
}
