package com.webknot.metro_service.Repository;

import com.webknot.metro_service.Model.Station;
import com.webknot.metro_service.Model.StationManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StationManagerRepository extends JpaRepository<StationManager, Long> {
    List<StationManager> findByStation(Station station);
}
