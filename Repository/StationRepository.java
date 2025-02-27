package com.webknot.metro_service.Repository;

import com.webknot.metro_service.Model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByIsActive(Boolean isActive);

}

