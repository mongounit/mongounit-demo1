package org.mongounit.demo.dao.mongo;

import java.util.Date;
import org.mongounit.demo.dao.PersonDaoService;
import org.mongounit.demo.dao.model.CreatePersonRequest;
import org.mongounit.demo.dao.model.Person;
import org.mongounit.demo.dao.model.Position;
import org.mongounit.demo.dao.model.UpdatePersonRequest;
import org.mongounit.demo.dao.mongo.repo.PersonRepo;
import org.mongounit.demo.dao.mongo.repo.PositionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DAO service for {@link Person} entity.
 */
@Service
public class MongoPersonDaoService implements PersonDaoService {

  private final PersonRepo personRepo;

  private final PositionRepo positionRepo;

  @Autowired
  MongoPersonDaoService(final PersonRepo personRepo, final PositionRepo positionRepo) {
    this.personRepo = personRepo;
    this.positionRepo = positionRepo;
  }

  @Override
  public Person createPerson(CreatePersonRequest createPersonRequest) {

    // Create position
    Date now = new Date();
    Position position = Position.builder()
        .positionName(createPersonRequest.getPositionName())
        .created(now)
        .updated(now)
        .build();
    Position savedPosition = positionRepo.save(position);

    // Create Person
    Person person = Person.builder()
        .name(createPersonRequest.getName())
        .positionId(savedPosition.getId())
        .favColors(createPersonRequest.getFavColors())
        .address(createPersonRequest.getAddress())
        .created(now)
        .updated(now)
        .build();

    return personRepo.save(person);
  }

  @Override
  public Person getPerson(String id) {
    return personRepo.findById(id).orElseThrow(RuntimeException::new);
  }

  @Override
  public Person updatePerson(UpdatePersonRequest updatePersonRequest) {

    // Create position
    Date now = new Date();
    Position position = Position.builder()
        .positionName(updatePersonRequest.getPositionName())
        .created(now)
        .updated(now)
        .build();
    Position savedPosition = positionRepo.save(position);

    // Get existing person
    Person existingPerson = getPerson(updatePersonRequest.getId());

    // Update existing person
    existingPerson.setName(updatePersonRequest.getName());
    existingPerson.setPositionId(savedPosition.getId());
    existingPerson.setFavColors(updatePersonRequest.getFavColors());
    existingPerson.setAddress(updatePersonRequest.getAddress());

    return personRepo.save(existingPerson);
  }

  @Override
  public void deletePerson(String id) {
    personRepo.deleteById(id);
  }
}
