package com.webforce.SafetyNet.repository;

import com.webforce.SafetyNet.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.webforce.SafetyNet.model.Data;
import com.webforce.SafetyNet.repository.DataBean;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {

    private final DataBean dataBean;

    @Autowired
    public PersonRepository(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public List<Person> findAllpersonByAddress(String address) {
        return dataBean.getData().getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    public List<Person> findAllPersons() {
        return dataBean.getData().getPersons();
    }

    public Person findpersonByfirstNameAndLastName(String firstName, String lastName){

        return dataBean.getData().getPersons().stream()
                .filter(p->p.getFirstName().equals(firstName))
                .filter(p->p.getLastName().equals(lastName))
                .findFirst()
                .orElseGet(()->new Person());
    }
}