package org.mongounit.demo.dao.mongo;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mongounit.AssertMatchesDataset;
import org.mongounit.MongoUnitTest;
import org.mongounit.SeedWithDataset;
import org.mongounit.demo.dao.model.Address;
import org.mongounit.demo.dao.model.CreatePersonRequest;
import org.mongounit.demo.dao.model.UpdatePersonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MongoUnitTest
@DisplayName("MongoPersonDaoService with MongoUnit testing framework")
public class MongoPersonDaoServiceIT {

  @Autowired
  private MongoPersonDaoService mongoPersonDaoService;

  @Test
  @DisplayName("Create person on an empty database")
  @AssertMatchesDataset
  void createPerson() {

    CreatePersonRequest request = CreatePersonRequest.builder()
        .name("Bob The Builder")
        .address(Address.builder().street("12 Builder St.").zipcode(12345).build())
        .favColors(Arrays.asList("red", "green"))
        .positionName("Builder")
        .build();

    mongoPersonDaoService.createPerson(request);
  }

  // ***** NOTE: this test should fail because of a bug in the DAO service code
  // Uncomment @Test to enable this test
  //@Test
  @DisplayName("Create person on a non-empty database")
  @SeedWithDataset
  @AssertMatchesDataset
  void createPersonWithExistingData() {

    CreatePersonRequest request = CreatePersonRequest.builder()
        .name("Robert")
        .address(Address.builder().street("13 Builder St.").zipcode(12345).build())
        .favColors(Arrays.asList("blue", "white"))
        .positionName("Builder")
        .build();

    mongoPersonDaoService.createPerson(request);
  }

  // ***** NOTE: this test should fail because of a bug in the DAO service code
  // Uncomment @Test to enable this test
  //@Test
  @DisplayName("Update person")
  @SeedWithDataset("createPersonWithExistingData-seed.json")
  @AssertMatchesDataset
  void updatePerson() {

    UpdatePersonRequest updateRequest =
        new UpdatePersonRequest(
            "5db7545b7b615c739732c777",
            "Builder",
            "Robert",
            Arrays.asList("red", "green"),
            new Address("12 Builder St.", 12345));

    mongoPersonDaoService.updatePerson(updateRequest);
  }

  // ***** NOTE: this test should fail because of a bug in the DAO service code
  // Uncomment @Test to enable this test
  //@Test
  @DisplayName("Delete person")
  @SeedWithDataset("createPersonWithExistingData-seed.json")
  @AssertMatchesDataset(additionalDataset = false)
  void deletePerson() {
    mongoPersonDaoService.deletePerson("5db7545b7b615c739732c777");
  }
}



