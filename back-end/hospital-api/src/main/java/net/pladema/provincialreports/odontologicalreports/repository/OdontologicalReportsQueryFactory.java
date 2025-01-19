package net.pladema.provincialreports.odontologicalreports.repository;

import net.pladema.provincialreports.reportformat.repository.ReportsRepositoryUtils;

import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class OdontologicalReportsQueryFactory {
	private final ReportsRepositoryUtils repositoryUtils;

	public OdontologicalReportsQueryFactory(ReportsRepositoryUtils repositoryUtils) {
		this.repositoryUtils = repositoryUtils;
	}

	public List<OdontologyConsultationDetail> queryFirstLevelPromotion(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OdontologicalReports.PromocionPrimerNivelConsultationDetail", institutionId, start, end, null);
	}

	public List<OdontologyConsultationDetail> queryFirstLevelPrevention(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OdontologicalReports.PrevencionPrimerNivelConsultationDetail", institutionId, start, end, null);
	}

	public List<OdontologyConsultationDetail> queryFirstLevelGroupPrevention(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OdontologicalReports.PrevencionGrupalPrimerNivelConsultationDetail", institutionId, start, end, null);
	}

	public List<OdontologyConsultationDetail> querySecondLevelOperation(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OdontologicalReports.OperatoriaSegundoNivelConsultationDetail", institutionId, start, end, null);
	}

	public List<OdontologyConsultationDetail> querySecondLevelEndodontics(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OdontologicalReports.EndodonciaSegundoNivelConsultationDetail", institutionId, start, end, null);
	}

	public List<OdontologicalProceduresConsultationDetail> queryOdontologicalProcedures(Integer institutionId, LocalDate start, LocalDate end) {
		return repositoryUtils.executeQuery("OdontologicalReports.OdontologicalProceduresConsultationDetail", institutionId, start, end, null);
	}
}
