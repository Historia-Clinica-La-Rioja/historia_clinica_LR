package net.pladema.establishment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@Repository
public interface ClinicalSpecialtySectorRepository extends JpaRepository<ClinicalSpecialtySector, Integer> {

	@Query(value = " SELECT cs FROM  ClinicalSpecialtySector css "
			+ " INNER JOIN ClinicalSpecialty cs ON cs.id = css.clinicalSpecialtyId "
			+ " INNER JOIN Sector s ON css.sectorId = s.id "
			+ " WHERE css.sectorId = :idSector AND s.institutionId = :institutionId")
	List<ClinicalSpecialty> getAllBySectorAndInstitution(@Param("idSector") Integer idSector,
			@Param("institutionId") Integer institutionId);

}
