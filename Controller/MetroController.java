package com.webknot.metro_service.Controller;


import com.webknot.metro_service.Model.Journey;
import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Service.MetroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metro")
public class MetroController {
    private final MetroService metroService;

    public MetroController(MetroService metroService) {
        this.metroService = metroService;
    }

    @GetMapping("/stations")

    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(metroService.getAllActiveStations());
    }

    @PostMapping("/check-in")

    public ResponseEntity<Journey> checkIn(@RequestParam String userId,
                                           @RequestParam Long sourceStationId,
                                           @RequestParam String ticketType) {
        return ResponseEntity.ok(metroService.checkIn(userId, sourceStationId, ticketType));
    }

    @PostMapping("/check-out")

    public ResponseEntity<Journey> checkOut(@RequestParam String userId,
                                            @RequestParam Long destinationStationId) {
        return ResponseEntity.ok(metroService.checkOut(userId, destinationStationId));
    }

    @PostMapping("/sos")

    public ResponseEntity<Void> triggerSOS(@RequestParam String userId) {
        metroService.triggerSOS(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active-users")

    public ResponseEntity<List<Journey>> getActiveUsers() {
        return ResponseEntity.ok(metroService.getActiveUsers());
    }
}
