package ar.lamansys.sgh.publicapi.reports.application.port.out;

import java.time.LocalDate;
import java.util.List;

import ar.lamansys.sgh.publicapi.reports.domain.ConsultationBo;

public interface ConsultationsByDateStorage {
	List<ConsultationBo> fetchConsultations(LocalDate dateFrom, LocalDate dateUntil, Integer institutionId, Integer hierarchicalUnitId);
}
