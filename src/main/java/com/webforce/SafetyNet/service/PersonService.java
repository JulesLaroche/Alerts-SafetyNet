package com.webforce.SafetyNet.service;

import com.webforce.SafetyNet.model.Firestation;
import com.webforce.SafetyNet.repository.DataBean;
import com.webforce.SafetyNet.service.dto.ChildAlertDto;
import com.webforce.SafetyNet.service.dto.FireDto;
import com.webforce.SafetyNet.model.Medicalrecord;
import com.webforce.SafetyNet.model.Person;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webforce.SafetyNet.repository.PersonRepository;
import com.webforce.SafetyNet.repository.MedicalrecordRepository;
import com.webforce.SafetyNet.repository.FirestationRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;
    private final FirestationRepository firestationRepository;
    private final DataBean dataBean;

    @Autowired
    public PersonService(PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository, FirestationRepository firestationRepository, DataBean dataBean) {
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.firestationRepository = firestationRepository;

        this.dataBean = dataBean;
    }

    public List<FireDto> findAllpersonsWithMedicalRecords(String address) {
        List<FireDto> result = new ArrayList<>();

        List<Person> persons = personRepository.findAllpersonByAddress(address);

        List<Medicalrecord> medicalrecords = medicalrecordRepository.findAllMedicalRecords();

        for (Person person : persons) {
            Medicalrecord medicalRecord = medicalRecordContainsPerson(medicalrecords, person);
            if (medicalRecord != null) {
                FireDto dto = new FireDto();
                Firestation firestation = firestationRepository.findFireStationNumberByAdress(address);
                dto.setFireStation(firestation.getStation());
                dto.setLastName(person.getLastName());
                dto.setPhoneNumber(person.getPhone());
                dto.setAge(String.valueOf(computeAge(medicalRecord.getBirthdate())));
                dto.setMedications(medicalRecord.getMedications()); // Convertir en tableau
                dto.setAllergies(medicalRecord.getAllergies()); // Convertir en tableau
                result.add(dto);
            }


        }

        return result;
    }

    private int computeAge(String birthdateOfPerson) {
        LocalDate dob = LocalDate.parse(birthdateOfPerson, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate curDate = LocalDate.now();
        int age = Period.between(dob, curDate).getYears();
        return age;
    }

    private Medicalrecord medicalRecordContainsPerson(List<Medicalrecord> medicalrecords, Person person) {
        for (Medicalrecord medicalrecord : medicalrecords) {
            if (medicalrecord.getFirstName().equals(person.getFirstName()) && medicalrecord.getLastName().equals(person.getLastName())) {
                return medicalrecord;
            }

        }
        return null;
    }

    public void addPerson(Person person) {
        dataBean.getData().getPersons().add(person);
        updateJsonDataInDataBean();
    }

    public void updatePerson(Person person) {
        Person existingPerson = findPersonByFullName(person.getFirstName(), person.getLastName());
        if (existingPerson != null) {
            existingPerson.setAdress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setPhone(person.getPhone());
            existingPerson.setEmail(person.getEmail());
            updateJsonDataInDataBean();
        }
    }

    public void deletePerson(String firstName, String lastName) {
        Person personToDelete = findPersonByFullName(firstName, lastName);
        if (personToDelete != null) {
            dataBean.getData().getPersons().remove(personToDelete);
            updateJsonDataInDataBean();
        }
    }

    private Person findPersonByFullName(String firstName, String lastName) {
        return dataBean.getData().getPersons().stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    private void updateJsonDataInDataBean() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String updatedJsonData = objectMapper.writeValueAsString(dataBean.getData());
            dataBean.setJsonData(updatedJsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ChildAlertDto> findAllChildsByAdsress(String address) {
        List<ChildAlertDto> result = new ArrayList<>();

        List<Person> persons = personRepository.findAllpersonByAddress(address);
        List<Medicalrecord> medicalrecords = medicalrecordRepository.findAllMedicalRecords();

        List<ChildAlertDto> children = new ArrayList<>();
        List<Person> adults = new ArrayList<>();

        for (Person person : persons) {
            Medicalrecord medicalRecord = medicalRecordContainsPerson(medicalrecords, person);
            if (medicalRecord != null) {
                int age = computeAge(medicalRecord.getBirthdate());
                ChildAlertDto dto = new ChildAlertDto();
                dto.setLastName(person.getLastName());
                dto.setFirstName(person.getFirstName());
                dto.setAge(String.valueOf(age));
                dto.setMember(new ArrayList<>());

                if (age <= 18) {
                    children.add(dto);
                } else {
                    adults.add(person);
                }
            }
        }

        for (ChildAlertDto child : children) {
            child.getMember().addAll(adults);
        }

        result.addAll(children);

        return result;
    }


}
