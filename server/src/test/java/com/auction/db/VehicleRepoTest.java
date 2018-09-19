package com.auction.db;

import com.auction.db.AuctionRepo;
import com.auction.db.VehicleRepo;
import com.auction.db.model.Amount;
import com.auction.db.model.Auction;
import com.auction.db.model.Currency;
import com.auction.db.model.Vehicle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VehicleRepoTest {

    @Autowired
    private VehicleRepo vehicleRepo;


    @Test
    public void saveVechile() {
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        vehicleRepo.save(vehicle);
    }

    @Test
    public void findVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setImageUrl("raptorImage");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));

        vehicle = vehicleRepo.save(vehicle);
        Vehicle savedVehicle = vehicleRepo.getOne(vehicle.getId());

        assertThat(savedVehicle).isNotEqualTo(null);
        assertThat(savedVehicle.getId()).isEqualTo(vehicle.getId());
        assertThat(savedVehicle.getName()).isEqualTo(vehicle.getName());
        assertThat(savedVehicle.getImageUrl()).isEqualTo(vehicle.getImageUrl());
        assertThat(savedVehicle.getPrice().getCurrency()).isEqualTo(vehicle.getPrice().getCurrency());
    }

    @Test()
    public void removeVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setImageUrl("raptorImage");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));

        Vehicle savedVehicle = vehicleRepo.save(vehicle);
        vehicleRepo.delete(savedVehicle);

    }

}
