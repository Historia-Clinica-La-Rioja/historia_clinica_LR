package net.pladema.emergencycare.triage.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.emergencycare.triage.repository.TriageRepositoryCustom;
import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TriageRepository extends SGXAuditableEntityJPARepository<Triage, Integer>, TriageRepositoryCustom {

	List<Triage> findAllByEmergencyCareEpisodeIdOrderByIdDesc(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.emergencycare.repository.domain.ProfessionalPersonVo(p.firstName, " +
			"p.lastName, pe.nameSelfDetermination, p.middleNames, p.otherLastNames) " +
			"FROM Triage AS t " +
			"JOIN UserPerson up ON (up.pk.userId = t.updateable.updatedBy) " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE t.id = :id")
	ProfessionalPersonVo getTriageRelatedProfessionalInfo(@Param("id") Integer id);

}