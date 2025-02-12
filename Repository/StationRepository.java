package com.webknot.metro_service.Repository;

import com.webknot.metro_service.Model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  StationRepository extends JpaRepository<Station, String> {

}
