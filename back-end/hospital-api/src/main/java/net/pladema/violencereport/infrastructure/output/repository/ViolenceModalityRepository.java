package net.pladema.violencereport.infrastructure.output.repository;

import java.util.List;

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

}
