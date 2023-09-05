package com.webforce.SafetyNet.controller;


import com.webforce.SafetyNet.model.Medicalrecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webforce.SafetyNet.repository.DataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MedicalrecordController {

    private final DataBean dataBean;

    @Autowired
    public MedicalrecordController(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    @GetMapping("/medicalrecords")
    public List<Medicalrecord> getAllMedicalrecords() {
        try {
            List<Medicalrecord> medicalrecords = parseJsonToMedicalrecords(dataBean.getJsonData());
            return medicalrecords;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Medicalrecord> parseJsonToMedicalrecords(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        JsonNode medicalrecordsNode = jsonNode.get("medicalrecords");

        List<Medicalrecord> medicalrecords = new ArrayList<>();
        for (JsonNode medicalrecordNode : medicalrecordsNode) {
            Medicalrecord medicalrecord = objectMapper.convertValue(medicalrecordNode, Medicalrecord.class);
            medicalrecords.add(medicalrecord);
        }

        return medicalrecords;
    }
}

