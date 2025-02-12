package com.webknot.metro_service.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Station {
    @Id
    private String stationId;
    private String stationName;
    private String location;
}
