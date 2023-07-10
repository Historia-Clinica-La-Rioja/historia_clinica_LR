package net.pladema.patient.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.person.repository.domain.PersonSearchResultVo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.repository.entity.MergedInactivePatient;

@Repository
public interface MergedInactivePatientRepository extends SGXAuditableEntityJPARepository<MergedInactivePatient, Integer> {

	@Query(value = "SELECT mip.inactivePatientId " +
			"FROM Patient pa " +
			"JOIN MergedPatient mp ON pa.id = mp.activePatientId AND mp.deleteable.deleted = false " +
			"JOIN MergedInactivePatient mip ON mp.id = mip.mergedPatientId AND mip.deleteable.deleted = false " +
			"WHERE mp.activePatientId = :activePatientId")
	List<Integer> findAllInactivePatientIdByActivePatientId(@Param("activePatientId") Integer activePatientId);

	@Query(value = "SELECT mip "
			+ "FROM MergedInactivePatient mip " +
			"WHERE mip.inactivePatientId = :inactivePatientId " +
			"AND mip.deleteable.deleted = false")
	Optional<MergedInactivePatient> findByInactivePatientId(@Param("inactivePatientId") Integer inactivePatientId);

	@Query(value = "SELECT mip "
			+ "FROM MergedInactivePatient mip " +
			"WHERE mip.inactivePatientId IN :inactivePatientId " +
			"AND mip.deleteable.deleted = false")
	List<MergedInactivePatient> findAlldByInactivePatientIds(@Param("inactivePatientId") List<Integer> inactivePatientId);


	@Query(value = "SELECT (case when count(mip.id)> 0 then true else false end) " +
			"FROM MergedInactivePatient mip " +
			"WHERE mip.mergedPatientId IN :mergedPatientId " +
			"AND mip.deleteable.deleted = false")
	Boolean existsByMergePatientId(@Param("mergedPatientId") Integer mergedPatientId);

	@Query(value = "SELECT new net.pladema.person.repository.domain.PersonSearchResultVo(" +
			"pa.id, p.firstName, p.middleNames, p.lastName, p.otherLastNames, p.identificationTypeId," +
			"p.identificationNumber, p.birthDate, p.genderId, pex.phoneNumber, pex.phonePrefix, pex.nameSelfDetermination, pa.typeId )" +
			"FROM MergedInactivePatient mip " +
			"JOIN MergedPatient mp ON mip.mergedPatientId = mp.id " +
			"JOIN Patient pa ON mip.inactivePatientId = pa.id " +
			"JOIN Person p ON pa.personId = p.id " +
			"JOIN PersonExtended pex ON pex.id = p.id " +
			"WHERE mp.activePatientId = :activePatientId " +
			"AND mip.deleteable.deleted = false ")
	List<PersonSearchResultVo> findMergedPersonInfoByActivePatientId(@Param("activePatientId") Integer activePatientId);


}