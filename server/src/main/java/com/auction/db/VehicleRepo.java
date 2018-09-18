package com.auction.db;

import com.auction.db.model.User;
import com.auction.db.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VehicleRepo extends JpaRepository<Vehicle, Long> {
}
