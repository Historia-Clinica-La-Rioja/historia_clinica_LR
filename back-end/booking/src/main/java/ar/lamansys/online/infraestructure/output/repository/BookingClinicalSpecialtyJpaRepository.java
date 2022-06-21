package ar.lamansys.online.infraestructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.lamansys.online.infraestructure.output.entity.VBookingClinicalSpecialty;

import java.util.List;

@Repository
public interface BookingClinicalSpecialtyJpaRepository extends JpaRepository<VBookingClinicalSpecialty, Integer>{

	@Query("SELECT NEW ar.lamansys.online.infraestructure.output.entity.VBookingClinicalSpecialty(" +
	"cs.id, cs.name, cs.clinicalSpecialtyTypeId) "+
	"FROM HealthcareProfessionalSpecialty hps JOIN ClinicalSpecialty cs ON cs.id = hps.clinicalSpecialtyId " +
	"ORDER BY cs.name")
	List<VBookingClinicalSpecialty> findAllAssociatedWithAProfessional();
}
