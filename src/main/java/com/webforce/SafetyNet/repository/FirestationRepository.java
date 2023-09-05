package com.webforce.SafetyNet.repository;

import com.webforce.SafetyNet.model.Firestation;
import com.webforce.SafetyNet.repository.DataBean;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FirestationRepository {

    private final DataBean dataBean;

    public FirestationRepository(DataBean dataBean) {
        this.dataBean = dataBean;
    }


    public Firestation findFireStationNumberByAdress(String address) {
        return dataBean.getData().getFirestations().stream().filter(p->p.getAddress().equals(address)).findFirst().orElseGet(()->new Firestation());

    }

    public Firestation findFireStationByStationNumber(Integer stationNumber) {
        return dataBean.getData().getFirestations().stream()
                .filter(p -> p.getStation().equals(String.valueOf(stationNumber))) // Convertir en String pour la comparaison
                .findFirst()
                .orElseGet(Firestation::new);
    }

    public List<Firestation> findAllFireStationsAddressByNumber(Integer number) {
        return dataBean.getData().getFirestations().stream().filter(f->f.getStation().equals(number.toString())).collect(Collectors.toList());
    }
}
