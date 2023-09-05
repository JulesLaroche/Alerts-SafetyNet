package com.webforce.SafetyNet.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class FireStationDto {

    Integer adultsCount;
    Integer childsCount;
    List<FireStationPersonDto> people;

    public Integer getAdultsCount() {
        return adultsCount;
    }

    public void setAdultsCount(Integer adultsCount) {
        this.adultsCount = adultsCount;
    }

    public Integer getChildsCount() {
        return childsCount;
    }

    public void setChildsCount(Integer childsCount) {
        this.childsCount = childsCount;
    }

    public List<FireStationPersonDto> getPeople() {
        return people;
    }

    public void setPeople(List<FireStationPersonDto> people) {
        this.people = people;
    }
}
