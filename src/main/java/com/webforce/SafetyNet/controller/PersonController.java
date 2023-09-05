package com.webforce.SafetyNet.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webforce.SafetyNet.model.Person;
import com.webforce.SafetyNet.service.dto.ChildAlertDto;
import com.webforce.SafetyNet.service.dto.FireDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.webforce.SafetyNet.repository.DataBean;
import com.webforce.SafetyNet.service.PersonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PersonController {

    private final DataBean dataBean;
    private final PersonService personService;

    @Autowired
    public PersonController(DataBean dataBean, PersonService personService) {
        this.dataBean = dataBean;
        this.personService = personService;
    }

    @GetMapping("/communityEmail")
    public List<String> getEmail(@RequestParam String city) {
        try {
            List<Person> persons = parseJsonToPersons(dataBean.getJsonData());

            List<String> emails = persons.stream()
                    .filter(person -> person.getCity().equals(city))
                    .map(Person::getEmail)
                    .collect(Collectors.toList());

            return emails;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @RequestMapping(value = "fire", method = RequestMethod.GET)
    public List<FireDto> listOfpersonsByAddress(@RequestParam(name="address")String address) {
        return this.personService.findAllpersonsWithMedicalRecords(address);
    }

    private List<Person> parseJsonToPersons(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        JsonNode personsNode = jsonNode.get("persons");

        List<Person> persons = new ArrayList<>();
        for (JsonNode personNode : personsNode) {
            Person person = objectMapper.convertValue(personNode, Person.class);
            persons.add(person);
        }

        return persons;
    }

    @RequestMapping(value = "childAlert", method = RequestMethod.GET)
    public List<ChildAlertDto> listOfChildsByAddress(@RequestParam(name="address")String address) {
        return this.personService.findAllChildsByAdsress(address);
    }




    @PostMapping("/person")
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully.");
    }

    @PutMapping("/person")
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        personService.updatePerson(person);
        return ResponseEntity.ok("Person updated successfully.");
    }

    @DeleteMapping("/person/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(@PathVariable String firstName, @PathVariable String lastName) {
        personService.deletePerson(firstName, lastName);
        return ResponseEntity.ok("Person deleted successfully.");
    }

    @GetMapping("/allPersons")
    public List<Person> getAllPersons() {
        try {
            List<Person> persons = parseJsonToPersons(dataBean.getJsonData());
            return persons;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @GetMapping("/listeEmails")
    public List<String> listeEmails(@RequestParam String city) {
        return getEmail(city);
    }




}






