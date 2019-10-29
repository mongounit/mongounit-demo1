package org.mongounit.demo.dao.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.mongounit.demo.dao.model.Address;
import org.mongounit.demo.dao.model.CreatePersonRequest;
import org.mongounit.demo.dao.model.Person;
import org.mongounit.demo.dao.model.Position;
import org.mongounit.demo.dao.model.UpdatePersonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
@DisplayName("BAD version of MongoPersonDaoService IT")
class BadMongoPersonDaoServiceIT {

  @Autowired
  private MongoPersonDaoService mongoPersonDaoService;

  @Autowired
  private MongoTemplate mongoTemplate;

  //@BeforeEach
  void beforeEach() {
    // I have to MANUALLY clean up the database?! 😱
    mongoTemplate.dropCollection(Person.class);
  }

  //@Test
  @DisplayName("Create person")
  void createPerson() {

    CreatePersonRequest request = CreatePersonRequest.builder()
        .name("Bob The Builder")
        .address(Address.builder().street("12 Builder St.").zipcode(12345).build())
        .favColors(Arrays.asList("red", "green"))
        .positionName("Builder")
        .build();

    Person expected = mongoPersonDaoService.createPerson(request);

    // Check if person is actually in db
    // Ah-oh! Using **expected** data to get **actual** data 🚩 - potential 💥
    Person actual = mongoTemplate.findById(expected.getId(), Person.class);

    assertEquals(expected.getName(), actual.getName(), "Name should be correct.");
    assertEquals(expected.getFavColors(), actual.getFavColors(), "Fav colors should be correct.");
    // It's very tedious to be testing like this! 🤯
  }

  //@Test
  @DisplayName("Update person")
  void updatePerson() {

    // Seed the database
    CreatePersonRequest createRequest = CreatePersonRequest.builder()
        .name("Bob The Builder")
        .address(Address.builder().street("12 Builder St.").zipcode(12345).build())
        .favColors(Arrays.asList("red", "green"))
        .positionName("Builder")
        .build();

    // Using THE API we are trying to get to SEED?! 🥶 😳
    // Alt. is to use manual MongoTemplate is super prone to mistakes - we'd need to test that!
    // At least we tested this method before, right?
    Person seededPerson = mongoPersonDaoService.createPerson(createRequest);

    // No way to get the position name! Need to use manual MongoTemplate findById!
    Position position = mongoTemplate.findById(seededPerson.getPositionId(), Position.class);

    UpdatePersonRequest updateRequest =
        new UpdatePersonRequest(
            seededPerson.getId(),
            position.getPositionName(),
            "Robert",
            seededPerson.getFavColors(),
            seededPerson.getAddress());

    Person expected = mongoPersonDaoService.updatePerson(updateRequest);

    // Check if person got updated in db
    // Ah-oh! Using **expected** data to get **actual** data 🚩 - potential 💥
    Person actual = mongoTemplate.findById(expected.getId(), Person.class);

    assertEquals("Robert", actual.getName(), "Name should be Robert now.");
    assertEquals(expected.getFavColors(), actual.getFavColors(), "Fav colors should be correct.");
    // It's very tedious to be testing like this! 🤯
  }

  //@Test
  void deletePerson() {

    // Seed the database
    CreatePersonRequest createRequest = CreatePersonRequest.builder()
        .name("Bob The Builder")
        .address(Address.builder().street("12 Builder St.").zipcode(12345).build())
        .favColors(Arrays.asList("red", "green"))
        .positionName("Builder")
        .build();

    // Using THE API we are trying to get to SEED?! 🥶 😳
    // Alt. is to use manual MongoTemplate is super prone to mistakes - we'd need to test that!
    // At least we tested this method before, right?
    Person seededPerson = mongoPersonDaoService.createPerson(createRequest);

    // Using what one part of the API gave us to test another part of the API 🚩 - potential 💥
    mongoPersonDaoService.deletePerson(seededPerson.getId());

    // Rely on manual verification that we actually deleted it?!
    Person actual = mongoTemplate.findById(seededPerson.getId(), Person.class);

    assertNull(actual, "Seeded person should not be in database.");
  }
}
