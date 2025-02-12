package com.webknot.metro_service.Controller;


import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Service.MetroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metro")
public class MetroController {
    @Autowired
    private MetroService metroService;

    @PostMapping("/check-in")
    public String checkIn(@RequestParam String userId, @RequestParam String stationId) {
        return metroService.checkIn(userId, stationId);
    }

    @PostMapping("/check-out")
    public String checkOut(@RequestParam String userId, @RequestParam String stationId) {
        return metroService.checkOut(userId, stationId);
    }

    @GetMapping("/stations")
    public List<Station> getAllStations() {
        return metroService.getAllStations();
    }

    @PostMapping("/sos")
    public String triggerSOS(@RequestParam String userId, @RequestParam String stationId) {
        return metroService.triggerSOS(userId, stationId);
    }

    @GetMapping("/active-users")
    public List<String> getActiveUsers() {
        return metroService.getActiveUsers();
    }
}
