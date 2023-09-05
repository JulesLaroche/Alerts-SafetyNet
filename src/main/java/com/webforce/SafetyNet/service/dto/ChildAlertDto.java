package com.webforce.SafetyNet.service.dto;

import com.webforce.SafetyNet.model.Person;
import lombok.Data;

import java.util.List;

@Data
public class ChildAlertDto {
    private String firstName;
    private String lastName;
    private String age;
    List<Person> member;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<Person> getMember() {
        return member;
    }

    public void setMember(List<Person> member) {
        this.member = member;
    }
}
