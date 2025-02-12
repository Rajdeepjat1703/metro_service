package com.webknot.metro_service.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
public class CheckIn {
    @Id
    private String userId;
    private String stationId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
