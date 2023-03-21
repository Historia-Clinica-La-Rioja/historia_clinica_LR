package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.AuditablePatient;

import net.pladema.patient.service.domain.AuditablePatientInfoBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Repository
public interface AuditablePatientRepository extends JpaRepository<AuditablePatient, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.patient.service.domain.AuditablePatientInfoBo(ap.message, i.name, ap.createdOn, " +
			"p.firstName, p.middleNames, p.lastName, p.otherLastNames, pe.nameSelfDetermination) " +
			"FROM AuditablePatient ap " +
			"JOIN Institution i ON (ap.institutionId = i.id) " +
			"JOIN UserPerson up ON (ap.createdBy = up.pk.userId) " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE ap.patientId = :patientId " +
			"ORDER BY ap.createdOn desc")
	Stream<AuditablePatientInfoBo> getLastSelectionForAnAudict(@Param("patientId") Integer patientId);

}
