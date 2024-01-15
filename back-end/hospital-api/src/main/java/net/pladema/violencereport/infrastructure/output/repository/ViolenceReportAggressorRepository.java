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
	@Query(" SELECT NEW net.pladema.violencereport.domain.ViolenceReportAggressorBo(vra.lastName, vra.firstName, vra.age, vra.address, d.id, d.description, " +
			"vra.relationshipWithVictimId, vra.otherRelationShipWithVictim, vra.hasGuns, vra.hasBeenTreated, vra.belongsToSecurityForces, vra.inDuty, " +
			"vra.securityForceTypeId, vra.liveTogetherStatusId, vra.relationshipLengthId, vra.violenceFrequencyId, vra.criminalRecordStatusId) " +
			"FROM ViolenceReportAggressor vra " +
			"LEFT JOIN Department d ON (d.id = vra.municipalityId) " +
			"WHERE vra.reportId = :reportId")
	List<ViolenceReportAggressorBo> getAllByReportId(@Param("reportId") Integer reportId);

}
