package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ToothIndicesRepository extends JpaRepository<ToothIndices, Integer>  {

    @Query("SELECT ti " +
           "FROM ToothIndices ti " +
           "WHERE ti.patientId = :patientId ")
    List<ToothIndices> getByPatientId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT ti " +
			"FROM ToothIndices ti " +
			"WHERE ti.patientId = :patientId " +
			"AND ti.toothId = :toothId")
	Optional<ToothIndices> getByPatientToothId(@Param("patientId") Integer patientId, @Param("toothId") String toothId);

	@Transactional
	@Modifying
	@Query("DELETE FROM ToothIndices ti " +
			"WHERE ti.patientId = :patientId")
	void deleteByPatientId(@Param("patientId") Integer patientId);

}
