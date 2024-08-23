package net.pladema.provincialreports.nursingreports.repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class NursingReportsQueryFactory {

	private final ReportsRepositoryUtils repositoryUtils;

	public NursingReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<NursingEmergencyConsultationDetail> queryNursingEmergency(Integer institutionId) {
		return repositoryUtils.executeQuery("NursingReports.NursingEmergencyConsultationDetail", institutionId, null, null, null);
	}

	public List<NursingOutpatientConsultationDetail> queryNursingOutpatient(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("NursingReports.NursingOutpatientConsultationDetail", institutionId, start, end, null);
	}

	public List<NursingHospitalizationConsultationDetail> queryNursingHospitalization(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("NursingReports.NursingHospitalizationConsultationDetail", institutionId, start, end, null);
	}

	public List<NursingProceduresConsultationDetail> queryNursingProcedures(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("NursingReports.NursingProceduresConsultationDetail", institutionId, start, end, null);
	}

	public List<NursingVaccineConsultationDetail> queryNursingVaccines(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("NursingReports.NursingVaccineConsultationDetail", institutionId, start, end, null);
	}
}
