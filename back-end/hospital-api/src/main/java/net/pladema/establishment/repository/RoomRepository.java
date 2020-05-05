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
			+ " WHERE css.sectorId = :sectorId AND css.clinicalSpecialtyId = :specialtyId ")
	List<Room> getAllBySector(@Param("sectorId") Integer sectorId, @Param("specialtyId") Integer specialtyId);

}
