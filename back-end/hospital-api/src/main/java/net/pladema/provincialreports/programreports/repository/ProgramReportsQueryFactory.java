package net.pladema.provincialreports.programreports.repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ProgramReportsQueryFactory {
	private final ReportsRepositoryUtils repositoryUtils;

	public ProgramReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<EpidemiologyOneConsultationDetail> queryEpidemiologyOne(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("ProgramReports.EpidemiologyOneConsultationDetail", institutionId, start, end, null);
	}

	public List<EpidemiologyTwoConsultationDetail> queryEpidemiologyTwo(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("ProgramReports.EpidemiologyTwoConsultationDetail", institutionId, start, end, null);
	}

	public List<RecuperoGeneralConsultationDetail> queryRecuperoGeneral(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("ProgramReports.RecuperoGeneralConsultationDetail", institutionId, start, end, null);
	}

	public List<OdontologicalConsultationDetail> queryRecuperoOdontologico(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("ProgramReports.RecuperoOdontologicoConsultationDetail", institutionId, start, end, null);
	}

	public List<SumarGeneralConsultationDetail> querySumarGeneral(Integer institutionId, LocalDate start, LocalDate end, Integer clinicalSpecialtyId, Integer doctorId) {
		List<SumarGeneralConsultationDetail> data = repositoryUtils.executeQuery("ProgramReports.SumarGeneralConsultationDetail", institutionId, start, end, null);

		return data.stream()
				.filter(oc -> doctorId == null || oc.getProfessionalId().equals(doctorId))
				.filter(oc -> clinicalSpecialtyId == null || oc.getClinicalSpecialtyId() != null)
				.filter(oc -> clinicalSpecialtyId == null || oc.getClinicalSpecialtyId().equals(clinicalSpecialtyId))
				.collect(Collectors.toList());
	}

	public List<OdontologicalConsultationDetail> querySumarOdontologico(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("ProgramReports.SumarOdontologicoConsultationDetail", institutionId, start, end, null);
	}
}
