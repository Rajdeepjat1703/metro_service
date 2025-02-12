package com.webknot.metro_service.Repository;

import com.webknot.metro_service.Model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, String> {
    CheckIn findByUserId(String userId);


}
