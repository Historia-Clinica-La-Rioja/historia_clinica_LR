package net.pladema.establishment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.establishment.repository.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	@Query(value = " SELECT r FROM  Room r "
			+ " INNER JOIN ClinicalSpecialtySector css ON css.id = r.clinicalSpecialtySectorId "
			+ " INNER JOIN Sector s ON s.id = css.sectorId "
			+ " WHERE css.sectorId = :sectorId AND css.clinicalSpecialtyId = :specialtyId "
			+ " AND s.institutionId = :institutionId ")
	List<Room> getAllBySectorAndInstitution(@Param("sectorId") Integer sectorId,
			@Param("specialtyId") Integer specialtyId, @Param("institutionId") Integer institutionId);


	@Query(value = " SELECT r FROM  Room AS r "
			+ " INNER JOIN ClinicalSpecialtySector css ON css.id = r.clinicalSpecialtySectorId "
			+ " INNER JOIN Sector s ON s.id = css.sectorId "
			+ " WHERE s.institutionId = :institutionId ")
	List<Room> getAllByInstitution( @Param("institutionId") Integer institutionId);

}
