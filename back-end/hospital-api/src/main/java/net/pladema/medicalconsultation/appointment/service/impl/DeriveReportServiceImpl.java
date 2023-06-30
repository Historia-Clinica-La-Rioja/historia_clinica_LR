package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.HistoricDeriveReportRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricDeriveReport;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.DeriveReportService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class DeriveReportServiceImpl implements DeriveReportService {
	private final AppointmentOrderImageService appointmentOrderImageService;
	private final HistoricDeriveReportRepository historicDeriveReportRepository;

	@Override
	@Transactional
	public boolean execute(Integer destInstitutionId, Integer appointmentId) {
		log.debug("Input parameters -> destInstitutionId {}, appointmentId {}", destInstitutionId, appointmentId);
		appointmentOrderImageService.setDestInstitutionId(destInstitutionId, appointmentId);
		appointmentOrderImageService.setReportStatusId(appointmentId, EDiagnosticImageReportStatus.DERIVED.getId());
		historicDeriveReportRepository.save(new HistoricDeriveReport(appointmentId, destInstitutionId));
		return Boolean.TRUE;
	}
}
