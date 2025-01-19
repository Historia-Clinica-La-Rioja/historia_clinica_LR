package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.domain.ViolenceReportAggressorBo;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportAggressor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ViolenceReportAggressorRepository extends JpaRepository<ViolenceReportAggressor, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.violencereport.domain.ViolenceReportAggressorBo(vra.lastName, vra.firstName, vra.age, vra.address, d.id, d.description, c.id, c.description, " +
			"vra.relationshipWithVictimId, vra.otherRelationShipWithVictim, vra.hasGuns, vra.hasBeenTreated, vra.belongsToSecurityForces, vra.inDuty, " +
			"vra.securityForceTypeId, vra.liveTogetherStatusId, vra.relationshipLengthId, vra.violenceFrequencyId, vra.criminalRecordStatusId) " +
			"FROM ViolenceReportAggressor vra " +
			"LEFT JOIN Department d ON (d.id = vra.municipalityId) " +
			"LEFT JOIN City c ON (c.id = vra.cityId) " +
			"WHERE vra.reportId = :reportId")
	List<ViolenceReportAggressorBo> getAllByReportId(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.violencereport.domain.ViolenceReportAggressorBo(vra.lastName, vra.firstName, vra.age, vra.address, vra.municipalityId, vra.cityId, " +
			"vra.relationshipWithVictimId, vra.otherRelationShipWithVictim, vra.hasGuns, vra.hasBeenTreated, vra.belongsToSecurityForces, vra.inDuty, " +
			"vra.securityForceTypeId, vra.liveTogetherStatusId, vra.relationshipLengthId, vra.violenceFrequencyId, vra.criminalRecordStatusId) " +
			"FROM ViolenceReportAggressor vra " +
			"JOIN ViolenceReport vr ON (vra.reportId = vr.id) " +
			"WHERE vr.patientId = :patientId " +
			"AND vr.situationId = :situationId " +
			"AND vr.evolutionId = :evolutionId")
	List<ViolenceReportAggressorBo> getAllFromLastEvolution(@Param("patientId") Integer patientId, @Param("situationId") Short situationId, @Param("evolutionId") Short evolutionId);
}
