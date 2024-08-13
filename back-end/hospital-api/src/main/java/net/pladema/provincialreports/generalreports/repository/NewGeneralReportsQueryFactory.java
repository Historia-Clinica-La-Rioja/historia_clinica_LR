package net.pladema.provincialreports.generalreports.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

@Repository
@Transactional
public class NewGeneralReportsQueryFactory {

	private final ReportsRepositoryUtils repositoryUtils;

	public NewGeneralReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<EmergencyConsultationDetail> queryEmergency(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("GeneralReports.EmergencyConsultationDetail", institutionId, start, end, null);
	}

	public List<DiabeticHypertensionConsultationDetail> queryDiabetics(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("GeneralReports.DiabeticsConsultationDetail", institutionId, start, end, null);
	}

}
