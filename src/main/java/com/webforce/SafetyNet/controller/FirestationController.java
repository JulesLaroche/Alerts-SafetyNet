package com.webforce.SafetyNet.controller;

import com.webforce.SafetyNet.model.Firestation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webforce.SafetyNet.service.FirestationService;
import com.webforce.SafetyNet.service.PersonService;
import com.webforce.SafetyNet.service.dto.FireStationDto;
import com.webforce.SafetyNet.service.dto.FireStationPersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.webforce.SafetyNet.repository.DataBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class FirestationController {

    private final DataBean dataBean;
    private final FirestationService firestationService;
    private final PersonService personService;

    @Autowired
    public FirestationController(DataBean dataBean, FirestationService firestationService, PersonService personService) {
        this.dataBean = dataBean;
        this.firestationService = firestationService;
        this.personService = personService;
    }

    @GetMapping("/allFirestation")
    public List<Firestation> getAllFirestation() {
        try {
            List<Firestation> firestations = parseJsonToFirestations(dataBean.getJsonData());
            return firestations;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @RequestMapping(value = "firestation", method = RequestMethod.GET)
    public FireStationDto personsListByFireStation(@RequestParam(name="stationNumber") int number) {

        return this.firestationService.findAllPersonsByStationNumber(number);
    }




    private List<Firestation> parseJsonToFirestations(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        JsonNode firestationsNode = jsonNode.get("firestations");

        List<Firestation> firestations = new ArrayList<>();
        for (JsonNode firestationNode : firestationsNode) {
            Firestation firestation = objectMapper.convertValue(firestationNode, Firestation.class);
            firestations.add(firestation);
        }

        return firestations;
    }
}
