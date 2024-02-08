package net.pladema.violencereport.infrastructure.output.repository;

import java.util.List;

import net.pladema.cipres.domain.SnomedBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceType;

@Repository
public interface ViolenceTypeRepository extends JpaRepository<ViolenceType, ViolenceReportSnomedPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT s.pt " +
			"FROM ViolenceType vt " +
			"JOIN Snomed s ON (s.id = vt.pk.snomedId) " +
			"WHERE vt.pk.reportId IN :reportIds")
	List<String> getViolenceTypeNamesByReportIds(@Param("reportIds") List<Integer> reportIds);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.cipres.domain.SnomedBo(s.sctid, s.pt) " +
			"FROM ViolenceType vt " +
			"JOIN Snomed s ON (s.id = vt.pk.snomedId) " +
			"WHERE vt.pk.reportId = :reportId")
	List<SnomedBo> getViolenceReportSnomedsByReportId(@Param("reportId") Integer reportId);

}
