package net.pladema.provincialreports.pregnantpeoplereports.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

@Repository
@Transactional
public class PregnantPeopleReportsQueryFactory {

	private final ReportsRepositoryUtils repositoryUtils;

	public PregnantPeopleReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<PregnantAttentionsConsultationDetail> queryPregnantAttentions(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("PregnantPeopleReports.PregnantAttentionsConsultationDetail", institutionId, start, end, null);
	}

	public List<PregnantControlsConsultationDetail> queryPregnantControls(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("PregnantPeopleReports.PregnantControlsConsultationDetail", institutionId, start, end, null);
	}
}