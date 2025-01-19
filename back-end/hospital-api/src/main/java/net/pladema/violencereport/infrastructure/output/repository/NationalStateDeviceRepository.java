package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.NationalStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.NationalStateDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NationalStateDeviceRepository extends JpaRepository<NationalStateDevice, NationalStateDevicePK> {

	@Transactional(readOnly = true)
	@Query(" SELECT nsd.pk.nationalStateDeviceId " +
			"FROM NationalStateDevice nsd " +
			"WHERE nsd.pk.reportId = :reportId")
	List<Short> getDeviceIdsByReportId(@Param("reportId") Integer reportId);

}
