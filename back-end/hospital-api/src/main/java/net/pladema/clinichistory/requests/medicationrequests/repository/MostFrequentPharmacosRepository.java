package net.pladema.clinichistory.requests.medicationrequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MostFrequentPharmacosRepository extends JpaRepository<MedicationStatement, Integer> {

	@Query("SELECT NEW net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo(s.pt, s.sctid) " +
			"FROM MedicationStatement ms " +
			"JOIN UserPerson up ON (ms.creationable.createdBy = up.pk.userId) " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"JOIN HealthcareProfessional hp ON (p.id = hp.personId) " +
			"JOIN MedicationRequest mr ON (hp.id = mr.doctorId) " +
			"JOIN Snomed s ON (s.id = ms.snomedId) " +
			"WHERE ms.creationable.createdBy = :professionalId " +
			"AND mr.institutionId = :institutionId " +
			"AND ms.dueDate IS NOT NULL " +
			"GROUP BY s.pt, s.sctid " +
			"ORDER BY COUNT(s.id) DESC")
	List<SnomedBo> getMostFrequentPharmacos(@Param("professionalId") Integer professionalId,
											@Param("institutionId") Integer institutionId);
}
