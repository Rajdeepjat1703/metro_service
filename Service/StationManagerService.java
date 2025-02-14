package com.webknot.metro_service.Service;

import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Model.StationManager;
import com.webknot.metro_service.Repository.StationManagerRepository;
import com.webknot.metro_service.Repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationManagerService {

    private final StationManagerRepository stationManagerRepository;
    private final StationRepository stationRepository;

    public StationManagerService(StationManagerRepository stationManagerRepository, StationRepository stationRepository) {
        this.stationManagerRepository = stationManagerRepository;
        this.stationRepository = stationRepository;
    }

    public List<StationManager> getManagersByStation(Long stationId) {
        Station station = stationRepository.findById(stationId).orElseThrow(() -> new RuntimeException("Station not found"));
        return stationManagerRepository.findByStation(station);
    }
}
