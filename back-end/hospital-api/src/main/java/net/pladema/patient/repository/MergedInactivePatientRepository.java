package net.pladema.patient.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.repository.entity.MergedInactivePatient;

@Repository
public interface MergedInactivePatientRepository extends SGXAuditableEntityJPARepository<MergedInactivePatient, Integer> {

	@Query(value = "SELECT mip.inactivePatientId " +
			"FROM Patient pa " +
			"JOIN MergedPatient mp ON pa.id = mp.activePatientId " +
			"JOIN MergedInactivePatient mip ON mp.id = mip.mergedPatientId " +
			"WHERE mp.activePatientId = :activePatientId")
	List<Integer> findAllInactivePatientIdByActivePatientId(@Param("activePatientId") Integer activePatientId);

	@Query(value = "SELECT mip "
			+ "FROM MergedInactivePatient mip " +
			"WHERE mip.inactivePatientId = :inactivePatientId")
	Optional<MergedInactivePatient> findByInactivePatientId(@Param("inactivePatientId") Integer inactivePatientId);

	@Query(value = "SELECT mip "
			+ "FROM MergedInactivePatient mip " +
			"WHERE mip.inactivePatientId IN :inactivePatientId")
	List<MergedInactivePatient> findAlldByInactivePatientIds(@Param("inactivePatientId") List<Integer> inactivePatientId);


	@Query(value = "SELECT (case when count(mip.id)> 0 then true else false end) " +
			"FROM MergedInactivePatient mip " +
			"WHERE mip.mergedPatientId IN :mergedPatientId")
	Boolean existsByMergePatientId(@Param("mergedPatientId") Integer mergedPatientId);

}