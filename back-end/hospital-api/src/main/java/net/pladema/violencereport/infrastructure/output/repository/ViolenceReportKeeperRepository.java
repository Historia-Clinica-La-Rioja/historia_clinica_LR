package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.domain.ViolenceReportActorBo;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReportKeeper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ViolenceReportKeeperRepository extends JpaRepository<ViolenceReportKeeper, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.violencereport.domain.ViolenceReportActorBo(vrk.lastName, vrk.firstName, vrk.age, vrk.address, " +
			"d.provinceId, d.id, d.description, c.id, c.description, vrk.relationshipWithVictimId, vrk.otherRelationshipWithVictim) " +
			"FROM ViolenceReportKeeper vrk " +
			"LEFT JOIN Department d ON (d.id = vrk.municipalityId) " +
			"LEFT JOIN City c ON (c.id = vrk.cityId) " +
			"WHERE vrk.reportId = :reportId")
	ViolenceReportActorBo getKeeperByReportId(@Param("reportId") Integer reportId);

}
