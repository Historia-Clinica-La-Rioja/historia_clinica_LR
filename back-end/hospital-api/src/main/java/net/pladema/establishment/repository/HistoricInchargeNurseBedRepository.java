package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HistoricInchargeNurseBed;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HistoricInchargeNurseBedRepository extends SGXAuditableEntityJPARepository<HistoricInchargeNurseBed, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT hinb " +
			"FROM HistoricInchargeNurseBed hinb " +
			"WHERE hinb.bedId = :bedId " +
			"ORDER BY hinb.id DESC")
	List<HistoricInchargeNurseBed> getLatestHistoricInchargeNurseBedByBedId(@Param("bedId") Integer bedId, Pageable pageable);
}
