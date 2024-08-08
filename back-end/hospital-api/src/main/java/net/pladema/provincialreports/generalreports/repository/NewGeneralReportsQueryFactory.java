package net.pladema.provincialreports.generalreports.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

@Repository
@Transactional
public class NewGeneralReportsQueryFactory {

	private final ReportsRepositoryUtils reportsRepositoryUtils;

	public NewGeneralReportsQueryFactory(ReportsRepositoryUtils reportsRepositoryUtils) {
		this.reportsRepositoryUtils = reportsRepositoryUtils;
	}

	public List<EmergencyConsultationDetail> queryEmergency(Integer institutionId, LocalDate start, LocalDate end) {
		return reportsRepositoryUtils.executeQuery("GeneralReports.EmergencyConsultationDetail", EmergencyConsultationDetail.class, institutionId, start, end);
	}

	public List<DiabeticHypertensionConsultationDetail> queryDiabetics(Integer institutionId, LocalDate start, LocalDate end) {
		return reportsRepositoryUtils.executeQuery("GeneralReports.DiabeticsConsultationDetail", DiabeticHypertensionConsultationDetail.class, institutionId, start, end);
	}

	public List<DiabeticHypertensionConsultationDetail> queryHypertensive(Integer institutionId, LocalDate start, LocalDate end) {
		return reportsRepositoryUtils.executeQuery("GeneralReports.HypertensiveConsultationDetail", DiabeticHypertensionConsultationDetail.class, institutionId, start, end);
	}

	public List<ComplementaryStudiesConsultationDetail> queryComplementaryStudies(Integer institutionId, LocalDate start, LocalDate end) {
		return reportsRepositoryUtils.executeQuery("GeneralReports.ComplementaryStudiesConsultationDetail", ComplementaryStudiesConsultationDetail.class, institutionId, start, end);
	}

}
