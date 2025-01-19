package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.VictimKeeperReportPlacePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.VictimKeeperReportPlace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VictimKeeperReportPlaceRepository extends JpaRepository<VictimKeeperReportPlace, VictimKeeperReportPlacePK> {

	@Transactional(readOnly = true)
	@Query(" SELECT vkrp.pk.reportPlaceId " +
			"FROM VictimKeeperReportPlace vkrp " +
			"WHERE vkrp.pk.reportId = :reportId")
	List<Short> getReportPlaceIds(@Param("reportId") Integer reportId);

}
