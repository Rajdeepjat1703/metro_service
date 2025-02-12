package com.webknot.metro_service.Service;

import com.webknot.metro_service.Model.CheckIn;
import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Repository.CheckInRepository;
import com.webknot.metro_service.Repository.StationRepository;
import com.webknot.metro_service.utils.FareCalculator;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MetroService {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private FareCalculator fareCalculator;

    public String checkIn(String userId, String stationId) {
        // Validate user's QR code or metro card
        // Save check-in time
        CheckIn checkIn = new CheckIn(userId, stationId, System.currentTimeMillis());
        checkInRepository.save(checkIn);
        return "Checked in successfully";
    }

    public String checkOut(String userId, String stationId) {
        // Calculate fare based on distance and time
        CheckIn checkIn = checkInRepository.findByUserId(userId);
        long fare = fareCalculator.calculateFare(checkIn, stationId);
        // Send fare to Payment Service
        return "Checked out successfully. Fare: " + fare;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public String triggerSOS(String userId, String stationId) {
        // Notify nearest metro manager via Kafka
        return "SOS triggered. Help is on the way.";
    }

    public static List<User> getActiveUsers() {
        // Fetch active users from Redis
        return MetroService.getActiveUsers();
    }


}
