package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ViolenceTypeRepository extends JpaRepository<ViolenceType, ViolenceReportSnomedPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT s.pt " +
			"FROM ViolenceType vt " +
			"JOIN Snomed s ON (s.id = vt.pk.snomedId) " +
			"WHERE vt.pk.reportId IN :reportIds")
	List<String> getViolenceTypeNamesByReportIds(@Param("reportIds") List<Integer> reportIds);

}
