package com.webforce.SafetyNet.service;

import com.webforce.SafetyNet.model.Firestation;
import com.webforce.SafetyNet.model.Medicalrecord;
import com.webforce.SafetyNet.model.Person;
import com.webforce.SafetyNet.service.dto.FireStationDto;
import com.webforce.SafetyNet.service.dto.FireStationPersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webforce.SafetyNet.repository.PersonRepository;
import com.webforce.SafetyNet.repository.MedicalrecordRepository;
import com.webforce.SafetyNet.repository.FirestationRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;

    @Autowired
    public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
    }

    public FireStationDto findAllPersonsByStationNumber(int number) {
        FireStationDto result = new FireStationDto();
        List<FireStationPersonDto> people = new ArrayList<>();
        result.setPeople(people);

        List<Firestation> firestations = firestationRepository.findAllFireStationsAddressByNumber(number);
        List<Medicalrecord> medicalrecords = medicalrecordRepository.findAllMedicalRecords();
        List<Person> persons = personRepository.findAllPersons();

        int childrenCount = 0;
        int adultsCount = 0;

        for (Person person : persons) {
            if (fireStationContainPersons(firestations, person) != null) {
                FireStationPersonDto fireStationPersonDto = new FireStationPersonDto();
                fireStationPersonDto.setFirstName(person.getFirstName());
                fireStationPersonDto.setLastName(person.getLastName());
                fireStationPersonDto.setAddress(person.getAddress());
                fireStationPersonDto.setPhone(person.getPhone());

                Medicalrecord medicalrecord = medicalrecordsContainPerson(medicalrecords, person);
                if (medicalrecord != null) {
                    int age = computeAge(medicalrecord.getBirthdate());
                    fireStationPersonDto.setBirthdate(medicalrecord.getBirthdate());

                    if (age < 18) {
                        childrenCount++;
                    } else {
                        adultsCount++;
                    }

                    result.getPeople().add(fireStationPersonDto);
                }
            }
        }

        result.setChildsCount(childrenCount);
        result.setAdultsCount(adultsCount);

        return result;
    }

    private Medicalrecord medicalrecordsContainPerson(List<Medicalrecord> medicalrecords, Person person2) {
        for(Medicalrecord medicalrecord : medicalrecords) {
            if (medicalrecord.getFirstName().equals(person2.getFirstName()) && medicalrecord.getLastName().equals(person2.getLastName())){
                return medicalrecord;
            }
        }
        return null;
    }


    private Firestation fireStationContainPersons(List<Firestation> firestations, Person person) {
        for(Firestation firestation : firestations) {
            if (firestation.getAddress().equals(person.getAddress())){
                return firestation;
            }
        }
        return null;
    }


    private int computeAge(String birthdateOfPerson) {
        LocalDate dob = LocalDate.parse(birthdateOfPerson, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate curDate = LocalDate.now();
        int age = Period.between(dob, curDate).getYears();
        return age;
    }
    }
    
    