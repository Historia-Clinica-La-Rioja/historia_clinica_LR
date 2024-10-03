package net.pladema.provincialreports.epidemiologyreports.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

@Repository
@Transactional
public class EpidemiologyReportsQueryFactory {
	private final ReportsRepositoryUtils repositoryUtils;

	public EpidemiologyReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<DenguePatientControlConsultationDetail> queryDenguePatientControl(Integer institutionId) {
		return repositoryUtils.executeQuery("EpidemiologyReports.DenguePatientControlConsultationDetail", institutionId, null, null, null);
	}

	public List<CompleteDengueConsultationDetail> queryCompleteDengue(Integer institutionId) {
		return repositoryUtils.executeQuery("EpidemiologyReports.CompleteDengueConsultationDetail", institutionId, null, null, null);
	}
}
