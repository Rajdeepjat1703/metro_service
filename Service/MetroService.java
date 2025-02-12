package com.webknot.metro_service.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webknot.metro_service.CustomException;
import com.webknot.metro_service.Model.CheckIn;
import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Repository.CheckInRepository;
import com.webknot.metro_service.Repository.StationRepository;
import com.webknot.metro_service.utils.FareCalculator;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MetroService {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private FareCalculator fareCalculator;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserService userService; // Assume this service exists for validation

    @Autowired
    private PaymentService paymentService; // Assume this service exists for payment processing

    public String checkIn(String userId, String stationId) {
        // Validate user's QR code or metro card
        boolean isValid = userService.validateUser(userId);
        if (!isValid) {
            throw new CustomException("Invalid QR code or metro card");
        }

        // Save check-in time
        CheckIn checkIn = new CheckIn(userId, stationId, System.currentTimeMillis());
        checkInRepository.save(checkIn);

        // Add user to active users in Redis
        redisTemplate.opsForSet().add("activeUsers", userId);

        return "Checked in successfully";
    }

    public String checkOut(String userId, String stationId) {
        // Fetch check-in record
        CheckIn checkIn = checkInRepository.findByUserId(userId);
        if (checkIn == null) {
            throw new CustomException("No check-in record found for user");
        }

        // Calculate fare based on distance and time
        long fare = fareCalculator.calculateFare(checkIn, stationId);

        // Send fare to Payment Service
        paymentService.processPayment(userId, fare);

        // Update check-out time
        checkIn.setCheckOutTime(System.currentTimeMillis());
        checkInRepository.save(checkIn);

        // Remove user from active users in Redis
        redisTemplate.opsForSet().remove("activeUsers", userId);

        return "Checked out successfully. Fare: " + fare;
    }

    public List<Station> getAllStations() {
        try {
            // Check Redis cache first
            String cachedStations = redisTemplate.opsForValue().get("allStations");

            if (cachedStations != null) {
                // Deserialize JSON string to List<Station>
                return objectMapper.readValue(cachedStations, new TypeReference<List<Station>>() {
                });
            } else {
                // Fetch from database if not in cache
                List<Station> stations = stationRepository.findAll();

                // Serialize List<Station> to JSON string
                String stationsJson = objectMapper.writeValueAsString(stations);

                // Store JSON string in Redis
                redisTemplate.opsForValue().set("allStations", stationsJson);

                return stations;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

        public String triggerSOS(String userId, String stationId) {
        // Create SOS event
        String sosEvent = "SOS triggered by user: " + userId + " at station: " + stationId;

        // Publish SOS event to Kafka
        kafkaTemplate.send("sos-topic", sosEvent);

        return "SOS triggered. Help is on the way.";
    }

    public List<String> getActiveUsers() {
        // Fetch active users from Redis
        return List.copyOf(redisTemplate.opsForSet().members("activeUsers"));
    }


}
