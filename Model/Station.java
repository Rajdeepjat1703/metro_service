package com.webknot.metro_service.Model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "station")
public class Station implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String code;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @JsonIgnore
    @OneToMany(mappedBy = "sourceStation", cascade = CascadeType.ALL)
    private List<Journey> journeysFromStation;

    @JsonIgnore
    @OneToMany(mappedBy = "destinationStation", cascade = CascadeType.ALL)
    private List<Journey> journeysToStation;

    @JsonIgnore
    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<StationManager> stationManagers;
}
