package com.webforce.SafetyNet.repository;
import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webforce.SafetyNet.model.Data;
import com.webforce.SafetyNet.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataBean {

    private String jsonData;
    private List<Person> persons;
    private Data data;

    @Autowired
    public DataBean(ResourceLoader resourceLoader) {
        loadData(resourceLoader);
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private void loadData(ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource("classpath:data.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());

            jsonData = jsonNode.toString();
            data = objectMapper.readValue(jsonData, Data.class);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJsonData() {
        return jsonData;
    }

    public Data getData() {
        return data;
    }


}


