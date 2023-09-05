package com.webforce.SafetyNet.repository;

import com.webforce.SafetyNet.model.Medicalrecord;
import com.webforce.SafetyNet.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.webforce.SafetyNet.model.Data;
import com.webforce.SafetyNet.repository.DataBean;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MedicalrecordRepository {

    private final DataBean dataBean;

    @Autowired
    public MedicalrecordRepository(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public Medicalrecord findMedicalrecordByPerson(Person person) {
        List<Medicalrecord> medicalrecords = dataBean.getData().getMedicalrecords();

        for (Medicalrecord medicalrecord : medicalrecords) {
            if (medicalrecord.getFirstName().equals(person.getFirstName()) &&
                    medicalrecord.getLastName().equals(person.getLastName())) {
                return medicalrecord;
            }
        }

        return null;
    }

    public List<Medicalrecord> findAllMedicalRecords() {
        return dataBean.getData().getMedicalrecords();
    }
}