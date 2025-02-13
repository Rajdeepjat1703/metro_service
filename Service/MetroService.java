package com.webknot.metro_service.Service;

import com.webknot.metro_service.MetroException;
import com.webknot.metro_service.Model.Journey;
import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Model.StationManager;
import com.webknot.metro_service.Model.TicketType;
import com.webknot.metro_service.Repository.JourneyRepository;
import com.webknot.metro_service.Repository.StationManagerRepository;
import com.webknot.metro_service.Repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MetroService {
    private final StationRepository stationRepository;
    private final JourneyRepository journeyRepository;
    private final StationManagerRepository managerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MetroService(StationRepository stationRepository,
                        JourneyRepository journeyRepository,
                        StationManagerRepository managerRepository,
                        KafkaTemplate<String, Object> kafkaTemplate) {
        this.stationRepository = stationRepository;
        this.journeyRepository = journeyRepository;
        this.managerRepository = managerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    public List<Station> getAllActiveStations() {
        log.info("Fetching all active stations");
        return stationRepository.findByIsActive(true);
    }

    public Journey checkIn(String userId, Long sourceStationId, String ticketType) {
        log.info("Processing check-in for user: {}", userId);
        Station station = stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new MetroException("Station not found"));

        Journey journey = new Journey();
        journey.setUserId(userId);
        journey.setSourceStation(station);
        journey.setCheckInTime(LocalDateTime.now());
        journey.setTicketType(TicketType.valueOf(ticketType.toUpperCase()));

        journey.setIsActive(true);

        return journeyRepository.save(journey);
    }

    public Journey checkOut(String userId, Long destinationStationId) {
        log.info("Processing check-out for user: {}", userId);
        Journey activeJourney = journeyRepository.findByUserIdAndIsActive(userId, true)
                .stream().findFirst()
                .orElseThrow(() -> new MetroException("No active journey found"));

        Station destination = stationRepository.findById(destinationStationId)
                .orElseThrow(() -> new MetroException("Station not found"));

        activeJourney.setDestinationStation(destination);
        activeJourney.setCheckOutTime(LocalDateTime.now());
        activeJourney.setIsActive(false);

        // Calculate time spent
        Duration duration = Duration.between(activeJourney.getCheckInTime(), activeJourney.getCheckOutTime());
        if (duration.toMinutes() > 90) {
            kafkaTemplate.send("penalty-topic", Map.of(
                    "userId", userId,
                    "journeyId", activeJourney.getId(),
                    "duration", duration.toMinutes()
            ));
        }

        return journeyRepository.save(activeJourney);
    }

    public void triggerSOS(String userId) {
        log.info("Processing SOS for user: {}", userId);
        Journey activeJourney = journeyRepository.findByUserIdAndIsActive(userId, true)
                .stream().findFirst()
                .orElseThrow(() -> new MetroException("No active journey found"));

        List<StationManager> managers = managerRepository.findByStation(activeJourney.getSourceStation());

        // Notify station managers through Kafka
        kafkaTemplate.send("sos-alert-topic", Map.of(
                "userId", userId,
                "stationId", activeJourney.getSourceStation().getId(),
                "stationManagers", managers.stream().map(StationManager::getEmail).toList()
        ));
    }


    public List<Journey> getActiveUsers() {
        log.info("Fetching all active users");
        return journeyRepository.findByIsActive(true);
    }
}
