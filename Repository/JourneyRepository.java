package com.webknot.metro_service.Repository;

import com.webknot.metro_service.Model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findByUserIdAndIsActive(String userId, Boolean isActive);
    List<Journey> findByIsActive(Boolean isActive);
}
