package com.webforce.SafetyNet;

import com.webforce.SafetyNet.controller.PersonController;
import com.webforce.SafetyNet.model.Person;
import com.webforce.SafetyNet.repository.PersonRepository;
import static org.assertj.core.api.Assertions.*;

import com.webforce.SafetyNet.service.PersonService;
import com.webforce.SafetyNet.service.dto.ChildAlertDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



import java.util.List;




@SpringBootTest
public class PersonControllerTest {

    @Autowired
    PersonController personController;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @BeforeAll
    private static void setUp() throws  Exception {

    }

    @Test
    void listeEmailsTest() {

        Assertions.assertThat(personController.listeEmails("Culver")).isNotNull();
    }

    @Test
    void findAllChildsByAddressTest() {
        List<ChildAlertDto> result = personService.findAllChildsByAdsress("1509 Culver St");
        Assertions.assertThat(result).isNotNull();

        assertEquals(2, result.size());
    }

    @Test
    void addAPersonTest() {

        Person person = new Person("firstNameTest", "lastNameTest", "892 Downing Ct",
                "Culver", "97451", "841-874-7878", "soph@email.com");


        personService.addPerson(person);

        Person result = personRepository.findpersonByfirstNameAndLastName(person.getFirstName(), person.getLastName());
        assert (result.getFirstName().equals(person.getFirstName()));
        assert (result.getLastName().equals(person.getLastName()));
        Integer lastIndex = personRepository.findAllPersons().size() -1;

        personService.deletePerson((personRepository.findAllPersons().get(lastIndex).getFirstName()),(personRepository.findAllPersons().get(lastIndex).getLastName()));



    }

    @Test
    void updateAPersonTest(){

        Person person = new Person("firstNameTest", "lastNameTest", "892 Downing Ct",
                "Culver", "97451", "841-874-7878", "soph@email.com");
        personService.addPerson(person);

        Person result = personRepository.findpersonByfirstNameAndLastName(person.getFirstName(), person.getLastName());
        assert (result.getFirstName().equals(person.getFirstName()));
        assert (result.getLastName().equals(person.getLastName()));

        person.setCity("Toulon");
        person.setPhone("987-654-3210");

        personService.updatePerson(person);

        Person result2 = personRepository.findpersonByfirstNameAndLastName(person.getFirstName(), person.getLastName());
        assert (result2.getFirstName().equals(person.getFirstName()));
        assert (result2.getLastName().equals(person.getLastName()));

    }

    @Test
    void deleteAPersonTest(){

        Person person = new Person("firstNameTest", "lastNameTest", "892 Downing Ct",
                "Culver", "97451", "841-874-7878", "soph@email.com");
        personService.addPerson(person);

        Person result = personRepository.findpersonByfirstNameAndLastName(person.getFirstName(), person.getLastName());
        assert (result.getFirstName().equals(person.getFirstName()));
        assert (result.getLastName().equals(person.getLastName()));



        personService.deletePerson(person.getFirstName(), person.getLastName());

        Person result2 = personRepository.findpersonByfirstNameAndLastName(person.getFirstName(), person.getLastName());
        assert (result2.getFirstName().equals(person.getFirstName()));
        assert (result2.getLastName().equals(person.getLastName()));

    }
}
