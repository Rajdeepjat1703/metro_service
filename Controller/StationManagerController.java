package com.webknot.metro_service.Controller;

import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Model.StationManager;
import com.webknot.metro_service.Service.StationManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station-managers")
public class StationManagerController {

    private final StationManagerService stationManagerService;

    public StationManagerController(StationManagerService stationManagerService) {
        this.stationManagerService = stationManagerService;
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<List<StationManager>> getManagersByStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(stationManagerService.getManagersByStation(stationId));
    }
}
