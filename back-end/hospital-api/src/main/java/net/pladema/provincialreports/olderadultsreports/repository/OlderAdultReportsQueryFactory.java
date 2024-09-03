package net.pladema.provincialreports.olderadultsreports.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

@Repository
@Transactional
public class OlderAdultReportsQueryFactory {
	private final ReportsRepositoryUtils repositoryUtils;

	public OlderAdultReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<OlderAdultsOutpatientConsultationDetail> queryOlderAdultOutpatient(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OlderAdultsReports.OlderAdultsOutpatientConsultationDetail", institutionId, start, end, null);
	}

	public List<OlderAdultsHospitalizationConsultationDetail> queryOlderAdultHospitalization(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OlderAdultsReports.OlderAdultsHospitalizationConsultationDetail", institutionId, start, end, null);
	}

	public List<PolypharmacyConsultationDetail> queryPolypharmacy(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OlderAdultsReports.PolypharmacyConsultationDetail", institutionId, start, end, null);
	}
}
