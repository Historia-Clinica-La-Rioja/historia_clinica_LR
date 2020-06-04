package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {

	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN ClinicalSpecialtySector css ON r.clinicalSpecialtySectorId = css.id"
			+ " JOIN Sector s ON css.sectorId = s.id " + " WHERE s.institutionId =:institutionId")
	List<Bed> getAllByInstitucion(@Param("institutionId") Integer institutionId);

	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN ClinicalSpecialtySector css ON r.clinicalSpecialtySectorId = css.id"
			+ " JOIN Sector s ON css.sectorId = s.id "
			+ " WHERE b.roomId = :roomId AND s.institutionId =:institutionId")
	List<Bed> getAllByRoomAndInstitution(@Param("roomId") Integer roomId,
			@Param("institutionId") Integer institutionId);

	@Query(value = " SELECT b FROM  Bed b "
			+ " JOIN Room r ON b.roomId = r.id"
			+ " JOIN ClinicalSpecialtySector css ON r.clinicalSpecialtySectorId = css.id"
			+ " JOIN Sector s ON css.sectorId = s.id "
			+ "WHERE b.free = true AND b.roomId = :roomId AND s.institutionId =:institutionId")
	List<Bed> getAllFreeBedsByRoomAndInstitution(@Param("roomId") Integer roomId,
			@Param("institutionId") Integer institutionId);
}
