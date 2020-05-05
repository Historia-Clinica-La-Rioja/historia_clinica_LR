package net.pladema.establishment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.establishment.repository.entity.Bed;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {

	@Query(value = " SELECT b FROM  Bed b WHERE b.roomId = :roomId ")
	List<Bed> getAllByRoom(@Param("roomId") Integer roomId);

}
