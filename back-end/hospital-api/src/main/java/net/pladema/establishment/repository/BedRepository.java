package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {

	@Query(value = " SELECT b FROM  Bed b WHERE b.roomId = :roomId ")
	List<Bed> getAllByRoom(@Param("roomId") Integer roomId);

	@Query(value = " SELECT b FROM  Bed b WHERE b.free = true AND b.roomId = :roomId ")
	List<Bed> getAllFreeBedsByRoom(@Param("roomId") Integer roomId);
}
