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

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VehicleRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private AuctionRepo auctionRepo;


    @Test
    public void save_a_vechile() {
        Vehicle vehicle = new Vehicle();
        vehicle.setName("raptor");
        vehicle.setImageUrl("raptorImage");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));

        Vehicle savedVehicle = entityManager.persistAndFlush(vehicle);

        assertThat(savedVehicle).isNotEqualTo(null);
        assertThat(savedVehicle.getId()).isEqualTo(vehicle.getId());
        assertThat(savedVehicle.getName()).isEqualTo(vehicle.getName());
        assertThat(savedVehicle.getImageUrl()).isEqualTo(vehicle.getImageUrl());
        assertThat(savedVehicle.getPrice().getCurrency()).isEqualTo(vehicle.getPrice().getCurrency());
    }

    @Test
    public void update_a_vehicle() {
        Vehicle vehicle = new Vehicle();
        Vehicle o = entityManager.persistAndFlush(vehicle);
        Long id = o.getId();

        vehicle.setId(id);
        vehicle.setName("raptor");
        vehicle.setImageUrl("raptorImage");
        vehicle.setPrice(new Amount(80000.01, Currency.CAD));
        entityManager.persistAndFlush(vehicle);

        Vehicle other = vehicleRepo.getOne(id);

        assertThat(other).isNotEqualTo(null);
        assertThat(other.getId()).isEqualTo(vehicle.getId());
        assertThat(other.getName()).isEqualTo(vehicle.getName());
        assertThat(other.getImageUrl()).isEqualTo(vehicle.getImageUrl());
        assertThat(other.getPrice().getCurrency()).isEqualTo(vehicle.getPrice().getCurrency());
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void remove_a_vehicle() {
        Vehicle vehicle = new Vehicle();
        Vehicle o = vehicleRepo.save(vehicle);
        Long id = o.getId();

        Vehicle other = vehicleRepo.getOne(id);
        assertThat(other).isNotEqualTo(null);

        vehicle.setId(id);
        vehicleRepo.delete(vehicle);

        other = vehicleRepo.getOne(id);

    }

}
