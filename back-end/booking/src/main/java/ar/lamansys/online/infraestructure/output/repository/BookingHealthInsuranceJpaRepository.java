package ar.lamansys.online.infraestructure.output.repository;

import ar.lamansys.online.infraestructure.output.entity.VBookingHealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingHealthInsuranceJpaRepository extends JpaRepository<VBookingHealthInsurance, Integer>{
}

