package net.pladema.violencereport.infrastructure.output.repository;

import java.util.List;

import ar.lamansys.sgh.shared.domain.FilterOptionBo;
import net.pladema.cipres.domain.SnomedBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceModality;

@Repository
public interface ViolenceModalityRepository extends JpaRepository<ViolenceModality, ViolenceReportSnomedPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT s.pt " +
			"FROM ViolenceModality vm " +
			"JOIN Snomed s ON (s.id = vm.pk.snomedId) " +
			"WHERE vm.pk.reportId IN :reportIds")
	List<String> getViolenceModalityNamesByReportIds(@Param("reportIds") List<Integer> reportIds);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.cipres.domain.SnomedBo(s.sctid, s.pt) " +
			"FROM ViolenceModality vm " +
			"JOIN Snomed s ON (s.id = vm.pk.snomedId) " +
			"WHERE vm.pk.reportId = :reportId")
	List<SnomedBo> getViolenceReportSnomedsByReportId(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW ar.lamansys.sgh.shared.domain.FilterOptionBo(s.id, s.pt) " +
			"FROM ViolenceModality vm " +
			"JOIN Snomed s ON (s.id = vm.pk.snomedId) " +
			"JOIN ViolenceReport vr ON (vr.id = vm.pk.reportId) " +
			"WHERE vr.patientId = :patientId")
	List<FilterOptionBo> getModalityFilterByPatientId(@Param("patientId") Integer patientId);
}
